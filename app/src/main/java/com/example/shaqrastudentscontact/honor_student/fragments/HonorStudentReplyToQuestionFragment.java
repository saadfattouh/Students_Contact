package com.example.shaqrastudentscontact.honor_student.fragments;

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
import android.widget.Toast;

import com.example.shaqrastudentscontact.R;

public class HonorStudentReplyToQuestionFragment extends Fragment {

    Context ctx;
    EditText mReplyContent;
    Button mReplyBtn;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    public HonorStudentReplyToQuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_honor_student_reply_to_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mReplyContent = view.findViewById(R.id.reply_content);
        mReplyBtn = view.findViewById(R.id.reply_btn);

        mReplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mReplyContent.getText().toString().isEmpty()){
                    Toast.makeText(ctx, getResources().getString(R.string.empty_reply), Toast.LENGTH_SHORT).show();
                }else{
                    sendReply();
                }
            }
        });
    }

    private void sendReply() {
    }
}