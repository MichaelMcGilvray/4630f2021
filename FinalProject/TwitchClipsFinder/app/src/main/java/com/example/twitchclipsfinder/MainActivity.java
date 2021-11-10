package com.example.twitchclipsfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private Button watchAClipButton;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Search button
        // Will generate a list of clips based on the filters
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    generateClips();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void openClipWatchingActivity() {
        Intent intent = new Intent(this, Clip_Watching.class);
        startActivity(intent);
    }

    public void generateClips() throws InterruptedException, ExecutionException, JSONException, IOException {
        LinearLayout linearLayout = findViewById(R.id.clipsList);

        // Temporarily, I will just generate 10 clips
        final int totalClips = 10;

        Clip[] arrOfClips = new clipGenerator().getClips("743");

        for (int i = 0; i < totalClips; i++) {
            createClipView(arrOfClips[i]);
        }
    }

    // This function will use the Twitch API and relevant search options to get each clips information
    public Clip getClipInformation() {
        // For now, I will just return a sample clip
        Clip ret = new Clip("https://clips-media-assets2.twitch.tv/AT-cm%7C1063432361-preview-480x272.jpg",
                "https://clips.twitch.tv/embed?clip=IncredulousMildSwordJonCarnage-GyOI5-ArzBOABbuS",
                "Mr. K makes FOX 32 Chicago News", 100000, "Ramee");

        return ret;
    }

    public void createClipView(Clip clip) {
        LinearLayout linearLayout = findViewById(R.id.clipsList);

        ImageView imageView = new ImageView(this);

        // Use Picasso to load image into ImageView using a URL
        Picasso.get().load(clip._thumbnail).into(imageView);

        // LinearLayout.LayoutParams imageParameters = new LinearLayout.LayoutParams(480, 720);
        // imageView.setLayoutParams(imageParameters);

        // Make the ImageView clickable and open the clip watching screen
        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openClipWatchingActivity();
            }
        });

        // Add the imageView to the linearLayout (scrolling section in app)
        linearLayout.addView(imageView);

        // Create title
        TextView textView = new TextView(this);
        textView.setText(clip._title);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        linearLayout.addView(textView);
    }
}