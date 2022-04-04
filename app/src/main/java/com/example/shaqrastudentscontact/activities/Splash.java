package com.example.shaqrastudentscontact.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.professor.ProfessorMain;
import com.example.shaqrastudentscontact.student.StudentMain;
import com.example.shaqrastudentscontact.utils.Constants;
import com.example.shaqrastudentscontact.utils.SharedPrefManager;


public class Splash extends AppCompatActivity{

    public static final int TIME_TO_START = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                if(SharedPrefManager.getInstance(Splash.this).isLoggedIn()){
                    if(SharedPrefManager.getInstance(Splash.this).getUserType() == Constants.USER_TYPE_STUDENT){
                        startActivity(new Intent(Splash.this, StudentMain.class));
                    }else{
                        startActivity(new Intent(Splash.this, ProfessorMain.class));
                    }
                }else{
                    startActivity(new Intent(Splash.this, Login.class));
                }
                finish();
            }
        }, TIME_TO_START);

    }


}