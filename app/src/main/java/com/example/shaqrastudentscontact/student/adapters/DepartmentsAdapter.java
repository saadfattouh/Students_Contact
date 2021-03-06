package com.example.shaqrastudentscontact.student.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shaqrastudentscontact.student.StudentMain;
import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.Department;
import com.example.shaqrastudentscontact.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

public class DepartmentsAdapter extends RecyclerView.Adapter<DepartmentsAdapter.ViewHolder> {


    Context context;

    private List<Department> depts;
    private final  OnItemClickListener listener;

    // RecyclerView recyclerView;
    public DepartmentsAdapter(Context context, ArrayList<Department> depts, OnItemClickListener listener) {
        this.context = context;
        this.depts = depts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_department, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Department dept = depts.get(position);

        holder.dept.setText(dept.getDeptName());

        holder.dept.setOnClickListener(v -> {

            listener.onItemClick(dept);

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

    public interface OnItemClickListener {
        void onItemClick(Department item);
    }
}
