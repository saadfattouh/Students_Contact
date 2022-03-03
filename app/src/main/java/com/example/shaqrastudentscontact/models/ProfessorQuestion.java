package com.example.shaqrastudentscontact.models;

public class ProfessorQuestion {

    int id;
    int studentID;
    String studentName;
    int professorId;
    String title;
    String details;
    String answer;

    public ProfessorQuestion(int id, int studentID, String studentName, int professorId, String title, String details, String answer) {
        this.id = id;
        this.studentID = studentID;
        this.studentName = studentName;
        this.professorId = professorId;
        this.title = title;
        this.details = details;
        this.answer = answer;
    }
    public ProfessorQuestion(int id, int studentID, int professorId, String title, String details, String answer) {
        this.id = id;
        this.studentID = studentID;
        this.professorId = professorId;
        this.title = title;
        this.details = details;
        this.answer = answer;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
