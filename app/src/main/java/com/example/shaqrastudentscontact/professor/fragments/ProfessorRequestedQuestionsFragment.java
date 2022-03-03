package com.example.shaqrastudentscontact.professor.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.Question;

import java.util.ArrayList;


public class ProfessorRequestedQuestionsFragment extends Fragment {

    Context ctx;
    RecyclerView mList;
    ArrayList<Question> list;
    ProgressDialog pDialog;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    public ProfessorRequestedQuestionsFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_professor_requested_questions, container, false);
    }
}