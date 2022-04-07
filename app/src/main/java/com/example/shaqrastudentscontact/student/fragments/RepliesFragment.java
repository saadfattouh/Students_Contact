package com.example.shaqrastudentscontact.student.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.Reply;
import com.example.shaqrastudentscontact.student.adapters.RepliesAdapter;
import com.example.shaqrastudentscontact.utils.SharedPrefManager;
import com.example.shaqrastudentscontact.utils.Urls;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class RepliesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    EditText replyET;
    ImageView replyBtn;
    Context context;

    RecyclerView mList;
    RepliesAdapter mAdapter;

    ArrayList<Reply> list;
    String questionId;
    ProgressDialog pDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public RepliesFragment() {}


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            questionId = getArguments().getString("question_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_replies, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.secondary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            getQuestionReplies(questionId);
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        replyET = view.findViewById(R.id.reply_et);
        replyBtn = view.findViewById(R.id.reply_btn);

        mList = view.findViewById(R.id.replies_list);
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);

        replyBtn.setOnClickListener(v -> {
            String reply = replyET.getText().toString().trim();
            if(reply.isEmpty()){
                Toast.makeText(context, context.getResources().getString(R.string.you_cant_post_empty_reply), Toast.LENGTH_SHORT).show();
            }else{
                postReply(questionId, reply);
            }
        });

    }


    private void getQuestionReplies(String questionId) {
        String url = Urls.GET_QUESTION_REPLIES;
        pDialog.show();
        list = new ArrayList<Reply>();
        AndroidNetworking.get(url)
                .addQueryParameter("question_id", questionId)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String messageGot = "founded";
                            String message = response.getString("message");
                            if (message.toLowerCase().contains(messageGot.toLowerCase())) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    JSONObject student_data = obj.getJSONObject("student");

                                    list.add(
                                            new Reply(
                                                    Integer.parseInt(obj.getString("id")),
                                                    student_data.getString("name"),
                                                    obj.getString("reply"),
                                                    obj.getString("created_at")
                                            )
                                    );
                                }
                                mAdapter = new RepliesAdapter(context, list);
                                mList.setAdapter(mAdapter);
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                            mSwipeRefreshLayout.setRefreshing(false);
                            pDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            mSwipeRefreshLayout.setRefreshing(false);
                            pDialog.dismiss();
                            Log.e("replies catch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        pDialog.dismiss();
                        Log.e("replies anerror", error.getErrorBody());
                    }
                });
    }

    @Override
    public void onRefresh() {
        getQuestionReplies(questionId);
    }

    private void postReply(String questionId, String reply) {
        pDialog.show();

        String url = Urls.POST_REPLY_FOR_QUESTION;
        String studentId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        AndroidNetworking.post(url)
                .addBodyParameter("student_id", studentId)
                .addBodyParameter("question_id", questionId)
                .addBodyParameter("reply", reply)
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
                                Toast.makeText(context, context.getResources().getString(R.string.success_post_reply), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                            Log.e("postQ catch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        Log.e("postQ", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("student_id")) {
                                Toast.makeText(context, data.getJSONArray("student_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("question_id")) {
                                Toast.makeText(context, data.getJSONArray("question_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("reply")) {
                                Toast.makeText(context, data.getJSONArray("reply").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}