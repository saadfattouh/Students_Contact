package com.example.shaqrastudentscontact.professor.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shaqrastudentscontact.R;

public class ProfessorProfileFragment extends Fragment {

    TextView name, department, email, specialization;
    Button editPass;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public ProfessorProfileFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_professor_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = view.findViewById(R.id.professor_name);
        department = view.findViewById(R.id.department);
        specialization = view.findViewById(R.id.specialization);
        email = view.findViewById(R.id.email);
        editPass = view.findViewById(R.id.edit_password);
        //TODO: get info from shared preferences
        String txt = "";
        name.setText(txt);
        department.setText(txt);
        specialization.setText(txt);
        email.setText(txt);

        editPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO:navigation to edit pass fragment
            }
        });

    }
}