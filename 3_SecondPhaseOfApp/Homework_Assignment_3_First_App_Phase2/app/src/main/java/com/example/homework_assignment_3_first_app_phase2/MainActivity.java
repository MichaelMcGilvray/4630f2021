package com.example.homework_assignment_3_first_app_phase2;


import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the the navigation bar using it's ID
        BottomNavigationView nav_View = findViewById(R.id.bottomNavigationView);
        NavController nav_Controller = Navigation.findNavController(this, R.id.fragmentContainerView);

        AppBarConfiguration activity_titles = new AppBarConfiguration.Builder(
                R.id.aboutMe, R.id.weather, R.id.funFacts, R.id.investments, R.id.other).build();

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        // Join and setup nav view and nav controller
        NavigationUI.setupWithNavController(nav_View, nav_Controller);
//        NavigationUI.setupWithNavController(toolbar, nav_Controller, activity_titles);

    }
}