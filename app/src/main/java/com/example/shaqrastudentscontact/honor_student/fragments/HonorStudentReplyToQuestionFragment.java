package com.example.shaqrastudentscontact.honor_student.fragments;

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
import com.example.shaqrastudentscontact.utils.SharedPrefManager;
import com.example.shaqrastudentscontact.utils.Urls;
import org.json.JSONException;
import org.json.JSONObject;

public class HonorStudentReplyToQuestionFragment extends Fragment {

    Context context;
    EditText mReplyContent;
    Button mReplyBtn;

    String questionId;
    ProgressDialog pDialog;
    NavController navController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public HonorStudentReplyToQuestionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionId = getArguments().getString("question_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_honor_student_reply_to_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mReplyContent = view.findViewById(R.id.reply_content);
        mReplyBtn = view.findViewById(R.id.reply_btn);

        navController = Navigation.findNavController(view);
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);

        mReplyBtn.setOnClickListener(v -> {
            if (mReplyContent.getText().toString().isEmpty()) {
                Toast.makeText(context, getResources().getString(R.string.empty_reply), Toast.LENGTH_SHORT).show();
            } else {
                sendReply();
            }
        });
    }

    private void sendReply() {
        pDialog.show();
        String answer = mReplyContent.getText().toString();

        String url = Urls.ANSWER_HONOR_QUESTION;
        String honorID = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        Log.e("id", honorID);
        Log.e("quId", questionId);
        Log.e("ans", answer);
        Log.e("url", url);
        AndroidNetworking.post(url)
                .addBodyParameter("honor_id", honorID)
                .addBodyParameter("question_id", questionId)
                .addBodyParameter("answer", answer)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String founded = "founded";
                            //if no error in response
                            if (message.toLowerCase().contains(founded.toLowerCase())) {
                                Toast.makeText(context, context.getResources().getString(R.string.success_post_reply), Toast.LENGTH_SHORT).show();
                                 navController.popBackStack();
                            }

                            pDialog.dismiss();
                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                            Log.e("postHReply catch", e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        Log.e("postHReply error", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("honor_id")) {
                                Toast.makeText(context, data.getJSONArray("honor_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("question_id")) {
                                Toast.makeText(context, data.getJSONArray("question_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("answer")) {
                                Toast.makeText(context, data.getJSONArray("answer").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}