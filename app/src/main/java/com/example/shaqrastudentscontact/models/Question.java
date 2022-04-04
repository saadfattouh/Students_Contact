package com.example.shaqrastudentscontact.models;

import java.util.ArrayList;

public class Question {
    private int id;
    private String studentName;
    private String question;
    private String date;
    private boolean common;
    //receiverId for backend

    public Question(int id, String studentName, String question, String date, boolean common) {
        this.id = id;
        this.studentName = studentName;
        this.question = question;
        this.date = date;
        this.common = common;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getQuestion() {
        return question;
    }

    public boolean isCommon() {
        return common;
    }
}
