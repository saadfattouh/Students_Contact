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
import com.example.shaqrastudentscontact.models.ProfessorQuestion;
import com.example.shaqrastudentscontact.models.Question;
import com.example.shaqrastudentscontact.student.adapters.CommunityAdapter;
import com.example.shaqrastudentscontact.student.adapters.ProfQuestionRepliesAdapter;
import com.example.shaqrastudentscontact.utils.Constants;
import com.example.shaqrastudentscontact.utils.SharedPrefManager;
import com.example.shaqrastudentscontact.utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfessorDetailsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    Context context;

    TextView askBtn;
    TextView mName, mEmail , mStartFreeTime, mEndFreeTime;
    String professorId;
    String profName, profEmail, profStartFreeTime, profEndFreeTime;

    NavController navController;

    RecyclerView mList;
    ProfQuestionRepliesAdapter mAdapter;
    ArrayList<ProfessorQuestion> list;
    ProgressDialog pDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public ProfessorDetailsFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            professorId = getArguments().getString(Constants.PROFESSOR_KEY, null);
            profName = getArguments().getString(Constants.PROFESSOR_NAME, null);
            profEmail = getArguments().getString(Constants.PROFESSOR_EMAIL, null);
            profStartFreeTime = getArguments().getString(Constants.PROFESSOR_FREE_TIME_START, null);
            profEndFreeTime = getArguments().getString(Constants.PROFESSOR_FREE_TIME_END, null);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_professor_details, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.secondary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            getQuestionsToProf();
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        askBtn = view.findViewById(R.id.ask_btn);
        mList = view.findViewById(R.id.rv);
        mName = view.findViewById(R.id.name);
        mEmail = view.findViewById(R.id.email);
        mStartFreeTime = view.findViewById(R.id.from);
        mEndFreeTime = view.findViewById(R.id.to);

        mName.setText(profName);
        mEmail.setText(profEmail);
        mStartFreeTime.setText(profStartFreeTime.equals("null")?"no time selected":profStartFreeTime);
        mEndFreeTime.setText(profEndFreeTime.equals("null")?"no time selected":profEndFreeTime);
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);

        askBtn.setOnClickListener(v -> {

            navController = Navigation.findNavController(view);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.PROFESSOR_KEY, professorId);
            navController.navigate(R.id.action_profDetailsFragment_to_askProfessorFragment, bundle);

        });



    }

    private void getQuestionsToProf(){
        String url = Urls.GET_QUESTIONS_TO_PROF;
        String studentId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        pDialog.show();
        list = new ArrayList<ProfessorQuestion>();
        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addQueryParameter("student_id", studentId)
                .addQueryParameter("professor_id", professorId)
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
                                            new ProfessorQuestion(
                                                    Integer.parseInt(obj.getString("id")),
                                                    Integer.parseInt(obj.getString("user_id")),
                                                    obj.getString("user_id"),//TODO :this should be student name
                                                    obj.getString("title"),
                                                    obj.getString("content"),
                                                    obj.getString("answer"),
                                                    obj.getString("created_at")
                                            )
                                    );
                                }
                                mAdapter = new ProfQuestionRepliesAdapter(context, list);
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
        getQuestionsToProf();
    }
}