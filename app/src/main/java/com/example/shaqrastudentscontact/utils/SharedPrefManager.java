package com.example.shaqrastudentscontact.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;

import com.example.shaqrastudentscontact.models.Professor;
import com.example.shaqrastudentscontact.models.Student;


public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "generalFile";

    //student registration/ login
    private static final String KEY_ID = "keyid";
    private static final String KEY_NAME = "keyname";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_STUDENT_TYPE = "userType";

    //professor registration/ login
    private static final String KEY_PROFESSOR_DEPT_ID = "keyprofessor_d_id";
    private static final String KEY_PROFESSOR_SPECIALIZATION = "keyspecialization";

    //control flow
    private static final String KEY_SELECTED_DEPT_ID = "key_dept_id";



    private static SharedPrefManager mInstance;
    private static Context context;

    public SharedPrefManager(Context context) {
        SharedPrefManager.context = context;
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will store the student data in shared preferences
    public void studentLogin(Student student) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, student.getId());
        editor.putString(KEY_NAME, student.getName());
        editor.putString(KEY_EMAIL, student.getEmail());
        editor.putInt(KEY_STUDENT_TYPE, student.getType());

        editor.apply();
    }

    //this method will store the professor data in shared preferences
    public void professorLogin(Professor professor) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, professor.getId());
        editor.putString(KEY_NAME, professor.getName());
        editor.putString(KEY_EMAIL, professor.getEmail());
        editor.putInt(KEY_PROFESSOR_DEPT_ID, professor.getDept_id());
        editor.putString(KEY_PROFESSOR_SPECIALIZATION, professor.getSpecialization());

        editor.apply();
    }

    //this method will check whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ID, -1) != -1;
    }

    public void setStudentType(int type){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_STUDENT_TYPE, type);
        editor.apply();
    }

    public int getStudentType(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_STUDENT_TYPE, -1);
    }

    public void setSelectedDept(int deptId){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_SELECTED_DEPT_ID, deptId);
        editor.apply();
    }

    public int getSelectedDept(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_SELECTED_DEPT_ID, -1);
    }


    //this method will give the logged in user id
    public int getUserId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ID, -1);
    }



    //this method will give the logged in user
    public Professor getProfessorData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Professor(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getInt(KEY_PROFESSOR_DEPT_ID, -1),
                sharedPreferences.getString(KEY_PROFESSOR_SPECIALIZATION, null),
                sharedPreferences.getString(KEY_EMAIL, null)
                );
    }


    //this method will give the logged in user
    public Student getStudentData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Student(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getInt(KEY_STUDENT_TYPE, -1)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
    }





}