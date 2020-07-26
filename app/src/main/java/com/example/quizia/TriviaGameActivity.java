package com.example.quizia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/*
    Author: Shivam Sood
    Date: 2020-07-25
    Description: Handles user responses and allows the user to navigate between questions.
 */

public class TriviaGameActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "com.example.android.quizia.SCORE";
    private Button mBtnOptionOne, mBtnOptionTwo, mBtnOptionThree, mBtnOptionFour, mBtnNext;
    private QuestionLoader mLoader;
    private int mNumCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Loads activity layout
        setContentView(R.layout.activity_trivia_game);

        // Get element references
        mBtnOptionOne = findViewById(R.id.btn_option_one);
        mBtnOptionTwo = findViewById(R.id.btn_option_two);
        mBtnOptionThree = findViewById(R.id.btn_option_three);
        mBtnOptionFour = findViewById(R.id.btn_option_four);
        mBtnNext = findViewById(R.id.btn_next);

        // Load questions into questions queue
        mLoader = new QuestionLoader(getApplication(), findViewById(android.R.id.content));
        mLoader.disableSelection();
        mBtnNext.setVisibility(View.GONE);

        // Set number of correct answers to 0
        mNumCorrect = 0;

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If game is over launch game over activity (send user score)
                if (mLoader.isGameOver()) {
                    Intent gameOverIntent = new Intent(TriviaGameActivity.this,
                            GameOverActivity.class);
                    gameOverIntent.putExtra(EXTRA_SCORE, mNumCorrect);
                    startActivity(gameOverIntent);
                } else {
                    mLoader.setContent();
                    // reset buttons
                    mBtnNext.setVisibility(View.GONE);
                    mBtnOptionOne.setBackground(getDrawable(R.drawable.white_button));
                    mBtnOptionTwo.setBackground(getDrawable(R.drawable.white_button));
                    mBtnOptionThree.setBackground(getDrawable(R.drawable.white_button));
                    mBtnOptionFour.setBackground(getDrawable(R.drawable.white_button));
                }
            }
        });
    }

    // Check if the button clicked was correct
    public void onAnswerSelected(View v) {
        switch (v.getId()) {
            case R.id.btn_option_one:
                checkAns(mBtnOptionOne);
                break;
            case R.id.btn_option_two:
                checkAns(mBtnOptionTwo);
                break;
            case R.id.btn_option_three:
                checkAns(mBtnOptionThree);
                break;
            case R.id.btn_option_four:
                checkAns(mBtnOptionFour);
                break;
        }
        // Allow user to move onto the next question
        mLoader.disableSelection();
        mBtnNext.setVisibility(View.VISIBLE);
    }

    // Change button colors depending on if user was correct or not
    public void checkAns(Button buttonSelected) {
        if (mLoader.getCorrectBtn() == buttonSelected) {
            mNumCorrect++;
            buttonSelected.setBackground(getDrawable(R.drawable.green_button));
        } else {
            mLoader.getCorrectBtn().setBackground(getDrawable(R.drawable.green_button));
            buttonSelected.setBackground(getDrawable(R.drawable.wrong_button));
        }
    }

}