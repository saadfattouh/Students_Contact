package com.example.shaqrastudentscontact.honor_student.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.honor_student.adapters.HonorStudentRequestedQuestionsAdapter;
import com.example.shaqrastudentscontact.models.HonorStudentQuestion;
import com.example.shaqrastudentscontact.utils.SharedPrefManager;

import java.util.ArrayList;

public class HonorStudentRequestedQuestionsFragment extends Fragment {


    Context ctx;
    RecyclerView mList;
    ArrayList<HonorStudentQuestion> list;
    HonorStudentRequestedQuestionsAdapter mAdapter;

    ProgressDialog pDialog;

    int myId;
    SharedPrefManager prefManager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    public HonorStudentRequestedQuestionsFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = SharedPrefManager.getInstance(ctx);
        myId = prefManager.getUserId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_honor_student_requested_questions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList = view.findViewById(R.id.rv);

        getQuestions();
    }

    //todo api call get asked questions by honor st_id
    private void getQuestions() {

        list = new ArrayList<HonorStudentQuestion>()
        {{
            add(new HonorStudentQuestion(1, 12, "saad", 2, "details", "answer", "20/10/2021"));
            add(new HonorStudentQuestion(1, 12, "saad", 2, "details", "answer", "20/10/2021"));
            add(new HonorStudentQuestion(1, 12, "saad", 2, "details", "", "20/10/2021"));
            add(new HonorStudentQuestion(1, 12, "saad", 2, "details", "", "20/10/2021"));
            add(new HonorStudentQuestion(1, 12, "saad", 2, "details", "", "20/10/2021"));

        }};

        mAdapter = new HonorStudentRequestedQuestionsAdapter(ctx, list);
        mList.setAdapter(mAdapter);

    }
}