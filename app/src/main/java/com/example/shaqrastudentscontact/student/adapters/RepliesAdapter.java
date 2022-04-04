package com.example.shaqrastudentscontact.student.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.Question;
import com.example.shaqrastudentscontact.models.Reply;

import java.util.ArrayList;
import java.util.List;

public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.ViewHolder>{


    Context context;
    private List<Reply> list;


    // RecyclerView recyclerView;
    public RepliesAdapter(Context context, ArrayList<Reply> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public RepliesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_reply, parent, false);
        RepliesAdapter.ViewHolder viewHolder = new RepliesAdapter.ViewHolder(listItem);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RepliesAdapter.ViewHolder holder, int position) {

        Reply reply = list.get(position);

        holder.studentName.setText(reply.getStudentName());
        holder.reply.setText(reply.getReply());
        holder.date.setText(reply.getDate());



    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView studentName, reply, date;

        public ViewHolder(View itemView) {
            super(itemView);
            this.studentName = itemView.findViewById(R.id.student_name);
            this.reply = itemView.findViewById(R.id.reply);
            this.date = itemView.findViewById(R.id.date);

        }
    }


}



