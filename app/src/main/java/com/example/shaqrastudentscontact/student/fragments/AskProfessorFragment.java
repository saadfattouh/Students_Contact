package com.example.shaqrastudentscontact.student.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.utils.Validation;

public class AskProfessorFragment extends Fragment {

    EditText mTitleEt, mDetailsEt;
    Button mSendBtn;

    String  professor_id;

    Context context;

    public AskProfessorFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            professor_id = getArguments().getString("prof_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ask_professor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitleEt = view.findViewById(R.id.title);
        mDetailsEt = view.findViewById(R.id.details);
        mSendBtn = view.findViewById(R.id.send_btn);

        mSendBtn.setOnClickListener(v -> {
            if(Validation.validateInput(context, mTitleEt, mDetailsEt)){
                String title = mTitleEt.getText().toString();
                String details = mDetailsEt.getText().toString();
                sendQuestion(title, details, professor_id);
            }
        });
    }

    //todo api call (must get student id before sending
    private void sendQuestion(String title, String details, String professor_id) {

    }
}