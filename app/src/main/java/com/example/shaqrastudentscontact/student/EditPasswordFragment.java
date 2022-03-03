package com.example.shaqrastudentscontact.student;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.activities.UpdateProfile;
import com.example.shaqrastudentscontact.utils.SharedPrefManager;
import com.example.shaqrastudentscontact.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;


public class EditPasswordFragment extends Fragment {
    EditText mPassword, mConfirmPassword;
    Button mUpdateBtn;
    Context ctx;

    SharedPrefManager sp;
    private ProgressDialog pDialog;

    public EditPasswordFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPassword = view.findViewById(R.id.password);
        mConfirmPassword = view.findViewById(R.id.confirm_password);
        mUpdateBtn = view.findViewById(R.id.btn_update);

        // Progress dialog
        pDialog = new ProgressDialog(ctx);
        pDialog.setCancelable(false);


        sp = SharedPrefManager.getInstance(ctx);
        int userId = sp.getUserId();
        String pass = mPassword.getText().toString().trim();
        mUpdateBtn.setOnClickListener(v -> {
            if(validateUserInput()){
                mUpdateBtn.setEnabled(false);
                editPass(userId,pass);
            }
        });

    }

    private void editPass(int userId, String pass) {

        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        //first getting the values

        String url = Urls.RESET_PASSWORD_URL;

        AndroidNetworking.post(url)
                .addBodyParameter("password", pass)
                .addBodyParameter("user_id",String.valueOf(userId))

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
                        Toast.makeText(ctx, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateUserInput() {
        //first getting the values
        final String pass = mPassword.getText().toString();
        final String confirmPass = mConfirmPassword.getText().toString();

        //checking if username is empty
        if (TextUtils.isEmpty(pass) || TextUtils.isEmpty(confirmPass) ) {
            Toast.makeText(ctx, getResources().getString(R.string.field_missing_message), Toast.LENGTH_SHORT).show();
            mUpdateBtn.setEnabled(true);
            return false;

        }else if (!pass.equals(confirmPass)){
            Toast.makeText(ctx, getResources().getString(R.string.password_not_same), Toast.LENGTH_SHORT).show();
            mUpdateBtn.setEnabled(true);
            return false;
        }
        return true;
    }
}