package com.example.shaqrastudentscontact.models;

import java.util.ArrayList;

public class Reply {
    private int id;
    private String studentName;
    private String reply;
    private String date;

    public Reply(int id, String studentName, String reply, String date) {
        this.id = id;
        this.studentName = studentName;
        this.reply = reply;
        this.date = date;
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

    public String getReply() {
        return reply;
    }


}
