package com.example.twitchclipsfinder;


import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class twitchRequest extends AsyncTask<Void, Void, Void> {
    URL _url;
    String App_Access_Token = "hlpj3tm0717ydxtihrn78kwywsup6i";
    String Client_ID = "k61784vtuptn3u2aes0xf6ss1e2nf5";
    String _jsonString = "";

    twitchRequest(String url) {
        try {
            _url = new URL (url);
            HttpURLConnection connection = null;
        } catch (MalformedURLException e) {
            System.out.print("Error: Couldn't create twitch request URL\n");
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected Void doInBackground(Void... params) {
        HttpURLConnection connection = null;

        try {
            // Setup connection so we get a valid response
            connection = (HttpURLConnection) _url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + App_Access_Token);
            connection.setRequestProperty("Client-Id", Client_ID);

            connection.connect();

            int responseCode = connection.getResponseCode();
            // System.out.print("Response code = " + responseCode + "\n");

            // String responseMessage = connection.getResponseMessage();
            // System.out.print("Response Message = " + responseMessage + "\n");


            // This means we got a valid response
            if ((responseCode == 200) || (responseCode == 201)) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line = reader.readLine();

                while (line != null) {
                    stringBuilder.append(line);
                    line = reader.readLine();
                }

                reader.close();

                _jsonString = stringBuilder.toString();
            } else {
                System.out.print("Error: Couldn't get a valid response from Twitch API\n");
                System.out.print("Response code = " + responseCode + "\n");
            }



        } catch(Exception e) {
            System.out.print("twitchRequest error\n");
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
}
