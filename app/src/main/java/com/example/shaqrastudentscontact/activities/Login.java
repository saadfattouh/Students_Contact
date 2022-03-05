package com.example.shaqrastudentscontact.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.shaqrastudentscontact.models.Professor;
import com.example.shaqrastudentscontact.models.Student;
import com.example.shaqrastudentscontact.student.StudentMain;
import com.example.shaqrastudentscontact.utils.Constants;
import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.professor.ProfessorMain;
import com.example.shaqrastudentscontact.student.ChooseDepartment;
import com.example.shaqrastudentscontact.utils.SharedPrefManager;
import com.example.shaqrastudentscontact.utils.Urls;
import com.example.shaqrastudentscontact.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;


public class Login extends AppCompatActivity {

    private Button mLoginBtn;
    private EditText mEmailET;
    private EditText mPassET;
    private ProgressDialog pDialog;

    RadioGroup mAccountTypeSelector;
    int selectedUserType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailET = findViewById(R.id.email);
        mPassET =  findViewById(R.id.password);
        mLoginBtn =  findViewById(R.id.btnLogin);
        mAccountTypeSelector = findViewById(R.id.type_selector);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Login button Click Event
        mLoginBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                mLoginBtn.setEnabled(false);

                //validation
                String email = mEmailET.getText().toString().trim();
                String password = mPassET.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    if(selectedUserType == -1){
                        Toast.makeText(Login.this, getResources().getString(R.string.type_missing_message), Toast.LENGTH_SHORT).show();
                        mLoginBtn.setEnabled(true);
                        return;
                    }else {
                        if(selectedUserType == Constants.USER_TYPE_PROFESSOR){
                            if(!email.contains(getResources().getString(R.string.professor_email_suffex))){
                                    Toast.makeText(Login.this, getResources().getString(R.string.please_provide_a_professor_email), Toast.LENGTH_SHORT).show();
                                    mLoginBtn.setEnabled(true);
                                    return ;
                            }else {
                                // login professor
                                loginProfessor(email, password);
                            }
                        }else if(selectedUserType == Constants.USER_TYPE_STUDENT){
                            if(!email.contains(getResources().getString(R.string.student_email_suffex))){
                                Toast.makeText(Login.this, getResources().getString(R.string.please_provide_a_valid_student_email), Toast.LENGTH_SHORT).show();
                                mLoginBtn.setEnabled(true);
                                return;
                            }else {
                                // login student
                                loginStudent(email, password);
                            }
                        }
                    }
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.email_and_password_required), Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        mAccountTypeSelector.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.student:
                    selectedUserType = Constants.USER_TYPE_STUDENT;
                    break;
                case R.id.professor:
                    selectedUserType = Constants.USER_TYPE_PROFESSOR;
                    break;
            }
        });




    }

    private void loginStudent(String email, String password) {
        startActivity(new Intent(this, ChooseDepartment.class));
        finish();

        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        String url = Urls.LOGIN_USER_URL;

        AndroidNetworking.post(url)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        pDialog.dismiss();

                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            //if no error in response
                            if (obj.getInt("status") == 1) {

                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("data");
                                Student user;
                                user = new Student(
                                        Integer.parseInt(
                                        userJson.getString("id")),
                                        userJson.getString("name"),
                                        userJson.getString("email"),
                                        Integer.parseInt(userJson.getString("type"))

                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).studentLogin(user);
                                goToUserMainActivity();
                                finish();

                                mLoginBtn.setEnabled(true);
                            } else if(obj.getInt("status") == -1){
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                mLoginBtn.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("login student", e.getMessage());

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mLoginBtn.setEnabled(true);
                        Toast.makeText(Login.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("login student", anError.getMessage());

                    }
                });


    }

    private void goToUserMainActivity() {
        startActivity(new Intent(Login.this, StudentMain.class));
        finish();
    }
    private void goToProfMainActivity() {
        startActivity(new Intent(Login.this, ProfessorMain.class));
        finish();
    }
    private void loginProfessor(String email, String password) {
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        String url = Urls.LOGIN_PROF_URL;

        AndroidNetworking.post(url)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        pDialog.dismiss();

                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            //if no error in response
                            if (obj.getInt("status") == 1) {

                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("data");
                                Professor user;
                                user = new Professor(
                                        Integer.parseInt(userJson.getString("id")),
                                        userJson.getString("name"),
                                        userJson.getString("deptName"),
                                        userJson.getString("email"),
                                        userJson.getString("specialization")
                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).professorLogin(user);
                                goToUserMainActivity();
                                finish();

                                mLoginBtn.setEnabled(true);
                            } else if(obj.getInt("status") == -1){
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                mLoginBtn.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("login prof", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mLoginBtn.setEnabled(true);
                        Toast.makeText(Login.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("login prof", anError.getMessage());

                    }
                });

    }

}