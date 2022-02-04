package com.example.shaqrastudentscontact.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shaqrastudentscontact.activities.student.StudentMain;
import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.Department;
import com.example.shaqrastudentscontact.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

public class DepartmentsAdapter extends RecyclerView.Adapter<DepartmentsAdapter.ViewHolder> {


    Context context;
    private List<Department> depts;

    // RecyclerView recyclerView;
    public DepartmentsAdapter(Context context, ArrayList<Department> depts) {
        this.context = context;
        this.depts = depts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.department_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Department dept = depts.get(position);

        holder.dept.setText(dept.getDeptName());

        holder.dept.setOnClickListener(v -> {
            SharedPrefManager.getInstance(context).setSelectedDept(dept.getId());
            context.startActivity(new Intent(context, StudentMain.class));
        });


    }


    @Override
    public int getItemCount() {
        return depts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button dept;

        public ViewHolder(View itemView) {
            super(itemView);
            this.dept = itemView.findViewById(R.id.dept);
        }
    }


}