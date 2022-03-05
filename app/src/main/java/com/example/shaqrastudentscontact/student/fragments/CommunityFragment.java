package com.example.shaqrastudentscontact.student.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.Image;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.models.Question;
import com.example.shaqrastudentscontact.student.adapters.CommunityAdapter;
import com.example.shaqrastudentscontact.utils.SharedPrefManager;
import com.example.shaqrastudentscontact.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CommunityFragment extends Fragment {

    EditText questionET;
    ImageView postQuestionBtn;
    Context ctx;

    RadioGroup contentTypeSelector;

    RecyclerView mList;
    CommunityAdapter mAdapter;

    ProgressDialog pDialog;

    public CommunityFragment() {
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
        return inflater.inflate(R.layout.fragment_student_community, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        questionET = view.findViewById(R.id.question_et);
        postQuestionBtn = view.findViewById(R.id.question_btn);
        mList = view.findViewById(R.id.rv);
        contentTypeSelector = view.findViewById(R.id.questions_type_selector);

        ArrayList<Question> list = new ArrayList<Question>(){{
            add(new Question(1,"student","what is this ?", "12-12-2022",true ));
            add(new Question(1,"student","what is this ?", "12-12-2022",false ));
            add(new Question(1,"student","what is this ?", "12-12-2022",true ));
            add(new Question(1,"student","what is  ?", "12-12-2022",false ));
            add(new Question(1,"student","how is this ?", "12-12-2022",true ));
            add(new Question(1,"student","what is this ?", "12-12-2022",false ));
            add(new Question(1,"student","why is this ?", "12-12-2022",true ));
        }};
        mAdapter = new CommunityAdapter(ctx, list);

        mList.setAdapter(mAdapter);
        postQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = questionET.getText().toString().trim();
                if(question.isEmpty()){
                    Toast.makeText(ctx, ctx.getResources().getString(R.string.you_cant_post_empty_question), Toast.LENGTH_SHORT).show();
                }else{
                    postQuestion(question);
                }
            }
        });

        contentTypeSelector.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton allBtn = view.findViewById(R.id.all);
            RadioButton commonBtn = view.findViewById(R.id.common);

            switch (checkedId){
                case R.id.all:
                    allBtn.setTextColor(getResources().getColor(R.color.white));
                    commonBtn.setTextColor(getResources().getColor(R.color.black));
                    mAdapter.getFilter().filter("");
                    break;
                case R.id.common:
                    commonBtn.setTextColor(getResources().getColor(R.color.white));
                    allBtn.setTextColor(getResources().getColor(R.color.black));
                    mAdapter.getFilter().filter("common");
                    break;
            }
        });

    }

    private void postQuestion(String question) {
        String url = Urls.POST_COMMUNITY_QUESTION;
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        int id = SharedPrefManager.getInstance(ctx).getUserId();

        AndroidNetworking.post(url).setPriority(Priority.MEDIUM)
                .addBodyParameter("id",String.valueOf(id))
                .addBodyParameter("question",question)
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