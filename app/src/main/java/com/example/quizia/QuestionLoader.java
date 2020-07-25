package com.example.quizia;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/*
    Author: Shivam Sood
    Date: 2020-07-24
    Description: Class to handle generating a specific URL, used to obtain data from the Open Trivia
    API, and load the data into a queue
 */

public class QuestionLoader {

    // url used to fetch data from the Open Trivia Database
    private String mFetchUrl;
    // Volley request queue
    private RequestQueue mQueue;
    // Question queue
    private Queue<QuizQuestion> mQuestionQueue = new LinkedList<>();
    // has the content been loaded
    private Context mContext;

    private TextView mTxtQuestion;
    private Button mBtnOptionOne, mBtnOptionTwo, mBtnOptionThree, mBtnOptionFour;
    private Button mCorrectBtn;
    private boolean mGameOver = false;

    // class constructor
    public QuestionLoader(Context context, View view) {
        this.mContext = context;

        mTxtQuestion = view.findViewById(R.id.txt_question);
        mBtnOptionOne = view.findViewById(R.id.btn_option_one);
        mBtnOptionTwo = view.findViewById(R.id.btn_option_two);
        mBtnOptionThree = view.findViewById(R.id.btn_option_three);
        mBtnOptionFour = view.findViewById(R.id.btn_option_four);

        // Create volley request queue and fetch data
        mQueue = Volley.newRequestQueue(context);
        generateSessionToken();
    }

    // Returns correct btn
    public Button getCorrectBtn() {
        return mCorrectBtn;
    }

    // Return if game is over
    public boolean isGameOver() {
        return mGameOver;
    }


    // Method to build final url used to connect to the Open Trivia API
    public String buildUrl(String sessionToken) {
        // Base url used to connect to the API
        String url = "https://opentdb.com/api.php?amount=7&token=" + sessionToken;

        // Get game options stored in shared preferences
        SharedPreferences sharedPref = mContext.getSharedPreferences(
                mContext.getString(R.string.shared_preferences_key), Context.MODE_PRIVATE);

        String difficulty = sharedPref.getString(mContext.getString(R.string.difficulty_key),
                "easy");
        int category = sharedPref.getInt(mContext.getString(R.string.category_key), -1);

        // If the user has selected a category add it to the url
        if (category != -1) {
            url = url + "&category=" + category;
        }

        // Add user selected difficulty to the url
        url = url + "&difficulty=" + difficulty;

        // &type=multiple indicates responses should be multiple choice
        return url + "&type=multiple";
    }

    // Method to get session token from the Open Trivia API
    // Session token is used to avoid question repetition
    public void generateSessionToken() {
        // Url to get session token
        String getTokenUrl = "https://opentdb.com/api_token.php?command=request";

        // Create a new Json Object request to get session token
        JsonObjectRequest tokenRequest = new JsonObjectRequest
                (Request.Method.GET, getTokenUrl, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    // Call build url with the session token and store it in a variable
                                    mFetchUrl = buildUrl(response.getString("token"));
                                    // Load quiz data after url has been built
                                    loadQuizData();
                                } catch (JSONException e) {
                                    // Print an error message if json object can not be parsed
                                    Log.d(mContext.getString(R.string.error_tag),
                                            "Could not find string token");
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    // Print an error if a connection cannot be established
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, "Unable to get session token",
                                Toast.LENGTH_SHORT).show();
                        Log.d(mContext.getString(R.string.error_tag), error.toString());
                    }
                });
        // Add to queue
        mQueue.add(tokenRequest);
    }

    // Method to load quiz data into the questions queue
    public void loadQuizData() {
        // Get data from the URL built earlier
        JsonObjectRequest quizRequest = new JsonObjectRequest(Request.Method.GET, mFetchUrl,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Parse results
                    JSONArray results = response.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        String question = results.getJSONObject(i).getString("question");
                        String correctAns = results.getJSONObject(i).
                                getString("correct_answer");

                        // Get incorrect answers array
                        JSONArray jsonIncorrectAns = results.getJSONObject(i).
                                getJSONArray("incorrect_answers");
                        String[] incorrectAns = new String[jsonIncorrectAns.length()];

                        for (int j = 0; j < jsonIncorrectAns.length(); j++) {
                            incorrectAns[j] = jsonIncorrectAns.getString(j);
                        }

                        // Create a new quiz question from the data obtained above and add it to
                        // the questions queue
                        mQuestionQueue.offer(new QuizQuestion(question, correctAns, incorrectAns));
                    }
                    // Once the queue has been filled set content
                    setContent();
                } catch (JSONException e) {
                    // If unable to parse JSON object display an error message
                    Log.d(mContext.getString(R.string.error_tag),
                            "Could not load questions");
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            // Display an error message in case of a network error
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "Unable to load quiz data",
                        Toast.LENGTH_SHORT).show();
                Log.d(mContext.getString(R.string.error_tag), error.toString());
            }
        });
        // Add request to volley queue
        mQueue.add(quizRequest);
    }

    public void setContent() {
        QuizQuestion newQuestion = mQuestionQueue.poll();

        // Check if queue is empty
        if (newQuestion != null) {
            mTxtQuestion.setText(Html.fromHtml(newQuestion.getQuestion()));

            // Randomly assign the correct answer to a button
            int randomNum = new Random().nextInt(4) + 1; // 1 to 4

            // Store correct button in a variable
            switch (randomNum) {
                case 1:
                    mCorrectBtn = mBtnOptionOne;
                    break;
                case 2:
                    mCorrectBtn = mBtnOptionTwo;
                    break;
                case 3:
                    mCorrectBtn = mBtnOptionThree;
                    break;
                case 4:
                    mCorrectBtn = mBtnOptionFour;
                    break;
            }
            // fill correct answer
            mCorrectBtn.setText(Html.fromHtml(newQuestion.getCorrectAnswer()));

            // Store incorrect answers
            String[] incorrectAns = newQuestion.getIncorrectAnswer();

            // Fill in incorrect answers
            int counter = 0;
            for (int i = 1; i <= 4; i++) {
                // if current button is already filled with correct answer skip it
                if (i == randomNum) {
                    continue;
                }
                setBtnText(i, Html.fromHtml(incorrectAns[counter]));
                counter++;
            }
        } else {
            // if queue is empty the set game over to true
            mGameOver = true;
        }

        // Once data has loaded make buttons clickable
        enableSelection();
    }

    // get button from number and set text
    public void setBtnText(int num, Spanned text) {
        switch (num) {
            case 1:
                mBtnOptionOne.setText(text);
                break;
            case 2:
                mBtnOptionTwo.setText(text);
                break;
            case 3:
                mBtnOptionThree.setText(text);
                break;
            case 4:
                mBtnOptionFour.setText(text);
                break;
        }
    }

    // Disable all input buttons
    public void disableSelection() {
        mBtnOptionOne.setEnabled(false);
        mBtnOptionTwo.setEnabled(false);
        mBtnOptionThree.setEnabled(false);
        mBtnOptionFour.setEnabled(false);
    }

    // enable all input buttons
    public void enableSelection() {
        mBtnOptionOne.setEnabled(true);
        mBtnOptionTwo.setEnabled(true);
        mBtnOptionThree.setEnabled(true);
        mBtnOptionFour.setEnabled(true);
    }
}