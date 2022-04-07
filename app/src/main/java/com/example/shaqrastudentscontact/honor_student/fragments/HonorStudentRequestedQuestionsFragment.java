package com.example.shaqrastudentscontact.honor_student.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.honor_student.adapters.HonorStudentRequestedQuestionsAdapter;
import com.example.shaqrastudentscontact.models.HonorStudentQuestion;
import com.example.shaqrastudentscontact.utils.SharedPrefManager;
import com.example.shaqrastudentscontact.utils.Urls;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class HonorStudentRequestedQuestionsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    Context context;
    RecyclerView mList;
    ArrayList<HonorStudentQuestion> list;
    HonorStudentRequestedQuestionsAdapter mAdapter;

    ProgressDialog pDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public HonorStudentRequestedQuestionsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_honor_student_requested_questions, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.secondary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            getQuestions();
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList = view.findViewById(R.id.rv);
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Processing please wait...");
        pDialog.setCancelable(false);
    }

    private void getQuestions() {
        pDialog.show();
        list = new ArrayList<HonorStudentQuestion>();

        String url = Urls.GET_HONOR_QUESTIONS;
        String userId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        Log.e("id", userId);
        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addQueryParameter("honor_id", userId)
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
//                                    JSONObject student_data = obj.getJSONObject("student");
                                    list.add(new HonorStudentQuestion(
                                            Integer.parseInt(obj.getString("id")),
                                            Integer.parseInt(obj.getString("user_id")),
                                            "student_name",
                                            Integer.parseInt(obj.getString("honor_id")),
                                            obj.getString("title"),
                                            obj.getString("content"),
                                            obj.getString("answer").equals("null")?"":obj.getString("answer"),
                                            obj.getString("created_at").split(" ")[0]
                                    ));

                                }
                                mAdapter = new HonorStudentRequestedQuestionsAdapter(context, list);

                                mList.setAdapter(mAdapter);
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                            mSwipeRefreshLayout.setRefreshing(false);
                            pDialog.dismiss();
                        } catch (Exception e) {
                            pDialog.dismiss();
                            mSwipeRefreshLayout.setRefreshing(false);
                            e.printStackTrace();
                            Log.e("cquestions catch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        mSwipeRefreshLayout.setRefreshing(false);
                        Log.e("cquestions anerror", error.getErrorBody());
                    }
                });
    }
    @Override
    public void onRefresh() {
        getQuestions();
    }
}