package com.example.shaqrastudentscontact.models;

import java.io.Serializable;

public class Professor implements Serializable {

    private int id;
    private String name;
    private String deptName;
    private String specialization;
    private String email;
    private String startFreeTime;
    private String endFreeTime;

    public Professor(int id, String name, String deptName, String specialization, String email) {
        this.id = id;
        this.name = name;
        this.deptName = deptName;
        this.specialization = specialization;
        this.email = email;
    }

    public Professor(int id, String name, String deptName, String specialization, String email, String startFreeTime, String endFreeTime) {
        this.id = id;
        this.name = name;
        this.deptName = deptName;
        this.specialization = specialization;
        this.email = email;
        this.startFreeTime = startFreeTime;
        this.endFreeTime = endFreeTime;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getStartFreeTime() {
        return startFreeTime;
    }

    public void setStartFreeTime(String startFreeTime) {
        this.startFreeTime = startFreeTime;
    }

    public String getEndFreeTime() {
        return endFreeTime;
    }

    public void setEndFreeTime(String endFreeTime) {
        this.endFreeTime = endFreeTime;
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
