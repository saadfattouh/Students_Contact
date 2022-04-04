package com.example.shaqrastudentscontact.professor.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.example.shaqrastudentscontact.utils.SharedPrefManager;
import com.example.shaqrastudentscontact.utils.Urls;
import com.example.shaqrastudentscontact.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

public class ScheduleFreeTime extends Fragment {

    EditText mFromET, mToET;
    Button mScheduleBtn;

    Context context;

    int profId;

    ProgressDialog pDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public ScheduleFreeTime() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profId = SharedPrefManager.getInstance(context).getUserId();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_professor_schedule_free_time, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFromET = view.findViewById(R.id.from);
        mToET = view.findViewById(R.id.to);
        mScheduleBtn = view.findViewById(R.id.schedule);

        mScheduleBtn.setOnClickListener(v -> {
                    if(Validation.validateInput(context, mFromET, mToET)){
                        String from = mFromET.getText().toString().trim();
                        String to = mToET.getText().toString().trim();

                        schedule(from, to);
                    }
        });


    }

    private void schedule(String from, String to) {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Processing please wait...");
        pDialog.setCancelable(false);

        String url = Urls.PROFESSOR_SCHEDULE;

        pDialog.show();

        AndroidNetworking.post(url)
                .addBodyParameter("professor_id", String.valueOf(profId))
                .addBodyParameter("start_free_time", from)
                .addBodyParameter("end_free_time", to)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String userSaved = "User Saved!";
                            //if no error in response
                            if (message.equals(userSaved)) {
                                Toast.makeText(context, context.getResources().getString(R.string.success_update_schedule), Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(context, "some error has occured, please try again...", Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("postQ catch", e.getMessage());
                            pDialog.dismiss();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        Log.e("postQ", anError.getErrorBody());
                        Toast.makeText(context, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}