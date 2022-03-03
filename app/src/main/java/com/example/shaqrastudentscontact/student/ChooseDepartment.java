package com.example.shaqrastudentscontact.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.student.adapters.DepartmentsAdapter;
import com.example.shaqrastudentscontact.models.Department;
import com.example.shaqrastudentscontact.utils.Urls;

import java.util.ArrayList;

public class ChooseDepartment extends AppCompatActivity {

    Toolbar mToolBar;

    DepartmentsAdapter mAdapter;
    RecyclerView mList;
    ArrayList<Department> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_choose_department);

        mToolBar = findViewById(R.id.toolbar);
        mList = findViewById(R.id.rv);

        setSupportActionBar(mToolBar);

        list = new ArrayList<Department>()
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


        mAdapter = new DepartmentsAdapter(this, list);
        mList.setAdapter(mAdapter);



    }

    //todo api call
    void getAllDepartments(){

        String url = Urls.GET_DEPARTMENTS;


    }
}