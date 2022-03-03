package com.example.shaqrastudentscontact.honor_student.adapters;

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
import com.example.shaqrastudentscontact.models.Question;

import java.util.ArrayList;
import java.util.List;

public class HonorStudentRequestedQuestionsAdapter extends RecyclerView.Adapter<HonorStudentRequestedQuestionsAdapter.ViewHolder> {


    Context context;
    private List<Question> list;
    public NavController navController;


    // RecyclerView recyclerView;
    public HonorStudentRequestedQuestionsAdapter(Context context, ArrayList<Question> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public HonorStudentRequestedQuestionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_requested_question, parent, false);
        HonorStudentRequestedQuestionsAdapter.ViewHolder viewHolder = new HonorStudentRequestedQuestionsAdapter.ViewHolder(listItem);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull HonorStudentRequestedQuestionsAdapter.ViewHolder holder, int position) {

        Question question = list.get(position);


        holder.studentName.setText(question.getStudentName());
        holder.question.setText(question.getQuestion());
        holder.date.setText(question.getDate());
        holder.addReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController = Navigation.findNavController(holder.itemView);
                Bundle bundle = new Bundle();
                bundle.putString("question_id", String.valueOf(question.getId()));
                navController.navigate(R.id.action_menu_requested_questions_to_ReplyToQuestionFragment,bundle);
            }
        });

        holder.replies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView studentName, question,questionTitle, date, replies, addReply;

        public ViewHolder(View itemView) {
            super(itemView);
            this.studentName = itemView.findViewById(R.id.name);
            this.question = itemView.findViewById(R.id.question);
            this.questionTitle = itemView.findViewById(R.id.question_title);
            this.date = itemView.findViewById(R.id.date);
            this.addReply = itemView.findViewById(R.id.reply);
            this.replies = itemView.findViewById(R.id.replies);

        }
    }


}