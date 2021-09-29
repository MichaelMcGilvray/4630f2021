package com.example.homework_assignment_2_first_app;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onAboutMeButtonClick();
    }

    private void onAboutMeButtonClick() {
        String[] about_me_facts = {
                "I'm a computer science major.",
                "I'm a senior at UML.",
                "I'm 21 years old.",
                "I have one younger brother.",
                "I worked on at an apple orchard for 4 years.",
                "I worked in IT here at UML over the summer.",
                "This app is the first time I've used Java."
        };
        TextView aboutMeID = findViewById(R.id.aboutMeText);
        Button buttonID = findViewById(R.id.button);

        buttonID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutMeID.setText(about_me_facts[(int)(Math.random() * about_me_facts.length)]);
                int rand = (int)(Math.random() * 3);
                if (rand == 0) {
                    aboutMeID.setTextColor(Color.RED);
                } else if (rand == 1) {
                    aboutMeID.setTextColor(Color.BLUE);
                } else {
                    aboutMeID.setTextColor(Color.GREEN);
                }
            }
        });
    }
}