package com.example.shaqrastudentscontact.models;

public class Professor {

    private int id;
    private String name;
    private String deptName;
    private String specialization;
    private String email;

    public Professor(int id, String name, String deptName, String specialization, String email) {
        this.id = id;
        this.name = name;
        this.deptName = deptName;
        this.specialization = specialization;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
