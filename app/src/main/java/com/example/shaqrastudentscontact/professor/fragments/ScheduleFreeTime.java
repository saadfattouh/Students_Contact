package com.example.shaqrastudentscontact.professor.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.utils.Urls;
import com.example.shaqrastudentscontact.utils.Validation;

public class ScheduleFreeTime extends Fragment {

    Context ctx;
    EditText mFromET, mToET;
    Button mScheduleBtn;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    public ScheduleFreeTime() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_professor_schedule_free_time, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFromET = view.findViewById(R.id.from);
        mToET = view.findViewById(R.id.to);
        mScheduleBtn = view.findViewById(R.id.schedule);

        mScheduleBtn.setOnClickListener(v -> {
                    if(Validation.validateInput(ctx, mFromET, mToET)){
                        String from = mFromET.getText().toString().trim();
                        String to = mToET.getText().toString().trim();

                        schedule(from, to);


                    }
        });
    }

    private void schedule(String from, String to) {
        String url = Urls.PROFESSOR_SCHEDULE;
    }
}