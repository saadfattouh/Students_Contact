package com.example.shaqrastudentscontact.student.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
import com.example.shaqrastudentscontact.utils.Validation;
import org.json.JSONException;
import org.json.JSONObject;

public class AskHonorStudentFragment extends Fragment {

    EditText mTitleET, mContentET;
    Button mSendBtn;

    String honorId;

    Context context;
    ProgressDialog pDialog;
    NavController navController;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public AskHonorStudentFragment() {}

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

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);
        navController = Navigation.findNavController(view);

        mSendBtn.setOnClickListener(v -> {
            if(Validation.validateInput(context, mContentET)){
                String title = mTitleET.getText().toString();
                String content = mContentET.getText().toString();
                sendQuestion(title, content, honorId);
            }
        });
    }

    private void sendQuestion(String title, String content, String honorId) {
        String url = Urls.SEND_QUESTION_TO_HONOR;
        String studentId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());

        Log.e("student_id", studentId);
        Log.e("honor_id", honorId);
        pDialog.show();
        AndroidNetworking.post(url)
                .addBodyParameter("student_id", studentId)
                .addBodyParameter("honor_id", honorId)
                .addBodyParameter("title", title)
                .addBodyParameter("content", content)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String userFounded = "founded";
                            //if no error in response
                            if (message.toLowerCase().contains(userFounded.toLowerCase())) {
                                Toast.makeText(context, context.getResources().getString(R.string.send_question_successfully), Toast.LENGTH_SHORT).show();
                                navController.popBackStack();
                            }
                            pDialog.dismiss();
                            mSendBtn.setEnabled(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("askprof catch", e.getMessage());
                            pDialog.dismiss();
                            mSendBtn.setEnabled(true);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mSendBtn.setEnabled(true);

                        Log.e("askproferror", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("student_id")) {
                                Toast.makeText(context, data.getJSONArray("student_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("honor_id")) {
                                Toast.makeText(context, data.getJSONArray("professor_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("title")) {
                                Toast.makeText(context, data.getJSONArray("title").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("content")) {
                                Toast.makeText(context, data.getJSONArray("content").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}