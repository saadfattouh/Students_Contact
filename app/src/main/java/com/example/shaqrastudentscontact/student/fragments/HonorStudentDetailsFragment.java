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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.HonorStudentQuestion;
import com.example.shaqrastudentscontact.student.adapters.HonorQuestionRepliesAdapter;
import com.example.shaqrastudentscontact.utils.Constants;

import java.util.ArrayList;

public class HonorStudentDetailsFragment extends Fragment {

    Context ctx;

    TextView askBtn;
    TextView mName, mEmail;

    String honorStudentId;
    String name, email;

    NavController navController;


    RecyclerView mQuestionsList;
    HonorQuestionRepliesAdapter mAdapter;
    ArrayList<HonorStudentQuestion> questions;
    ProgressDialog pDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    public HonorStudentDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            honorStudentId = getArguments().getString(Constants.HONOR_STUDENT_KEY, null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_honor_student_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mName = view.findViewById(R.id.name);
        mEmail = view.findViewById(R.id.email);
        mName.setText(name);
        mEmail.setText(email);
        askBtn = view.findViewById(R.id.ask_btn);
        mQuestionsList = view.findViewById(R.id.rv);

        askBtn.setOnClickListener(v -> {

            navController = Navigation.findNavController(view);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.HONOR_STUDENT_KEY, honorStudentId);
            navController.navigate(R.id.action_honorDetailsFragment_to_askHonorFragment, bundle);

        });

        questions = new ArrayList<HonorStudentQuestion>()
        {{
            add(new HonorStudentQuestion(1, 1, "ahmad", 1,"question", "how to solve (1+1) equation in physics ?", "no comment !", "8-2-2022"));

        }};

        mAdapter = new HonorQuestionRepliesAdapter(ctx, questions);
        mQuestionsList.setAdapter(mAdapter);

    }
}