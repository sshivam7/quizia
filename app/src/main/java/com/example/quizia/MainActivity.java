package com.example.quizia;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/*
    Author: Shivam Sood
    Date: 2020-07-24
    Description: Main activity starts on app launch and allows users to navigate to the options menu
    or start the trivia game.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Loads activity layout
        setContentView(R.layout.activity_main);

        // Test for internet and display warning if not connected
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Network Error", Toast.LENGTH_LONG).show();
        }

        Button mBtnStart = findViewById(R.id.btn_start);
        Button mBtnOptions = findViewById(R.id.btn_Options);

        // Starts the trivia game once the start button has been clicked
        mBtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Launches the TriviaGameActivity Activity
                Intent gameIntent = new Intent(MainActivity.this, TriviaGameActivity.class);
                startActivity(gameIntent);
            }
        });

        // Opens game options menu if the options button has been clicked
        mBtnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launches the options activity
                Intent optionsIntent = new Intent(MainActivity.this,
                        OptionsActivity.class);
                startActivity(optionsIntent);
            }
        });
    }

    // Check for network connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}