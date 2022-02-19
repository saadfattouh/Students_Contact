package com.example.shaqrastudentscontact.student.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.ProfessorQuestion;
import com.example.shaqrastudentscontact.models.Question;
import com.example.shaqrastudentscontact.professor.fragments.AnswerQuestionsFragment;
import com.example.shaqrastudentscontact.student.adapters.QuestionRepliesAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProfessorDetailsFragment extends Fragment {

    Context ctx;

    TextView askBtn;

    String professorId;

    NavController navController;


    RecyclerView mQuestionsList;
    QuestionRepliesAdapter mAdapter;
    ArrayList<ProfessorQuestion> questions;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    public ProfessorDetailsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            professorId = getArguments().getString("prof_id", null);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_professor_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        askBtn = view.findViewById(R.id.ask_btn);
        mQuestionsList = view.findViewById(R.id.rv);

        askBtn.setOnClickListener(v -> {

            navController = Navigation.findNavController(view);
            Bundle bundle = new Bundle();
            bundle.putString("prof_id", professorId);
            navController.navigate(R.id.action_profDetailsFragment_to_askProfessorFragment, bundle);

        });

        questions = new ArrayList<ProfessorQuestion>()
        {{
            add(new ProfessorQuestion(1, 2, 3, "I need help", "how to solve (1+1) equation in physics ?", "no comment !"));
            add(new ProfessorQuestion(1, 2, 3, "I need help", "how to solve (1+1) equation in physics ?", "no comment !"));
            add(new ProfessorQuestion(1, 2, 3, "I need help", "how to solve (1+1) equation in physics ?", "no comment !"));
            add(new ProfessorQuestion(1, 2, 3, "I need help", "how to solve (1+1) equation in physics ?", "no comment !"));
            add(new ProfessorQuestion(1, 2, 3, "I need help", "how to solve (1+1) equation in physics ?", "no comment !"));
            add(new ProfessorQuestion(1, 2, 3, "I need help", "how to solve (1+1) equation in physics ?", "no comment !"));
            add(new ProfessorQuestion(1, 2, 3, "I need help", "how to solve (1+1) equation in physics ?", "no comment !"));
            add(new ProfessorQuestion(1, 2, 3, "I need help", "how to solve (1+1) equation in physics ?", "no comment !"));
            add(new ProfessorQuestion(1, 2, 3, "I need help", "how to solve (1+1) equation in physics ?", "no comment !"));
            add(new ProfessorQuestion(1, 2, 3, "I need help", "how to solve (1+1) equation in physics ?", "no comment !"));

        }};

        mAdapter = new QuestionRepliesAdapter(ctx, questions);
        mQuestionsList.setAdapter(mAdapter);

    }
}