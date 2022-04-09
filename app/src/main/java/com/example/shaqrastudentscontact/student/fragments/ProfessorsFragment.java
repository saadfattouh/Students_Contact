package com.example.shaqrastudentscontact.student.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.shaqrastudentscontact.models.Professor;
import com.example.shaqrastudentscontact.models.Question;
import com.example.shaqrastudentscontact.student.adapters.CommunityAdapter;
import com.example.shaqrastudentscontact.student.adapters.ProfessorsAdapter;
import com.example.shaqrastudentscontact.utils.SharedPrefManager;
import com.example.shaqrastudentscontact.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfessorsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    Context context;
    ArrayList<Professor> list;
    RecyclerView mList;
    ProfessorsAdapter mAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SearchView searchView;

    private ProgressDialog pDialog;

    public ProfessorsFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_student_professors, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.secondary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            getProfs();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList = view.findViewById(R.id.rv);
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);

        searchView = view.findViewById(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void getProfs() {
        String url = Urls.GET_PROFESSORS;
        String deptId = String.valueOf(SharedPrefManager.getInstance(context).getSelectedDept());
        pDialog.show();
        list = new ArrayList<Professor>();
        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addQueryParameter("department_id", deptId)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String messageGot = "founded";
                            String message = response.getString("message");
                            if (message.toLowerCase().contains(messageGot.toLowerCase())) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    list.add(
                                            new Professor(
                                                    Integer.parseInt(obj.getString("id")),
                                                    obj.getString("name"),
                                                    obj.getString("department_id"),
                                                    obj.getString("specialization"),
                                                    obj.getString("email"),
                                                    obj.getString("start_free_time"),
                                                    obj.getString("end_free_time")
                                            )
                                    );
                                }
                                mAdapter = new ProfessorsAdapter(context, list);
                                mList.setAdapter(mAdapter);
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                            mSwipeRefreshLayout.setRefreshing(false);
                            pDialog.dismiss();
                        } catch (Exception e) {
                            mSwipeRefreshLayout.setRefreshing(false);
                            pDialog.dismiss();
                            e.printStackTrace();
                            Log.e("profList catch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        pDialog.dismiss();
                        Log.e("profList anerror", error.getErrorBody());
                    }
                });
    }

    @Override
    public void onRefresh() {
        getProfs();
    }
}

