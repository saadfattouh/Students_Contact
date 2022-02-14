package com.example.shaqrastudentscontact.models;

import java.util.ArrayList;

public class Question {
    private int id;
    private String question;
    private ArrayList<String> answers;
    private boolean common;

    public Question(int id, String question, ArrayList<String> answers, boolean common) {
        this.id = id;
        this.question = question;
        this.answers = answers;
        this.common = common;
    }


}
