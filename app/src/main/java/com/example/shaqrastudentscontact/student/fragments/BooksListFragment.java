package com.example.shaqrastudentscontact.student.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.Book;
import com.example.shaqrastudentscontact.student.adapters.BooksListAdapter;
import com.example.shaqrastudentscontact.utils.Urls;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class BooksListFragment extends Fragment {

    Context context;
    RecyclerView mList;
    BooksListAdapter mAdapter;
    ArrayList<Book> list;
    ProgressDialog pDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public BooksListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_students_books_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = view.findViewById(R.id.rv);
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);
        getBooks();
    }

    private void getBooks(){
        String url = Urls.GET_BOOKS;
        pDialog.show();
        list = new ArrayList<Book>();
        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String messageGot = "founded";
                            String message = response.getString("message");
                            if (message.toLowerCase().contains(messageGot.toLowerCase())) {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    list.add(
                                            new Book(
                                                    Integer.parseInt(obj.getString("id")),
                                                    obj.getString("url"),
                                                    obj.getString("title")
                                            )
                                    );
                                }
                                mAdapter = new BooksListAdapter(context, list);
                                mList.setAdapter(mAdapter);
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            Log.e("books catch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        Log.e("books anerror", error.getErrorBody());
                        Log.e("books anerror", error.getErrorDetail());
                    }
                });
    }
}