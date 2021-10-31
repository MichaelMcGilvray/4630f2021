package com.example.homework_assignment_3_first_app_phase2;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class FunFacts extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FunFacts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Investments.
     */
    public static FunFacts newInstance(String param1, String param2) {
        FunFacts fragment = new FunFacts();
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
        View view = inflater.inflate(R.layout.fragment_fun_facts, container, false);
        onLearnMoreButtonClick(view);

        // Inflate the layout for this fragment
        return view;
    }


    private void onLearnMoreButtonClick(View view) {
        String[] about_me_facts = {
                "I'm a computer science major.",
                "I'm a senior at UML.",
                "I'm 21 years old.",
                "I have one younger brother.",
                "I worked on at an apple orchard for 4 years.",
                "I worked in IT here at UML over the summer.",
                "This app is the second time I've used Java.",
                "I will graduate in the Spring of 2022",
                "Most of my family lives in Kentucky"
        };

        TextView learnMoreText = view.findViewById(R.id.learnMoreText);
        Button learnMoreButton = view.findViewById(R.id.learnMoreButton);

        learnMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                learnMoreText.setText(about_me_facts[(int)(Math.random() * about_me_facts.length)]);
            }
        });
    }
}

