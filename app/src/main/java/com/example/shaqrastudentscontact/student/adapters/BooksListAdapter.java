package com.example.shaqrastudentscontact.student.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.Book;

import java.util.ArrayList;
import java.util.List;

public class BooksListAdapter extends RecyclerView.Adapter<BooksListAdapter.ViewHolder> implements Filterable {

    Context context;
    private List<Book> list;
    private List<Book> filtredBooks;
    public NavController navController;


    // RecyclerView recyclerView;
    public BooksListAdapter(Context context, ArrayList<Book> list) {
        this.context = context;
        this.list = list;
        this.filtredBooks = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_book, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Book item = list.get(position);

        holder.title.setText(item.getTitle());

        holder.itemView.setOnClickListener(v -> {
            navController = Navigation.findNavController(holder.itemView);
            Bundle bundle = new Bundle();
            bundle.putInt("book_id", item.getId());
            bundle.putString("title", item.getTitle());
            bundle.putString("url", item.getPdf());
            navController.navigate(R.id.action_Books_to_BookViewFragment,bundle);
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

                if(charSequence == null | charSequence.length() == 0){
                    filterResults.count = filtredBooks.size();
                    filterResults.values = filtredBooks;
                }else{
                    String searchChr = charSequence.toString().toLowerCase();
                    List<Book> resultData = new ArrayList<>();

                    for(Book book: filtredBooks){
                        if(book.getTitle().toLowerCase().contains(searchChr)){
                            resultData.add(book);
                        }
                    }
                    filterResults.count = resultData.size();
                    filterResults.values = resultData;
                }
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (List<Book>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.tv_title);
        }
    }


}