package com.example.shaqrastudentscontact.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.shaqrastudentscontact.models.Professor;
import com.example.shaqrastudentscontact.models.Student;
import com.example.shaqrastudentscontact.utils.Constants;
import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.professor.ProfessorMain;
import com.example.shaqrastudentscontact.utils.SharedPrefManager;
import com.example.shaqrastudentscontact.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;


public class Login extends AppCompatActivity {

    private Button mLoginBtn, mToRegisterBtn;
    private EditText mEmailET;
    private EditText mPassET;
    private ProgressDialog pDialog;

    int selectedUserType;
    int userTypeFromResponse;
    AlertDialog verificationDialog;

    RadioGroup mAccountTypeSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailET = findViewById(R.id.email);
        mPassET =  findViewById(R.id.password);
        mLoginBtn =  findViewById(R.id.btnLogin);
        mToRegisterBtn =  findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);

        mToRegisterBtn.setOnClickListener(v->{
            startActivity(new Intent(this, Register.class));
            finish();
        });

        mAccountTypeSelector = findViewById(R.id.type_selector);
        mAccountTypeSelector.check(R.id.student);
        selectedUserType = Constants.USER_TYPE_STUDENT;
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

        mLoginBtn.setOnClickListener(view -> {
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
                            loginUser(email, password);
                        }
                    }else if(selectedUserType == Constants.USER_TYPE_STUDENT){
                        if(!email.matches("^[\\w]+[\\w.%+-]*@std\\.su\\.edu\\.sa$")){
                            Toast.makeText(Login.this, getResources().getString(R.string.please_provide_a_valid_student_email), Toast.LENGTH_SHORT).show();
                            mLoginBtn.setEnabled(true);
                            return;
                        }else {
                            loginUser(email, password);
                        }
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.email_and_password_required), Toast.LENGTH_LONG)
                        .show();
            }
        });
    }
    private void loginUser(String email, String password) {
        pDialog.show();
        String url = Urls.LOGIN_URL;
        AndroidNetworking.post(url)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String userFounded = "User founded";
                            String userNotVerified = "User Not Verified";
                            //if no error in response
                            if (message.toLowerCase().contains(userFounded.toLowerCase()) || message.toLowerCase().contains(userNotVerified.toLowerCase())) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.loggedin_success), Toast.LENGTH_SHORT).show();
                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("data");
                                if(userJson.has("type")){
                                    userTypeFromResponse = userJson.getInt("type");
                                }else{
                                    userTypeFromResponse = Constants.USER_TYPE_PROFESSOR;
                                }
                                Log.e("uType", userTypeFromResponse +"");
                                if(userTypeFromResponse == Constants.USER_TYPE_STUDENT || userTypeFromResponse == Constants.STUDENT_TYPE_HONOR){
                                            SharedPrefManager.getInstance(getApplicationContext()).studentLogin(new Student(
                                                    Integer.parseInt(userJson.getString("id")),
                                                    userJson.getString("name"),
                                                    userJson.getString("email"),
                                                    userJson.getInt("type"))
                                            );
                                }else{
                                            SharedPrefManager.getInstance(getApplicationContext()).professorLogin(new Professor(
                                                    Integer.parseInt(userJson.getString("id")),
                                                    userJson.getString("name"),
                                                    "dept",
                                                    userJson.getString("specialization"),
                                                    userJson.getString("email")));
                                }
                                goToUser(userTypeFromResponse);
                            }
                            else{
                                Toast.makeText(Login.this, getResources().getString(R.string.wrong_pass), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                            mLoginBtn.setEnabled(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("regprof catch", e.getMessage());
                            pDialog.dismiss();
                            mLoginBtn.setEnabled(true);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mLoginBtn.setEnabled(true);
                        Log.e("regiproferror", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(Login.this, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("email")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("email").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("password")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("password").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public void goToUser(int userType){
        if(userType == Constants.USER_TYPE_STUDENT || userType == Constants.STUDENT_TYPE_HONOR){
            Intent i = new Intent(Login.this, ChooseDepartmentActivity.class);
            i.putExtra(Constants.FROM, Constants.USER_TYPE_STUDENT);
            startActivity(i);
        }else{
            startActivity(new Intent(Login.this, ProfessorMain.class));
        }
        finish();
    }


}