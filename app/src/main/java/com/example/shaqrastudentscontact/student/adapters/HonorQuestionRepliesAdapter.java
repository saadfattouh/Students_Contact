package com.example.shaqrastudentscontact.student.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.HonorStudentQuestion;
import com.example.shaqrastudentscontact.models.ProfessorQuestion;

import java.util.ArrayList;
import java.util.List;

public class HonorQuestionRepliesAdapter  extends RecyclerView.Adapter<HonorQuestionRepliesAdapter.ViewHolder>{


    Context context;
    private List<HonorStudentQuestion> list;
    public NavController navController;


    // RecyclerView recyclerView;
    public HonorQuestionRepliesAdapter(Context context, ArrayList<HonorStudentQuestion> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public HonorQuestionRepliesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_question_professor, parent, false);
        HonorQuestionRepliesAdapter.ViewHolder viewHolder = new HonorQuestionRepliesAdapter.ViewHolder(listItem);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull HonorQuestionRepliesAdapter.ViewHolder holder, int position) {

        HonorStudentQuestion question = list.get(position);

        holder.title.setText(question.getTitle());
        holder.details.setText(question.getContent());

        holder.itemView.setOnClickListener(v -> {
            LayoutInflater factory = LayoutInflater.from(context);
            final View view = factory.inflate(R.layout.dialog_question_reply, null);
            final AlertDialog questionReplyDialog = new AlertDialog.Builder(context).create();
            questionReplyDialog.setView(view);
            questionReplyDialog.setCanceledOnTouchOutside(true);

            TextView title = view.findViewById(R.id.title);
            TextView details = view.findViewById(R.id.question);
            TextView reply = view.findViewById(R.id.answer);

            title.setText(question.getTitle());
            details.setText(question.getContent());
            if(question.getAnswer() != null || question.getAnswer().isEmpty()){
                reply.setText(question.getAnswer());
            }else {
                reply.setText(context.getResources().getString(R.string.no_answer_yet));
            }

            questionReplyDialog.show();
        });



    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, details;

        public ViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.title);
            this.details = itemView.findViewById(R.id.question);

        }
    }


}