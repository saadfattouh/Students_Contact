package com.example.shaqrastudentscontact.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;

import com.example.shaqrastudentscontact.R;
import com.example.shaqrastudentscontact.activities.Login;
import com.example.shaqrastudentscontact.utils.Constants;
import com.example.shaqrastudentscontact.utils.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;


public class StudentMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {

    public Toolbar toolbar;

    public DrawerLayout drawerLayout;

    public NavController navController;

    public NavigationView navigationView;

    int destination = R.id.menu_community;

    AppBarConfiguration mAppBarConfiguration;

    int accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        Log.e("dept", SharedPrefManager.getInstance(this).getSelectedDept() + "");
        accountType = SharedPrefManager.getInstance(this).getStudentType();

        setupNavigation(accountType);

    }

    private void setupNavigation(int accountType) {
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout = findViewById(R.id.my_drawer_layout);

        navigationView = findViewById(R.id.nav_view);

        if(accountType == Constants.STUDENT_TYPE_HONOR){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu_nav_honor_student);

            navController = Navigation.findNavController(this, R.id.nav_host_fragment);

            mAppBarConfiguration = new AppBarConfiguration.Builder( R.id.menu_community,R.id.menu_professors,R.id.menu_honor_students,R.id.menu_books, R.id.menu_student_profile, R.id.menu_requested_questions, R.id.menu_rules).setOpenableLayout(drawerLayout).build();

            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

            NavigationUI.setupWithNavController(navigationView, navController);

            navigationView.setNavigationItemSelectedListener(this);

            drawerLayout.addDrawerListener(this);
        }else if(accountType == Constants.STUDENT_TYPE_NORMAL){

            navigationView.getMenu().clear();

            navigationView.inflateMenu(R.menu.user_nav_menu);

            navController = Navigation.findNavController(this, R.id.nav_host_fragment);

            mAppBarConfiguration = new AppBarConfiguration.Builder( R.id.menu_community,R.id.menu_professors,R.id.menu_honor_students,R.id.menu_books, R.id.menu_student_profile).setOpenableLayout(drawerLayout).build();

            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

            NavigationUI.setupWithNavController(navigationView, navController);

            navigationView.setNavigationItemSelectedListener(this);

            drawerLayout.addDrawerListener(this);

        }



    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();
        navigationView.getMenu().findItem(id).setChecked(true);

        switch (id) {

            case R.id.menu_community:
                destination = R.id.menu_community;
                break;
            case R.id.menu_books:
                destination = R.id.menu_books;

                break;
            case R.id.menu_professors:
                destination = R.id.menu_professors;

                break;
            case R.id.menu_honor_students:
                destination = R.id.menu_honor_students;

                break;

            case R.id.menu_student_profile:
                destination = R.id.menu_student_profile;

                break;
            //for honor student
            case R.id.menu_requested_questions:
                destination = R.id.menu_requested_questions;
                break;
            case R.id.menu_rules:
                destination = R.id.menu_rules;
                break;
            case R.id.menu_logout:
                SharedPrefManager.getInstance(this).logout();
                startActivity(new Intent(this, Login.class));
                finish();
                break;
        }

        drawerLayout.closeDrawers();

        return true;
    }


    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {}

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        navController.navigate(destination);
    }

    @Override
    public void onDrawerStateChanged(int newState) {}
}