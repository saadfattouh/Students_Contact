package com.example.shaqrastudentscontact.student.fragments;

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

import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.Professor;
import com.example.shaqrastudentscontact.models.Reply;
import com.example.shaqrastudentscontact.student.adapters.ProfessorsAdapter;
import com.example.shaqrastudentscontact.student.adapters.RepliesAdapter;

import java.util.ArrayList;

public class ProfessorsFragment extends Fragment {

    Context ctx;

    RecyclerView mList;
    ProfessorsAdapter mAdapter;

    public ProfessorsFragment() {
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_professors, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList = view.findViewById(R.id.rv);


        ArrayList<Professor> list = new ArrayList<Professor>() {
            {
               add(new Professor(1, "prof", "information technology","MAth","@prof"));
               add(new Professor(1, "prof", "information technology","MAth","@prof"));
               add(new Professor(1, "prof", "information technology","MAth","@prof"));
               add(new Professor(1, "prof", "information technology","MAth","@prof"));
               add(new Professor(1, "prof", "information technology","MAth","@prof"));
               add(new Professor(1, "prof", "information technology","MAth","@prof"));

            }

        };
        mAdapter = new ProfessorsAdapter(ctx, list);

        mList.setAdapter(mAdapter);
    }

    private void postQuestion(String question) {
    }
}

