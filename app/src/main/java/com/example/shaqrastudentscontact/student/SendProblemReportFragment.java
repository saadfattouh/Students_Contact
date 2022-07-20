package com.example.shaqrastudentscontact.student;

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
import com.androidnetworking.model.Progress;
import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.utils.SharedPrefManager;
import com.example.shaqrastudentscontact.utils.Urls;
import com.example.shaqrastudentscontact.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

public class SendProblemReportFragment extends Fragment {

    Button mSendReportBtn;
    EditText mTitleET, mContent;
    Context context;
    ProgressDialog pDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public SendProblemReportFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_send_problem_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleET = view.findViewById(R.id.report_title);
        mContent = view.findViewById(R.id.report_content);
        mSendReportBtn = view.findViewById(R.id.send_btn);


        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);

        mSendReportBtn.setOnClickListener(v->{
            if(Validation.validateInput(context, mTitleET, mContent)){
                sendReport();

            }
        });
    }

    private void sendReport() {
        String url = Urls.SEND_REPORT;
        String userId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        String title = mTitleET.getText().toString().trim();
        String content = mContent.getText().toString().trim();
        Log.e("user_id", userId);
        Log.e("title", title);
        Log.e("content", content);
        mSendReportBtn.setEnabled(false);
        pDialog.show();
        AndroidNetworking.post(url)
                .addBodyParameter("user_id", userId)
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
                            String userFounded = "Report Saved";
                            //if no error in response
                            if (message.toLowerCase().contains(userFounded.toLowerCase())) {
                                Toast.makeText(context, context.getResources().getString(R.string.send_report_successfully), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, context.getResources().getString(R.string.some_error), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                            mSendReportBtn.setEnabled(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("report catch", e.getMessage());
                            pDialog.dismiss();
                            mSendReportBtn.setEnabled(true);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mSendReportBtn.setEnabled(true);

                        Log.e("reporterror", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("user_id")) {
                                Toast.makeText(context, data.getJSONArray("user_id").toString(), Toast.LENGTH_SHORT).show();
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