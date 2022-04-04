package com.example.shaqrastudentscontact.models;

public class HonorStudentQuestion {

    int id;
    int studentId;
    String studentName;
    int honorId;
    String title;
    String content;
    String answer;
    String date;

    public HonorStudentQuestion(int id, int studentId, String studentName, int honorId, String title, String content, String answer, String date) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.honorId = honorId;
        this.title = title;
        this.content = content;
        this.answer = answer;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getHonorId() {
        return honorId;
    }

    public void setHonorId(int honorId) {
        this.honorId = honorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
