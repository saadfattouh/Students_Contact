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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.Question;
import com.example.shaqrastudentscontact.student.adapters.CommunityAdapter;
import com.example.shaqrastudentscontact.utils.SharedPrefManager;
import com.example.shaqrastudentscontact.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CommunityFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    EditText questionET;
    ImageView postQuestionBtn;
    Context context;

    RadioGroup contentTypeSelector;

    RecyclerView mList;
    CommunityAdapter mAdapter;
    ArrayList<Question> list;
    ProgressDialog pDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public CommunityFragment() {}

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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_student_community, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.secondary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            get_questions();
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        questionET = view.findViewById(R.id.question_et);
        postQuestionBtn = view.findViewById(R.id.question_btn);
        mList = view.findViewById(R.id.rv);
        contentTypeSelector = view.findViewById(R.id.questions_type_selector);
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);

        postQuestionBtn.setOnClickListener(v -> {
            String question = questionET.getText().toString().trim();
            if(question.isEmpty()){
                Toast.makeText(context, context.getResources().getString(R.string.you_cant_post_empty_question), Toast.LENGTH_SHORT).show();
            }else{
                postQuestion(question);
            }
        });

        contentTypeSelector.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton allBtn = view.findViewById(R.id.all);
            RadioButton commonBtn = view.findViewById(R.id.common);

            switch (checkedId){
                case R.id.all:
                    allBtn.setTextColor(getResources().getColor(R.color.white));
                    commonBtn.setTextColor(getResources().getColor(R.color.black));
                    mAdapter.getFilter().filter("");
                    break;
                case R.id.common:
                    commonBtn.setTextColor(getResources().getColor(R.color.white));
                    allBtn.setTextColor(getResources().getColor(R.color.black));
                    mAdapter.getFilter().filter("common");
                    break;
            }
        });
    }

    private void get_questions(){
        String url = Urls.GET_COMMUNITY_QUESTIONS;
        String deptId = String.valueOf(SharedPrefManager.getInstance(context).getSelectedDept());
        pDialog.show();
        list = new ArrayList<Question>();
        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addQueryParameter("department_id", deptId)
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
                                            new Question(
                                                    Integer.parseInt(obj.getString("id")),
                                                    student_data.getString("name"),
                                                    obj.getString("question"),
                                                    obj.getString("created_at"),
                                                    obj.getInt("is_common")==1
                                            )
                                    );
                                }
                                mAdapter = new CommunityAdapter(context, list);

                                mList.setAdapter(mAdapter);
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                            mSwipeRefreshLayout.setRefreshing(false);
                            pDialog.dismiss();
                        } catch (Exception e) {
                            mSwipeRefreshLayout.setRefreshing(false);
                            pDialog.dismiss();
                            e.printStackTrace();
                            Log.e("cquestions catch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        pDialog.dismiss();
                        Log.e("cquestions anerror", error.getErrorBody());
                    }
                });
    }

    @Override
    public void onRefresh() {
        get_questions();
    }

    private void postQuestion(String question) {
        pDialog.show();

        String url = Urls.POST_COMMUNITY_QUESTION;
        String studentId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        String departmentId = String.valueOf(SharedPrefManager.getInstance(context).getSelectedDept());
        AndroidNetworking.post(url)
                .addBodyParameter("student_id", studentId)
                .addBodyParameter("question", question)
                .addBodyParameter("department_id", departmentId)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String userFounded = "founded";//TODO
                            //if no error in response
                            if (message.toLowerCase().contains(userFounded.toLowerCase())) {
                                Toast.makeText(context, context.getResources().getString(R.string.success_post_question), Toast.LENGTH_SHORT).show();
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
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("student_id")) {
                                Toast.makeText(context, data.getJSONArray("student_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("question")) {
                                Toast.makeText(context, data.getJSONArray("question").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("department_id")) {
                                Toast.makeText(context, data.getJSONArray("department_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}