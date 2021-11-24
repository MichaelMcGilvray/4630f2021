package com.example.twitchclipsfinder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
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
    private Button fullscreenButton;
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
                System.out.print("Test button clicked!\n");
            }
        });

        fullscreenButton = findViewById(R.id.fullscreenButton);
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("Fullscreen clicked!\n");
            }
        });

        setupClip();
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