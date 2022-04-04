package com.example.shaqrastudentscontact.models;

public class ProfessorQuestion {

    int id;
    int studentID;
    String studentName;
    String title;
    String content;
    String answer;
    String date;


//    public ProfessorQuestion(int id, int studentID, int professorId, String content, String answer) {
//        this.id = id;
//        this.studentID = studentID;
//        this.professorId = professorId;
//        this.content = content;
//        this.answer = answer;
//    }


    public ProfessorQuestion(int id, int studentID, String studentName, String title, String content, String answer, String date) {
        this.id = id;
        this.studentID = studentID;
        this.studentName = studentName;
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

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
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

    public String getStudentName() {
        return studentName;
    }

    public String getDate() {
        return date;
    }
}
