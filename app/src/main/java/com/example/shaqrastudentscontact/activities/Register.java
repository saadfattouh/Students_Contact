package com.example.shaqrastudentscontact.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.example.shaqrastudentscontact.models.Student;
import com.example.shaqrastudentscontact.utils.Constants;
import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.utils.SharedPrefManager;
import com.example.shaqrastudentscontact.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    EditText mNameET, mPassET, mEmailET, mSpecializationET;
    Button mRegisterBtn, mToLoginBtn;
    RadioGroup mAccountTypeSelector;

    int selectedUserType;
    String verificationCode;

    private ProgressDialog pDialog;
    AlertDialog verificationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mNameET = findViewById(R.id.name);
        mPassET = findViewById(R.id.password);
        mEmailET = findViewById(R.id.email);
        mSpecializationET = findViewById(R.id.specialization);
        mSpecializationET.setVisibility(View.GONE);

        mRegisterBtn = findViewById(R.id.btnRegister);
        mToLoginBtn = findViewById(R.id.btnLinkToLoginScreen);

        mAccountTypeSelector = findViewById(R.id.type_selector);
        selectedUserType = Constants.USER_TYPE_STUDENT;
        mAccountTypeSelector.check(R.id.student);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);

        mAccountTypeSelector.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.student:
                    selectedUserType = Constants.USER_TYPE_STUDENT;
                    mSpecializationET.setVisibility(View.GONE);
                    break;
                case R.id.professor:
                    mSpecializationET.setVisibility(View.VISIBLE);
                    selectedUserType = Constants.USER_TYPE_PROFESSOR;
                    break;
            }
        });

        mToLoginBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, Login.class));
            finish();
        });

        mRegisterBtn.setOnClickListener(v -> {
            if (validateUserInput()) {
                if (selectedUserType == Constants.USER_TYPE_STUDENT) {
                    register_student();
                } else {
                    Intent i = new Intent(this, ChooseDepartmentActivity.class);
                    i.putExtra(Constants.PROFESSOR_NAME, mNameET.getText().toString().trim());
                    i.putExtra(Constants.PROFESSOR_EMAIL, mEmailET.getText().toString().trim());
                    i.putExtra(Constants.PROFESSOR_PASS, mPassET.getText().toString().trim());
                    i.putExtra(Constants.PROFESSOR_SPEC, mSpecializationET.getText().toString().trim());
                    i.putExtra(Constants.FROM, selectedUserType);

                    startActivity(i);
                }
            }
        });
    }

    private void verifyEmail(String verificationCode) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.dilaog_email_verification, null);
        verificationDialog = new AlertDialog.Builder(this).create();
        verificationDialog.setView(view);
        verificationDialog.setCanceledOnTouchOutside(true);
        verificationDialog.setCancelable(false);


        EditText code = view.findViewById(R.id.code);
        Button verify = view.findViewById(R.id.btn_verify_code);
        verify.setOnClickListener(v -> {
            if (!code.getText().toString().trim().isEmpty()) {
                String codeFromUser = code.getText().toString().trim();
                if(codeFromUser.equals(verificationCode)){
                    Toast.makeText(this, getResources().getString(R.string.correct_code), Toast.LENGTH_SHORT).show();
                    SharedPrefManager.getInstance(getApplicationContext()).setVerified(true);
                    goToDepartmentsForStudent();
                }else{
                    Toast.makeText(this, getResources().getString(R.string.error_code), Toast.LENGTH_SHORT).show();
                }
            }
        });
        verificationDialog.show();
    }


    private boolean validateUserInput() {
        //first getting the values
        final String pass = mPassET.getText().toString();
        final String name = mNameET.getText().toString();
        final String email = mEmailET.getText().toString();
        final String spec = mSpecializationET.getText().toString();

        //checking if username is empty
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, getResources().getString(R.string.name_missing_message), Toast.LENGTH_SHORT).show();
            mRegisterBtn.setEnabled(true);
            return false;
        }

        //checking if email is empty
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, getResources().getString(R.string.email_missing_message), Toast.LENGTH_SHORT).show();
            mRegisterBtn.setEnabled(true);
            return false;
        }

        //checking if password is empty
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, getResources().getString(R.string.password_missing_message), Toast.LENGTH_SHORT).show();
            mRegisterBtn.setEnabled(true);
            return false;
        }
        if (selectedUserType == Constants.USER_TYPE_PROFESSOR) {
            if (TextUtils.isEmpty(spec)) {
                Toast.makeText(this, getResources().getString(R.string.spec_missing_message), Toast.LENGTH_SHORT).show();
                mRegisterBtn.setEnabled(true);
                return false;
            }
        }
        if (selectedUserType == -1) {
            Toast.makeText(this, getResources().getString(R.string.type_missing_message), Toast.LENGTH_SHORT).show();
            mRegisterBtn.setEnabled(true);
            return false;
        } else {
            if (selectedUserType == Constants.USER_TYPE_PROFESSOR) {
                if (email.contains(getResources().getString(R.string.professor_email_suffex))) {
                    Toast.makeText(this, getResources().getString(R.string.please_provide_a_professor_email), Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else if (selectedUserType == Constants.USER_TYPE_STUDENT) {
                if (email.matches("^[\\w]+[\\w.%+-]*@std\\.su\\.edu\\.sa$")) {
                    Toast.makeText(this, getResources().getString(R.string.please_provide_a_valid_student_email), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    private void register_student() {
        pDialog.show();

        mRegisterBtn.setEnabled(false);
        String url = Urls.REGISTER_STUDENT_URL;

        //first getting the values
        final String pass = mPassET.getText().toString();
        final String name = mNameET.getText().toString();
        final String email = mEmailET.getText().toString();

        AndroidNetworking.post(url)
                .addBodyParameter("name", name)
                .addBodyParameter("email", email)
                .addBodyParameter("password", pass)
                .addBodyParameter("type", String.valueOf(Constants.STUDENT_TYPE_HONOR))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String userFounded = "User Saved";

                            //if no error in response
                            if (message.toLowerCase().contains(userFounded.toLowerCase())) {

                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("data");
                                Student student;
                                student = new Student(
                                        Integer.parseInt(userJson.getString("id")),
                                        userJson.getString("name"),
                                        userJson.getString("email"),
                                        userJson.getInt("type")
                                );
                                SharedPrefManager.getInstance(getApplicationContext()).studentLogin(student);

                                verificationCode = userJson.getString("status");
                                Log.e("code", verificationCode);
                                SharedPrefManager.getInstance(getApplicationContext()).setVerified(false);
                                SharedPrefManager.getInstance(getApplicationContext()).setVerificationCode(verificationCode);
                                verifyEmail(verificationCode);
                                //storing the user in shared preferences
                            }
                            pDialog.dismiss();
                            mRegisterBtn.setEnabled(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("login catch", e.getMessage());
                            pDialog.dismiss();
                            mRegisterBtn.setEnabled(true);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mRegisterBtn.setEnabled(true);
                        Log.e("registererror", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(Register.this, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("name")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("name").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("email")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("email").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("password")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("password").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("type")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("type").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void goToDepartmentsForStudent() {
        Intent i = new Intent(Register.this, ChooseDepartmentActivity.class);
        i.putExtra(Constants.FROM, Constants.USER_TYPE_STUDENT);
        startActivity(i);
        finish();
    }
}
//    private void sendVerificationRequest(String code) {
//        String url = Urls.EMAIL_VERIFICATION;
//        String email = mEmailET.getText().toString().trim();
//
//        Log.e("email", email);
//        Log.e("code", code);
//        Log.e("type", String.valueOf(selectedUserType));
//        pDialog.show();
//        AndroidNetworking.post(url)
//                .addBodyParameter("email", email)
//                .addBodyParameter("code", code)
//                .addBodyParameter("type", String.valueOf(selectedUserType))
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONObject obj = response;
//                            String message = obj.getString("message");
//                            String success = "User founded";
//                            //if no error in response
//                            if (message.toLowerCase().contains(success.toLowerCase())) {
//
//                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.correct_code), Toast.LENGTH_SHORT).show();
//                                verificationDialog.dismiss();
//                                goToDepts();
//                                Log.e("code", code);
//                            } else {
//                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                            }
//                            pDialog.dismiss();
//                            mRegisterBtn.setEnabled(true);
//                        } catch (JSONException e) {
//                            pDialog.dismiss();
//                            e.printStackTrace();
//                            Log.e("codest catch", e.getMessage());
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        pDialog.dismiss();
//                        Log.e("codestror", anError.getErrorBody());
//                        try {
//                            JSONObject error = new JSONObject(anError.getErrorBody());
//                            JSONObject data = error.getJSONObject("data");
//                            Toast.makeText(Register.this, error.getString("message"), Toast.LENGTH_SHORT).show();
//                            if (data.has("email")) {
//                                Toast.makeText(getApplicationContext(), data.getJSONArray("email").toString(), Toast.LENGTH_SHORT).show();
//                            }
//                            if (data.has("code")) {
//                                Toast.makeText(getApplicationContext(), data.getJSONArray("code").toString(), Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//    }
