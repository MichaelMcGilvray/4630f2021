package com.example.twitchclipsfinder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.json.*;


public class Clip_Watching extends AppCompatActivity {
    private TextView title;
    private TextView broadcaster;
    private TextView views;
    private Button openInTwitchButton;

    private ImageButton fullscreenButton;
    private WebView webView;
    private boolean isFullscreened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_watching);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Enable up button

        openInTwitchButton = findViewById(R.id.openInTwitchButton);
        title = findViewById(R.id.title);
        openInTwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Clip clip = (Clip) intent.getSerializableExtra("clip");

                String Stream_created = "";

                try {
                    Stream_created = new clipGenerator().getStreamStartDate(clip._video_id);
                } catch (IOException e) {
                    // e.printStackTrace();
                    noStreamExists();
                    return;
                } catch (JSONException e) {
                    // e.printStackTrace();
                    noStreamExists();
                    return;
                } catch (ExecutionException e) {
                    // e.printStackTrace();
                    noStreamExists();
                    return;
                } catch (InterruptedException e) {
                    // e.printStackTrace();
                    noStreamExists();
                    return;
                }

                String Stream_created_time = Stream_created.replace("T", " ").replace("Z", "");
                String Clip_created_time = clip._created_at.replace("T", " ").replace("Z", "");

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Date Stream_created_date = new Date(0, 0, 0);
                Date Clip_created_date = new Date(0, 0, 0);
                try {
                    Stream_created_date = formatter.parse(Stream_created_time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    Clip_created_date = formatter.parse(Clip_created_time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long result = Clip_created_date.getTime() - Stream_created_date.getTime();
                long result_sec = result / 1000 % 60;
                long result_min = result / (60*1000)%60;
                long result_hour = result / (60*60*1000)%24;
                String result_time = String.valueOf(result_hour) + "h" + String.valueOf(result_min) + "m" + String.valueOf(result_sec) + "s";

                System.out.print("Stream = " + Stream_created_date + "\n");
                System.out.print("Clip = " + Clip_created_date + "\n");

                Intent newIntenet = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitch.tv/videos/" + clip._video_id + "?t=" + result_time));
                startActivity(newIntenet);
            }
        });

        fullscreenButton = findViewById(R.id.fullscreenButton);
        webView = findViewById(R.id.webView);
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("Fullscreen clicked!\n");
                if (isFullscreened) {
                    LinearLayout.LayoutParams imageParameters = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 650);
                    webView.setLayoutParams(imageParameters);

                    fullscreenButton.setImageResource(R.drawable.ic_baseline_fullscreen_24);

                    openInTwitchButton.setVisibility(View.VISIBLE);
                    isFullscreened = false;
                } else {
                     ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
                     webView.setLayoutParams(layoutParams);

                    fullscreenButton.setImageResource(R.drawable.ic_baseline_fullscreen_exit_24);

                    openInTwitchButton.setVisibility(View.GONE);
                    isFullscreened = true;
                }
            }
        });

        setupClip();
    }

    public void noStreamExists() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.clipWatchingLayout), "Unfortunately, that stream no longer exists", 2000);
        snackbar.show();
    }

    public void setupClip() {
        System.out.print("setting up clip\n");
        // Get the extra data we pass with the intent when this activity is started
        Intent intent = getIntent();
        Clip clip = (Clip) intent.getSerializableExtra("clip");

        title = findViewById(R.id.title);
        broadcaster = findViewById(R.id.broadcaster);
        views = findViewById(R.id.views);
        
        title.setText(clip._title);
        broadcaster.setText(clip._broadcaster_name);
        // NumberFormat adds the commas to views
        views.setText((NumberFormat.getInstance().format(clip._views)) + " views");

        loadVideo(clip._embed_url);

        clip.print();
    }


    public void loadVideo(String embed_url) {
        WebView webView = findViewById(R.id.webView);

        // Setup the webView settings
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        // Load the clip using TwitchClipsFinder site I made
        String TwitchClipsFinderURL = "https://michaelmcgilvray.github.io/Twitch-Clips-Finder/?clipURL=";
        webView.loadUrl(TwitchClipsFinderURL + embed_url);
        
        System.out.print("Video started\n");
    }
}