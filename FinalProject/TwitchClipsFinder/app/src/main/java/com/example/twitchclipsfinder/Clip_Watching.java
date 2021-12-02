package com.example.twitchclipsfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.json.*;
import com.squareup.picasso.Picasso;

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

        setupClip();
        setupRecommendedClips();



        // Setup fullscreen button
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



        // Setup openInTwitch button
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
    }

    public void noStreamExists() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.clipWatchingLayout), "Unfortunately, that stream no longer exists", 2000);
        snackbar.show();
    }



    public void setupClip() {
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
    }



    public void setupRecommendedClips() {
        Intent intent = getIntent();
        Clip clip = (Clip) intent.getSerializableExtra("clip");

        Tuple tuple = new Tuple();

        try {
            tuple = new clipGenerator().getClipsFromStreamer(clip._broadcaster_id, "", "", "");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Select a random clip
        int min = 0;
        int max = tuple._clips.length - 1;
        int i = (int) (Math.random() * (max - min + 1) + min);
        Clip randomClip = tuple._clips[i];

        // Setup the clip view
        LinearLayout linearLayout = findViewById(R.id.recommendedClipsList);
        ImageView imageView = new ImageView(this);

        // Use Picasso to load image into ImageView using a URL
        Picasso.get().load(randomClip._thumbnail).into(imageView);

        LinearLayout.LayoutParams imageParameters = new LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 500);
        imageView.setLayoutParams(imageParameters);

        // Make the ImageView clickable and open the clip watching screen
        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Clip_Watching.this, Clip_Watching.class);
                intent.putExtra("clip", randomClip);
                startActivity(intent);
            }
        });

        // Add the imageView to the linearLayout (scrolling section in app)
        linearLayout.addView(imageView);

        // Create title
        TextView title = new TextView(this);
        title.setText(randomClip._title);
        title.setTextColor(Color.BLACK);
        title.setPadding(100,10, 0, 0);
        title.setTextSize(18);
        linearLayout.addView(title);

        // Create streamer name
        TextView streamer = new TextView(this);
        streamer.setText(randomClip._broadcaster_name);
        streamer.setTextColor(Color.BLACK);
        streamer.setPadding(100,0, 0, 0);
        streamer.setTextSize(14);
        linearLayout.addView(streamer);

        // Create views
        TextView views = new TextView(this);
        views.setText((NumberFormat.getInstance().format(randomClip._views)) + " views");
        views.setTextColor(Color.BLACK);
        views.setPadding(100,0, 0, 50);
        views.setTextSize(14);
        linearLayout.addView(views);
    }
}