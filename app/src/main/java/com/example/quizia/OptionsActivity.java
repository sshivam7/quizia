package com.example.quizia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

/*
    Author: Shivam Sood
    Date: 2020-07-24
    Description: The options activity allows the user to customize trivia questions based
    on difficulty and category. User preferences are stored to be accessed later. (Trivia
    questions are retrieved from the Open Trivia Database)
 */

public class OptionsActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private String mDifficulty;
    private int mCategory;
    private Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Loads activity layout
        setContentView(R.layout.activity_options);

        // Create spinner object for trivia categories selection
        createSpinner();

        // Get done button reference and save the data selected by the user before returning
        // to the main activity
        Button mBtnDone = findViewById(R.id.btn_done);

        // Load saved preferences
        getPreferences();

        mBtnDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Store user selected data using the SharedPreferences API
                SharedPreferences sharedPref = getApplication()
                        .getSharedPreferences(getString(R.string.shared_preferences_key),
                                MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.difficulty_key), mDifficulty);
                editor.putInt(getString(R.string.category_key), mCategory);
                editor.apply();

                // Return to main activity
                Intent returnIntent = new Intent(OptionsActivity.this, MainActivity.class);
                startActivity(returnIntent);
            }
        });

    }

    // Method to create and initialize spinner object
    public void createSpinner() {
        // Create and initialize the spinner object to select trivia categories
        categorySpinner = findViewById(R.id.spinner_category);
        if (categorySpinner != null) {
            categorySpinner.setOnItemSelectedListener(this);
        }

        // Create ArrayAdapter using the string array and default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, R.layout.spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // apply the adapter to the spinner
        if (categorySpinner != null) {
            categorySpinner.setAdapter(adapter);
        }
    }

    // Gets categories from spinner selection. Numbers correspond with the categories found on
    // the open trivia database website (https://opentdb.com/api_config.php)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                // -1 to represent that category does not need to be included in the GET request
                mCategory = -1;
                break;
            case 1:
                mCategory = 9;
                break;
            case 2:
                mCategory = 10;
                break;
            case 3:
                mCategory = 11;
                break;
            case 4:
                mCategory = 12;
                break;
            case 5:
                mCategory = 14;
                break;
            case 6:
                mCategory = 15;
                break;
            case 7:
                mCategory = 16;
                break;
            case 8:
                mCategory = 21;
                break;
            case 9:
                mCategory = 23;
                break;
            case 10:
                mCategory = 25;
                break;
            case 11:
                mCategory = 29;
                break;
            case 12:
                mCategory = 30;
                break;
            default:
                mCategory = -1;
                Log.d(getString(R.string.error_tag), "Valid Category not selected");
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do Nothing
        // Category is set to "Any Category" by default
    }

    // Method to handle radio button selection for difficulty
    public void onRadioButtonClicked(View view) {
        // Is the radio button selected
        boolean checked = ((RadioButton) view).isChecked();

        // Check radio button was selected
        switch (view.getId()) {
            case R.id.r_btn_easy:
                if (checked) mDifficulty = "easy";
                break;
            case R.id.r_btn_medium:
                if (checked) mDifficulty = "medium";
                break;
            case R.id.r_btn_hard:
                if (checked) mDifficulty = "hard";
                break;
            default:
                // Do nothing
                Log.d(getString(R.string.error_tag), "Radio Button not selected");
        }
    }

    // returns position of spinner category when given a numeric category value (see Open Trivia DB)
    public int getSpinnerSelection(int value) {
        switch (value) {
            // See Open Trivia DB for value pairs
            case -1:
                return 0;
            case 9:
                return 1;
            case 10:
                return 2;
            case 11:
                return 3;
            case 12:
                return 4;
            case 14:
                return 5;
            case 15:
                return 6;
            case 16:
                return 7;
            case 21:
                return 8;
            case 23:
                return 9;
            case 25:
                return 10;
            case 29:
                return 11;
            case 30:
                return 12;
        }
        return 0; // Default case
    }

    // get stored preferences and update options display
    public void getPreferences() {
        // Get game options stored in shared preferences and update buttons
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.shared_preferences_key), MODE_PRIVATE);

        String difficulty = sharedPref.getString(getString(R.string.difficulty_key),
                "easy");
        int category = sharedPref.getInt(getString(R.string.category_key), -1);

        // Update radio buttons with stored value
        RadioGroup group = findViewById(R.id.radioGroup);

        if (difficulty.equals("easy")) {
            group.check(R.id.r_btn_easy);
        } else if (difficulty.equals("medium")) {
            group.check(R.id.r_btn_medium);
        } else {
            group.check(R.id.r_btn_hard);
        }

        // Update spinner with stored value
        int spinnerPos = getSpinnerSelection(category);
        categorySpinner.setSelection(spinnerPos);
    }
}