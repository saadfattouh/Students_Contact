package com.example.shaqrastudentscontact.student.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.Professor;
import com.example.shaqrastudentscontact.models.Student;
import com.example.shaqrastudentscontact.student.adapters.HonorStudentsAdapter;
import com.example.shaqrastudentscontact.student.adapters.ProfessorsAdapter;

import java.util.ArrayList;

public class HonorStudentsFragment extends Fragment {

    ArrayList<Student> list;
    RecyclerView mList;
    HonorStudentsAdapter mAdapter;
    private ProgressDialog pDialog;

    Context ctx;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    public HonorStudentsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_honor_students, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = view.findViewById(R.id.rv);


        list = new ArrayList<Student>() {
            {
                add(new Student(1, "student", "student@",1));

            }

        };
        mAdapter = new HonorStudentsAdapter(ctx, list);

        mList.setAdapter(mAdapter);
    }
}