package com.example.shaqrastudentscontact.student.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.Question;
import com.example.shaqrastudentscontact.models.Reply;
import com.example.shaqrastudentscontact.student.adapters.CommunityAdapter;
import com.example.shaqrastudentscontact.student.adapters.RepliesAdapter;

import java.util.ArrayList;

public class RepliesFragment extends Fragment {
    EditText replyET;
    ImageView replyBtn;
    Context ctx;

    RecyclerView mList;
    RepliesAdapter mAdapter;
    public RepliesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_replies, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        replyET = view.findViewById(R.id.reply_et);
        replyBtn = view.findViewById(R.id.reply_btn);

        mList = view.findViewById(R.id.replies_list);


        ArrayList<Reply> list = new ArrayList<Reply>(){{
            add(new Reply(1,"student","what is this ?", "12-12-2022",true ));
            add(new Reply(1,"student","what is this ?", "12-12-2022",false ));
            add(new Reply(1,"student","what is this ?", "12-12-2022",true ));
            add(new Reply(1,"student","what is  ?", "12-12-2022",false ));
            add(new Reply(1,"student","how is this ?", "12-12-2022",true ));
            add(new Reply(1,"student","what is this ?", "12-12-2022",false ));
            add(new Reply(1,"student","why is this ?", "12-12-2022",true ));
        }};
        mAdapter = new RepliesAdapter(ctx, list);

        mList.setAdapter(mAdapter);
        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = replyET.getText().toString().trim();
                if(question.isEmpty()){
                    Toast.makeText(ctx, ctx.getResources().getString(R.string.you_cant_post_empty_reply), Toast.LENGTH_SHORT).show();
                }else{
                    postQuestion(question);
                }
            }
        });

    }

    private void postQuestion(String question) {


    }
}