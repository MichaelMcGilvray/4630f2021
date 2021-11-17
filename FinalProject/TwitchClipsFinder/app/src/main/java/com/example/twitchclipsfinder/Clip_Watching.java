package com.example.twitchclipsfinder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;

import org.json.*;


public class Clip_Watching extends AppCompatActivity {
    private TextView title;
    private TextView broadcaster;
    private TextView views;
    private Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_watching);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Enable up button

        testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                try {
                    Clip clips[] = new clipGenerator().getClips("743");
                    for (int i = 0; i < clips.length; i++) {
                        clips[i].print();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                 */

                // Temporarily, just open the clip in a browser
                Intent intent = getIntent();
                Clip clip = (Clip) intent.getSerializableExtra("clip");

                Uri uri = Uri.parse(clip._url);
                Intent openClipIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(openClipIntent);
            }
        });

        setupClip();

        // Setup the video view using embed url
        VideoView videoView = findViewById(R.id.videoView);
        String url = "https://clips.twitch.tv/embed?clip=SleepyGracefulChickpeaDBstyle";
        String url2 = "https://clips.twitch.tv/SleepyGracefulChickpeaDBstyle";
//        Uri uri = Uri.parse(url);
//        videoView.setVideoURI(uri);

//        videoView.setVideoPath(url);

//        videoView.start();
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
    }
}