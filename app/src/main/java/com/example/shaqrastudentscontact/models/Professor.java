package com.example.shaqrastudentscontact.models;

public class Professor {

    private int id;
    private String name;
    private int dept_id;
    private String specialization;
    private String email;

    public Professor(int id, String name, int dept_id, String specialization, String email) {
        this.id = id;
        this.name = name;
        this.dept_id = dept_id;
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

    public int getDept_id() {
        return dept_id;
    }

    public void setDept_id(int dept_id) {
        this.dept_id = dept_id;
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
