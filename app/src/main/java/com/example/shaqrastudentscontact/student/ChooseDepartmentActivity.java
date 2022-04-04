package com.example.shaqrastudentscontact.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.activities.Register;
import com.example.shaqrastudentscontact.models.Professor;
import com.example.shaqrastudentscontact.models.Student;
import com.example.shaqrastudentscontact.professor.ProfessorMain;
import com.example.shaqrastudentscontact.student.adapters.DepartmentsAdapter;
import com.example.shaqrastudentscontact.models.Department;
import com.example.shaqrastudentscontact.utils.Constants;
import com.example.shaqrastudentscontact.utils.SharedPrefManager;
import com.example.shaqrastudentscontact.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChooseDepartmentActivity extends AppCompatActivity implements DepartmentsAdapter.OnItemClickListener{

    Toolbar mToolBar;

    DepartmentsAdapter mAdapter;
    RecyclerView mList;
    ArrayList<Department> list;
    String profName,profEmail, profPass, profSpec;
    int profDeptId;
    boolean fromProf = false;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_choose_department);

        mToolBar = findViewById(R.id.toolbar);
        mList = findViewById(R.id.rv);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);
        setSupportActionBar(mToolBar);
        getAllDepartments();
        list.add(
            new Department(1, "technology")
       );
        list.add(
                new Department(1, "Computer science" )
        );
        Intent i = getIntent();

        if (i.getIntExtra(Constants.FROM,-1) == Constants.USER_TYPE_PROFESSOR){
            profName = i.getStringExtra(Constants.PROFESSOR_NAME);
            profEmail= i.getStringExtra(Constants.PROFESSOR_EMAIL);
            profPass = i.getStringExtra(Constants.PROFESSOR_PASS);
            profSpec = i.getStringExtra(Constants.PROFESSOR_SPEC);
            Log.e("fronProf", fromProf + "");
            fromProf = true;
        }else{
            fromProf = false;
        }
    }

    void getAllDepartments(){
        String url = Urls.GET_DEPARTMENTS;
        pDialog.show();
        list = new ArrayList<Department>();
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
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    list.add(
                                            new Department(
                                                    Integer.parseInt(obj.getString("id")),
                                                    obj.getString("name")
                                            )
                                    );
                                }
                                mAdapter = new DepartmentsAdapter(ChooseDepartmentActivity.this, list, ChooseDepartmentActivity.this);
                                mList.setAdapter(mAdapter);
                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            Log.e("depts catch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        Log.e("depts anerror", error.getErrorBody());
                    }
                });
    }

    void registerProf(int dept_id){
        pDialog.show();

        String url = Urls.REGISTER_PROFESSOR_URL;
        AndroidNetworking.post(url)
                .addBodyParameter("name", profName)
                .addBodyParameter("email", profEmail)
                .addBodyParameter("password", profPass)
                .addBodyParameter("'specialization' ", profSpec)
                .addBodyParameter("'department_id' ", String.valueOf(dept_id))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String userFounded = "User founded";
                            //if no error in response
                            if (message.toLowerCase().contains(userFounded.toLowerCase())) {

                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("data");
                                Professor professor;
                                String deptName = "";
                                for(Department dept :list){
                                    if(dept.getId() == dept_id){
                                        deptName = dept.getDeptName();
                                    }
                                }
                                professor = new Professor(
                                        Integer.parseInt(userJson.getString("id")),
                                        userJson.getString("name"),
                                        deptName,
                                        userJson.getString("specialization"),
                                        userJson.getString("email")

                                );
                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).professorLogin(professor);
                                goToProfMain();

                            }
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("regprof catch", e.getMessage());
                            pDialog.dismiss();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        Log.e("regiproferror", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(ChooseDepartmentActivity.this, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("name")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("name").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("email")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("email").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("password")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("password").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("specialization")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("specialization").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("dept_id")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("dept_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void goToProfMain() {
        startActivity(new Intent(ChooseDepartmentActivity.this, ProfessorMain.class));
        finish();
    }
    @Override
    public void onItemClick(Department item) {
        if(fromProf){
            profDeptId = item.getId();
            registerProf(profDeptId);
        }else{
            SharedPrefManager.getInstance(ChooseDepartmentActivity.this).setSelectedDept(item.getId());
            startActivity(new Intent(ChooseDepartmentActivity.this, StudentMain.class));
            finish();
        }
    }
}