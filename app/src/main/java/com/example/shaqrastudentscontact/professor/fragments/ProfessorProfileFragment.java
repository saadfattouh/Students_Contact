package com.example.shaqrastudentscontact.professor.fragments;

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

import com.androidnetworking.model.Progress;
import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.Professor;
import com.example.shaqrastudentscontact.utils.Constants;
import com.example.shaqrastudentscontact.utils.SharedPrefManager;

public class ProfessorProfileFragment extends Fragment {

    TextView name, department, email, specialization;
    Button editPass;
    Context ctx;
    public NavController navController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
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

        Professor prof = SharedPrefManager.getInstance(ctx).getProfessorData();
        name.setText(prof.getName());
        department.setText(prof.getDeptName());
        specialization.setText(prof.getSpecialization());
        email.setText(prof.getEmail());

        editPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController = Navigation.findNavController(v);
                Bundle bundle = new Bundle();
                bundle.putInt("type", Constants.USER_TYPE_PROFESSOR);
                navController.navigate(R.id.action_ProfessorProfile_to_EditPassFragment,bundle);
            }
        });

    }
}