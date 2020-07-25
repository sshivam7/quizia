package com.example.quizia;

/*
   Author: Shivam Sood
   Date: 2020-07-24
   Description: QuizQuestion object to hold information of a given question
 */

public class QuizQuestion {

    private String mQuestion, mCorrectAnswer;
    private String[] mIncorrectAnswer;

    public QuizQuestion(String question, String mCorrectAnswer, String[] incorrectAnswer) {
        this.mQuestion = question;
        this.mCorrectAnswer = mCorrectAnswer;
        this.mIncorrectAnswer = incorrectAnswer;
    }

    // getters for variables
    public String getQuestion() {
        return mQuestion;
    }

    public String getCorrectAnswer() {
        return mCorrectAnswer;
    }

    public String[] getIncorrectAnswer() {
        return mIncorrectAnswer;
    }
}
