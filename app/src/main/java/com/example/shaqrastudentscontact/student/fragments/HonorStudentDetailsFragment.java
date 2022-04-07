package com.example.shaqrastudentscontact.student.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.HonorStudentQuestion;
import com.example.shaqrastudentscontact.student.adapters.HonorQuestionRepliesAdapter;
import com.example.shaqrastudentscontact.utils.Constants;
import com.example.shaqrastudentscontact.utils.SharedPrefManager;
import com.example.shaqrastudentscontact.utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HonorStudentDetailsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    Context context;

    TextView askBtn, mName, mEmail;

    String honorStudentId, honorName, honorEmail;

    NavController navController;

    RecyclerView mList;
    HonorQuestionRepliesAdapter mAdapter;
    ArrayList<HonorStudentQuestion> list;
    ProgressDialog pDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public HonorStudentDetailsFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            honorStudentId = getArguments().getString(Constants.HONOR_STUDENT_KEY, null);
            honorName = getArguments().getString(Constants.HONOR_STUDENT_NAME, null);
            honorEmail = getArguments().getString(Constants.HONOR_STUDENT_EMAIL, null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_honor_student_details, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.secondary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            getQuestionsToHonor();
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mName = view.findViewById(R.id.name);
        mEmail = view.findViewById(R.id.email);
        mName.setText(honorName);
        mEmail.setText(honorEmail);
        askBtn = view.findViewById(R.id.ask_btn);
        mList = view.findViewById(R.id.rv);

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);

        askBtn.setOnClickListener(v -> {
            navController = Navigation.findNavController(view);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.HONOR_STUDENT_KEY, honorStudentId);
            navController.navigate(R.id.action_honorDetailsFragment_to_askHonorFragment, bundle);
        });
    }

    private void getQuestionsToHonor(){
        String url = Urls.GET_QUESTIONS_TO_HONOR;
        String studentId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        pDialog.show();
        list = new ArrayList<HonorStudentQuestion>();
        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addQueryParameter("student_id", studentId)
                .addQueryParameter("honor_id", honorStudentId)
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
                                    list.add(
                                            new HonorStudentQuestion(
                                                    Integer.parseInt(obj.getString("id")),
                                                    Integer.parseInt(obj.getString("user_id")),
                                                    obj.getString("user_id"),//TODO :this should be student name
                                                    Integer.parseInt(obj.getString("honor_id")),
                                                    obj.getString("title"),
                                                    obj.getString("content"),
                                                    obj.getString("answer"),
                                                    obj.getString("created_at")
                                            )
                                    );
                                }
                                mAdapter = new HonorQuestionRepliesAdapter(context, list);
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
        getQuestionsToHonor();
    }
}