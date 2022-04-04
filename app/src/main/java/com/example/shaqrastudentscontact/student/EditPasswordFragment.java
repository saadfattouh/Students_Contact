package com.example.shaqrastudentscontact.student;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.text.TextUtils;
import android.util.Log;
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
import com.example.shaqrastudentscontact.utils.Constants;
import com.example.shaqrastudentscontact.utils.SharedPrefManager;
import com.example.shaqrastudentscontact.utils.Urls;
import org.json.JSONException;
import org.json.JSONObject;

public class EditPasswordFragment extends Fragment {

    EditText mPassword, mConfirmPassword;
    Button mUpdateBtn;
    Context context;

    ProgressDialog pDialog;
    NavController navController;

    public EditPasswordFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
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
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);
        navController = Navigation.findNavController(view);

        mUpdateBtn.setOnClickListener(v -> {
            if(validateUserInput()){
                if(validateUserInput()){
                    int type = SharedPrefManager.getInstance(context).getUserType();
                    if(type == Constants.USER_TYPE_STUDENT){
                        editStudentPass();
                    }else{
                        editProfPass();
                    }
                }
            }
        });

    }

    private void editStudentPass() {
        mUpdateBtn.setEnabled(false);
        pDialog.show();

        String url = Urls.RESET_PASSWORD_STUDENT_URL;
        String pass = mPassword.getText().toString().trim();

        String studentId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        AndroidNetworking.post(url)
                .addBodyParameter("student_id", studentId)
                .addBodyParameter("password", pass)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String userFounded = "User founded";
                            //if no error in response
                            if (message.toLowerCase().contains(userFounded.toLowerCase())) {
                                Toast.makeText(context, context.getResources().getString(R.string.password_edit), Toast.LENGTH_SHORT).show();
                            }
                            mUpdateBtn.setEnabled(true);
                            pDialog.dismiss();
                            navController.popBackStack();

                        } catch (JSONException e) {
                            mUpdateBtn.setEnabled(true);
                            pDialog.dismiss();
                            e.printStackTrace();
                            Log.e("editstprof catch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        mUpdateBtn.setEnabled(true);
                        pDialog.dismiss();
                        Log.e("editsterror", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("student_id")) {
                                Toast.makeText(context, data.getJSONArray("student_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("password")) {
                                Toast.makeText(context, data.getJSONArray("password").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    private void editProfPass() {
        mUpdateBtn.setEnabled(false);
        pDialog.show();

        String url = Urls.RESET_PASSWORD_PROFESSOR_URL;
        String pass = mPassword.getText().toString().trim();

        String profId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        AndroidNetworking.post(url)
                .addBodyParameter("professor_id", profId)
                .addBodyParameter("password", pass)
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
                            //if no error in response
                            if (message.toLowerCase().contains(userFounded.toLowerCase())) {
                                Toast.makeText(context, context.getResources().getString(R.string.password_edit), Toast.LENGTH_SHORT).show();
                            }
                            mUpdateBtn.setEnabled(true);
                            pDialog.dismiss();
                            navController.popBackStack();
                        } catch (JSONException e) {
                            mUpdateBtn.setEnabled(true);
                            pDialog.dismiss();
                            e.printStackTrace();
                            Log.e("editprof catch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        mUpdateBtn.setEnabled(true);
                        pDialog.dismiss();
                        Log.e("editproferror", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("professor_id")) {
                                Toast.makeText(context, data.getJSONArray("professor_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("password")) {
                                Toast.makeText(context, data.getJSONArray("password").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private boolean validateUserInput() {
        //first getting the values
        final String pass = mPassword.getText().toString();
        final String confirmPass = mConfirmPassword.getText().toString();

        //checking if username is empty
        if (TextUtils.isEmpty(pass) || TextUtils.isEmpty(confirmPass) ) {
            Toast.makeText(context, getResources().getString(R.string.field_missing_message), Toast.LENGTH_SHORT).show();
            mUpdateBtn.setEnabled(true);
            return false;

        }else if (!pass.equals(confirmPass)){
            Toast.makeText(context, getResources().getString(R.string.password_not_same), Toast.LENGTH_SHORT).show();
            mUpdateBtn.setEnabled(true);
            return false;
        }
        return true;
    }
}