package com.example.shaqrastudentscontact.student.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.Question;

import java.util.ArrayList;
import java.util.List;

public class CommunityAdapter  extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> implements Filterable {


    Context context;
    private List<Question> list;
    private List<Question> filteredList;
    public NavController navController;


    // RecyclerView recyclerView;
    public CommunityAdapter(Context context, ArrayList<Question> list) {
        this.context = context;
        this.list = list;
        this.filteredList = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_question, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Question question = list.get(position);


        holder.studentName.setText(question.getStudentName());
        holder.question.setText(question.getQuestion());
        holder.date.setText(question.getDate());
        holder.addReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: go to replies
                navController = Navigation.findNavController(holder.itemView);
                Bundle bundle = new Bundle();
                bundle.putString("question_id", String.valueOf(question.getId()));
                navController.navigate(R.id.action_communityFragment_to_repliesFragment,bundle);
            }
        });

        holder.replies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController = Navigation.findNavController(holder.itemView);
                Bundle bundle = new Bundle();
                bundle.putString("question_id", String.valueOf(question.getId()));
                navController.navigate(R.id.action_communityFragment_to_repliesFragment,bundle);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                if (charSequence == null | charSequence.length() == 0) {
                    filterResults.count = filteredList.size();
                    filterResults.values = filteredList;

                } else {
                    String search = charSequence.toString().toLowerCase();

                    List<Question> resultData = new ArrayList<>();

                    if (search.equals("common")) {
                        for (Question question : list) {
                            if (question.isCommon())
                                resultData.add(question);
                        }
                        filterResults.count = resultData.size();
                        filterResults.values = resultData;
                    }
                }
                return filterResults;
            }

                @Override
                protected void publishResults (CharSequence charSequence, FilterResults
                filterResults){
                    list = (List<Question>) filterResults.values;
                    notifyDataSetChanged();
                }
        };

        return filter;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView studentName, question, date, replies, addReply;

        public ViewHolder(View itemView) {
            super(itemView);
            this.studentName = itemView.findViewById(R.id.name);
            this.question = itemView.findViewById(R.id.question);
            this.date = itemView.findViewById(R.id.date);
            this.addReply = itemView.findViewById(R.id.reply);
            this.replies = itemView.findViewById(R.id.replies);

        }
    }


}


