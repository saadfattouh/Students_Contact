package com.example.shaqrastudentscontact.student.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RepliesFragment extends Fragment {
    EditText replyET;
    ImageView replyBtn;
    Context ctx;

    RecyclerView mList;
    RepliesAdapter mAdapter;
    String questionId;
    ProgressDialog pDialog;

    public RepliesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_replies, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        replyET = view.findViewById(R.id.reply_et);
        replyBtn = view.findViewById(R.id.reply_btn);

        mList = view.findViewById(R.id.replies_list);

//         getQuestionReplies(questionId);

        ArrayList<Reply> list = new ArrayList<Reply>(){{
            add(new Reply(1,"student","what is this ?", "12-12-2022" ));
            add(new Reply(1,"student","what is this ?", "12-12-2022" ));
            add(new Reply(1,"student","what is this ?", "12-12-2022" ));
            add(new Reply(1,"student","what is  ?", "12-12-2022" ));
            add(new Reply(1,"student","how is this ?", "12-12-2022"));
            add(new Reply(1,"student","what is this ?", "12-12-2022"));
            add(new Reply(1,"student","why is this ?", "12-12-2022"));
        }};
        mAdapter = new RepliesAdapter(ctx, list);

        mList.setAdapter(mAdapter);
        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reply = replyET.getText().toString().trim();
                if(reply.isEmpty()){
                    Toast.makeText(ctx, ctx.getResources().getString(R.string.you_cant_post_empty_reply), Toast.LENGTH_SHORT).show();
                }else{
                    postReply(questionId, reply);
                }
            }
        });

    }

    private void postReply(String questionId, String reply) {
        String url = Urls.POST_REPLY_FOR_QUESTION;
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        int userId = SharedPrefManager.getInstance(ctx).getUserId();

        AndroidNetworking.post(url).setPriority(Priority.MEDIUM)
                .addBodyParameter("user_id",String.valueOf(userId))
                .addBodyParameter("question_id",questionId)
                .addBodyParameter("reply",reply)
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

    private void getQuestionReplies(String questionId) {
        String url = Urls.GET_QUESTION_REPLIES;
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        int id = SharedPrefManager.getInstance(ctx).getUserId();

        AndroidNetworking.post(url).setPriority(Priority.MEDIUM)
                .addBodyParameter("question_id",questionId)
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