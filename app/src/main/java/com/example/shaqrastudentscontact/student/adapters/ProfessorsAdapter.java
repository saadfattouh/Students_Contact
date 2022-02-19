package com.example.shaqrastudentscontact.student.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.Professor;

import java.util.ArrayList;
import java.util.List;

public class ProfessorsAdapter extends RecyclerView.Adapter<ProfessorsAdapter.ViewHolder> {


    Context context;
    private List<Professor> profs;
    public NavController navController;

    // RecyclerView recyclerView;
    public ProfessorsAdapter(Context context, ArrayList<Professor> profs) {
        this.context = context;
        this.profs = profs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_professor, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Professor prof = profs.get(position);
        holder.profName.setText(prof.getName());
        holder.profDept.setText(prof.getDeptName());
        holder.profSpec.setText(prof.getSpecialization());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController = Navigation.findNavController(holder.itemView);
                Bundle bundle = new Bundle();
                bundle.putString("prof_id", String.valueOf(prof.getId()));
                navController.navigate(R.id.action_profFragment_to_profDetailsFragment,bundle);
            }
        });


    }


    @Override
    public int getItemCount() {
        return profs.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView profName, profDept, profSpec;
        public ViewHolder(View itemView) {
            super(itemView);
            this.profName = itemView.findViewById(R.id.professor_name);
            this.profDept = itemView.findViewById(R.id.department);
            this.profSpec = itemView.findViewById(R.id.specialization);

        }
    }


}