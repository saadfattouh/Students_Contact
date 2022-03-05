package com.example.shaqrastudentscontact.models;

public class HonorStudentQuestion {

    int id;
    int askerId;
    String studentName;
    int honorId;
    String content;
    String answer;
    String date;

    public HonorStudentQuestion(int id, int askerId, String studentName, int honorId, String content, String answer, String date) {
        this.id = id;
        this.askerId = askerId;
        this.studentName = studentName;
        this.honorId = honorId;
        this.content = content;
        this.answer = answer;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAskerId() {
        return askerId;
    }

    public void setAskerId(int askerId) {
        this.askerId = askerId;
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
