package com.example.shaqrastudentscontact.student.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.Professor;
import com.example.shaqrastudentscontact.models.Student;
import com.example.shaqrastudentscontact.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class HonorStudentsAdapter extends RecyclerView.Adapter<HonorStudentsAdapter.ViewHolder> {

    Context context;
    private List<Student> list;
    public NavController navController;

    // RecyclerView recyclerView;
    public HonorStudentsAdapter(Context context, ArrayList<Student> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HonorStudentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_honor_student, parent, false);
        HonorStudentsAdapter.ViewHolder viewHolder = new HonorStudentsAdapter.ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HonorStudentsAdapter.ViewHolder holder, int position) {

        Student student = list.get(position);
        holder.name.setText(student.getName());
        holder.email.setText(student.getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController = Navigation.findNavController(holder.itemView);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.HONOR_STUDENT_KEY, String.valueOf(student.getId()));
                bundle.putString(Constants.HONOR_STUDENT_NAME, student.getName());
                bundle.putString(Constants.HONOR_STUDENT_EMAIL, student.getEmail());
                navController.navigate(R.id.action_HonorStudent_to_HonorDetails, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, email;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.email = itemView.findViewById(R.id.email);
        }
    }
}

