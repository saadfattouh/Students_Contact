package com.example.shaqrastudentscontact.activities.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.adapters.DepartmentsAdapter;
import com.example.shaqrastudentscontact.models.Department;

import java.util.ArrayList;

public class ChooseDepartment extends AppCompatActivity {

    Toolbar mToolBar;

    DepartmentsAdapter mAdapter;
    RecyclerView mDeptList;
    ArrayList<Department> depts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_choose_department);

        mToolBar = findViewById(R.id.toolbar);
        mDeptList = findViewById(R.id.rv);

        setSupportActionBar(mToolBar);

        depts = new ArrayList<Department>()
        {{
            add(new Department(1, "technology"));
            add(new Department(2,"hoqoqe"));
            add(new Department(3, "iqtisade"));
            add(new Department(1, "technology"));
            add(new Department(2,"hoqoqe"));
            add(new Department(3, "iqtisade"));
            add(new Department(1, "technology"));
            add(new Department(2,"hoqoqe"));
            add(new Department(3, "iqtisade"));
        }};


        mAdapter = new DepartmentsAdapter(this, depts);
        mDeptList.setAdapter(mAdapter);



    }

    //todo api call
    void getAllDepartments(){

    }
}