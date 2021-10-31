package com.example.homework_assignment_3_first_app_phase2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Weather#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Weather extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Weather() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Weather.
     */
    public static Weather newInstance(String param1, String param2) {
        Weather fragment = new Weather();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        Button bornButton = view.findViewById(R.id.weather_bornButton);
        Button liveButton = view.findViewById(R.id.weather_liveButton);
        Button likeToLiveButton = view.findViewById(R.id.weather_likeToLiveButton);
        Button brotherLivesButton = view.findViewById(R.id.weather_brotherLivesButton);
        Button whereIAmNowButton = view.findViewById(R.id.weather_whereIAmNowButton);

        bornButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL("https://weather.com/weather/tenday/l/Framingham+MA?canonicalCityId=7ea343c8202f6d7238324b57f3c6dcbcda17c17da79582deb3fb458458c839c8");
            }
        });

        liveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL("https://weather.com/weather/tenday/l/6aaa2ca3833ec3974a5229540ae7a386f55fc4a5b812d61097e29514408b9b4e");
            }
        });

        likeToLiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL("https://weather.com/weather/tenday/l/New+York+City+NY?canonicalCityId=a701ee19c4ab71bbbe2f6ba2fe8c250913883e5ae9b8eee8b54f8efbdb3eec03");
            }
        });

        brotherLivesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL("https://weather.com/weather/tenday/l/Stow+MA?canonicalCityId=a3d29694a240e954eeb0f2202b01b721cb4c8665fd90ae4de4cbc45275c50935");
            }
        });

        whereIAmNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL("https://www.weatherbug.com/weather-forecast/10-day-weather/");
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void openURL(String url) {
        Intent openNewBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(openNewBrowser);
    }
}