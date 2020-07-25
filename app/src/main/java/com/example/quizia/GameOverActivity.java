package com.example.quizia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/*
    Author: Shivam Sood
    Date: 2020-07-25
    Description: Activity to disply user score and allow the user to play again or navigate back to
    the home screen.
 */

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        // Get references
        Button mBtnPlayAgain = findViewById(R.id.btn_play_again);
        Button mBtnReturn = findViewById(R.id.btn_return);
        TextView txtScore = findViewById(R.id.txt_score);

        // Get intent data and display user score
        Intent scoreIntent = getIntent();
        int numCorrect = scoreIntent.getIntExtra(TriviaGameActivity.EXTRA_SCORE, 0);
        String score = numCorrect + "/7";

        txtScore.setText(score);

        // Play another round of the quiz
        mBtnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playAgainIntent = new Intent(GameOverActivity.this,
                        TriviaGameActivity.class);
                startActivity(playAgainIntent);
            }
        });

        // Return to the main menu
        mBtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(GameOverActivity.this,
                        MainActivity.class);
                startActivity(returnIntent);
            }
        });
    }
}