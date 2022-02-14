package com.example.shaqrastudentscontact.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.api.Urls;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateProfile extends AppCompatActivity {

    EditText mPassword;
    EditText mConfirmPassword;

    Button mUpdateBtn;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


        bindViews();

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        mUpdateBtn.setOnClickListener(v -> {
            if(validateUserInput()){
                mUpdateBtn.setEnabled(false);
                register();
            }
        });


    }


    private void bindViews() {
        mPassword = findViewById(R.id.password);
        mConfirmPassword = findViewById(R.id.confirm_password);
        mUpdateBtn = findViewById(R.id.btn_update);
    }

    private boolean validateUserInput() {

        //first getting the values
        final String pass = mPassword.getText().toString();
        final String confirmPass = mConfirmPassword.getText().toString();


        //checking if username is empty
        if (TextUtils.isEmpty(pass) || TextUtils.isEmpty(confirmPass) ) {
            Toast.makeText(this, getResources().getString(R.string.field_missing_message), Toast.LENGTH_SHORT).show();
            mUpdateBtn.setEnabled(true);
            return false;

        }else if (!pass.equals(confirmPass)){
            Toast.makeText(this, getResources().getString(R.string.password_not_same), Toast.LENGTH_SHORT).show();
            mUpdateBtn.setEnabled(true);
            return false;
        }

        return true;
    }

    private void register() {
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        //first getting the values
        final String pass = mPassword.getText().toString();

        String url = Urls.BASE_URL + Urls.REGISTER_URL;

        AndroidNetworking.post(url)
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
//                                        "+966 "+userJson.getString("phone")
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
                        mUpdateBtn.setEnabled(true);
                        Toast.makeText(UpdateProfile.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}