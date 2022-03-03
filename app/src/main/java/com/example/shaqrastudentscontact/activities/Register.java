package com.example.shaqrastudentscontact.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.shaqrastudentscontact.utils.Constants;
import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    EditText mNameET;
    EditText mPassET;
    EditText mEmailET;

    Button mRegisterBtn;
    Button mToLoginBtn;

    RadioGroup mAccountTypeSelector;
    int selectedUserType = -1;

    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mNameET = findViewById(R.id.name);
        mPassET = findViewById(R.id.password);
        mEmailET = findViewById(R.id.email);


        mRegisterBtn = findViewById(R.id.btnRegister);
        mToLoginBtn = findViewById(R.id.btnLinkToLoginScreen);

        mAccountTypeSelector = findViewById(R.id.type_selector);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        mToLoginBtn.setOnClickListener(v -> {
            finish();
        });

        mRegisterBtn.setOnClickListener(v -> {
            if(validateUserInput()){
                mRegisterBtn.setEnabled(false);
                register();
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


    private boolean validateUserInput() {

        //first getting the values
        final String pass = mPassET.getText().toString();
        final String name = mNameET.getText().toString();
        final String email = mEmailET.getText().toString();

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

        if(selectedUserType == -1){
            Toast.makeText(this, getResources().getString(R.string.type_missing_message), Toast.LENGTH_SHORT).show();
            mRegisterBtn.setEnabled(true);
            return false;
        }else {
            if(selectedUserType == Constants.USER_TYPE_PROFESSOR){
                if(!email.contains(getResources().getString(R.string.professor_email_suffex))){
                    Toast.makeText(this, getResources().getString(R.string.please_provide_a_professor_email), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else if(selectedUserType == Constants.USER_TYPE_STUDENT){
                if(!email.contains(getResources().getString(R.string.student_email_suffex))){
                    Toast.makeText(this, getResources().getString(R.string.please_provide_a_valid_student_email), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }

        return true;
    }

    private void register() {
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        String url = Urls.REGISTER_URL;

        //first getting the values
        final String pass = mPassET.getText().toString();
        final String name = mNameET.getText().toString();
        final String email = mEmailET.getText().toString();

        AndroidNetworking.post(url)
                .addBodyParameter("type", String.valueOf(selectedUserType))
                .addBodyParameter("name", name)
                .addBodyParameter("email", email)
                .addBodyParameter("password", pass)
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

//                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//
//                                //getting the user from the response
//                                JSONObject userJson = obj.getJSONObject("data");
//                                User user;
//                                SharedPrefManager.getInstance(getApplicationContext()).setUserType(Constants.USER);
//                                user = new User(
//                                        Integer.parseInt(userJson.getString("id")),
//                                        userJson.getString("name"),
//                                        "+966 "+userJson.getString("email")
//                                );
//
//                                //storing the user in shared preferences
//                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
//                                goToUserMainActivity();
//                                finish();
//
//                                mRegisterBtn.setEnabled(true);
//                            } else if(obj.getInt("status") == -1){
//                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                                mRegisterBtn.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mRegisterBtn.setEnabled(true);
                        Toast.makeText(Register.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}