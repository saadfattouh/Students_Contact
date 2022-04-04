package com.example.shaqrastudentscontact.student.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.example.shaqrastudentscontact.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

public class askHonorStudentFragment extends Fragment {

    EditText mTitleET, mContentET;
    Button mSendBtn;

    String honorId;

    Context ctx;
    ProgressDialog pDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    public askHonorStudentFragment() {
        // Required empty public constructor

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            honorId = getArguments().getString(Constants.HONOR_STUDENT_KEY);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ask_honor_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitleET = view.findViewById(R.id.title);
        mContentET = view.findViewById(R.id.details);
        mSendBtn = view.findViewById(R.id.send_btn);

        mSendBtn.setOnClickListener(v -> {
            if(Validation.validateInput(ctx, mContentET)){
                String title = mTitleET.getText().toString();
                String content = mContentET.getText().toString();
                sendQuestion(title, content, honorId);
            }
        });
    }

    private void sendQuestion(String title, String content, String honorId) {

        String url = Urls.SEND_QUESTION_TO_PROF;
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        AndroidNetworking.post(url).setPriority(Priority.MEDIUM)
                .addBodyParameter("student_id", String.valueOf(SharedPrefManager.getInstance(ctx).getUserId()))
                .addBodyParameter("title",title)
                .addBodyParameter("content",content)
                .addBodyParameter("honor_id", honorId)
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
                        Toast.makeText(ctx, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}