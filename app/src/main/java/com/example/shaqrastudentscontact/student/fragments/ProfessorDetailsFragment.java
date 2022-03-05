package com.example.shaqrastudentscontact.student.fragments;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.ProfessorQuestion;
import com.example.shaqrastudentscontact.student.adapters.QuestionRepliesAdapter;
import com.example.shaqrastudentscontact.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfessorDetailsFragment extends Fragment {

    Context ctx;

    TextView askBtn;

    String professorId;

    NavController navController;


    RecyclerView mQuestionsList;
    QuestionRepliesAdapter mAdapter;
    ArrayList<ProfessorQuestion> questions;
    ProgressDialog pDialog;


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
            add(new ProfessorQuestion(1, 2, 3, "how to solve (1+1) equation in physics ?", "no comment !"));
            add(new ProfessorQuestion(1, 2, 3, "how to solve (1+1) equation in physics ?", "no comment !"));
            add(new ProfessorQuestion(1, 2, 3, "how to solve (1+1) equation in physics ?", "no comment !"));
            add(new ProfessorQuestion(1, 2, 3, "how to solve (1+1) equation in physics ?", "no comment !"));
            add(new ProfessorQuestion(1, 2, 3, "how to solve (1+1) equation in physics ?", "no comment !"));
            add(new ProfessorQuestion(1, 2, 3, "how to solve (1+1) equation in physics ?", "no comment !"));
            add(new ProfessorQuestion(1, 2, 3, "how to solve (1+1) equation in physics ?", "no comment !"));
            add(new ProfessorQuestion(1, 2, 3, "how to solve (1+1) equation in physics ?", "no comment !"));
            add(new ProfessorQuestion(1, 2, 3, "how to solve (1+1) equation in physics ?", "no comment !"));
            add(new ProfessorQuestion(1, 2, 3, "how to solve (1+1) equation in physics ?", "no comment !"));

        }};

        mAdapter = new QuestionRepliesAdapter(ctx, questions);
        mQuestionsList.setAdapter(mAdapter);

    }

    private void getQuestionsToProf(){
        String url = Urls.GET_QUESTIONS_TO_PROF;
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        AndroidNetworking.post(url).setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        pDialog.dismiss();
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            //if no error in response
                            if (obj.getInt("status") == 1) {
//                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//
//                                //getting the user from the response
//                                JSONObject userJson = obj.getJSONObject("data");
//                                User user;
//                                SharedPrefManager.getInstance(getApplicationContext()).setUserType(Constants.USER);
//                                user = new User(
//                                        Integer.parseInt(userJson.getString("id")),
//                                        userJson.getString("name"),
//                                        "+966 "+userJson.getString("email")
//                                );
//
//                                //storing the user in shared preferences
//                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
//                                goToUserMainActivity();
//                                finish();
//
//                                mRegisterBtn.setEnabled(true);
//                            } else if(obj.getInt("status") == -1){
//                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                                mRegisterBtn.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        Toast.makeText(ctx, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}