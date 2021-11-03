package com.example.twitchclipsfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class Clip_Watching extends AppCompatActivity {
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
                // System.out.print("Test button clicked!\n");

                try {
                    getClipTitle();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getClipTitle() throws IOException {
        // System.out.print("getClipTitle starting\n");
//        new twitchRequest().execute();

//        String cURL_command = "curl -X GET 'https://api.twitch.tv/helix/streams' -H 'Authorization: Bearer hlpj3tm0717ydxtihrn78kwywsup6i' -H 'Client-Id: k61784vtuptn3u2aes0xf6ss1e2nf5'";
//        System.out.print(cURL_command + "\n");
//
//        ProcessBuilder process = new ProcessBuilder(cURL_command);
//        Process p;
//
//        p = process.start();
    }

    private class twitchRequest extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {

        }

        protected Void doInBackground(Void... params) {
            HttpURLConnection connection = null;

            try {
                // URL url = new URL("https://api.twitch.tv/helix/streams");
                URL url = new URL("https://www.metaweather.com/api/location/search/?query=london");

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                // connection.setRequestProperty("Authorization", "Bearer hlpj3tm0717ydxtihrn78kwywsup6i");
                // connection.setRequestProperty("Client-Id", "k61784vtuptn3u2aes0xf6ss1e2nf5'");

                 connection.connect();

                int responseCode = connection.getResponseCode();
            } catch(Exception e) {
                System.out.print("getClipTitle error\n");
                e.printStackTrace();
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        protected void onPostExecute(Void result) {

        }
    }
}