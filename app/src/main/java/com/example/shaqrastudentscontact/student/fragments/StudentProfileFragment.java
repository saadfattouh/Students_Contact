package com.example.shaqrastudentscontact.student.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.Student;
import com.example.shaqrastudentscontact.utils.SharedPrefManager;


public class StudentProfileFragment extends Fragment {


    Context ctx;
    TextView nameTV, typeTV, emailTV;
    Button editPassBtn;
    public NavController navController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    public StudentProfileFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPrefManager sp = SharedPrefManager.getInstance(ctx);
        Student student = sp.getStudentData();

        nameTV = view.findViewById(R.id.student_name);
        typeTV = view.findViewById(R.id.type);
        emailTV = view.findViewById(R.id.email);
        editPassBtn = view.findViewById(R.id.edit_password);

        nameTV.setText(student.getName());
        typeTV.setText(String.valueOf(student.getType()));
        emailTV.setText(student.getEmail());
        editPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_Profile_to_EditPassFragment);
            }
        });
    }
}