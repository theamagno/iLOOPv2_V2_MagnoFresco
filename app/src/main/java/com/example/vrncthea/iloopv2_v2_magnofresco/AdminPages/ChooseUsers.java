package com.example.vrncthea.iloopv2_v2_magnofresco.AdminPages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.vrncthea.iloopv2_v2_magnofresco.R;

public class ChooseUsers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_users);
    }

    public void createUser (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), CreateUser.class);

        startActivity(myIntent);
    }

    public void goToAllUsersPage (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), AllUsers.class);

        startActivity(myIntent);
    }

    public void gotToAdminsPage (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), AdminUsers.class);

        startActivity(myIntent);
    }

    public void goToOrgsPages (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), OrgUsers.class);

        startActivity(myIntent);
    }

    public void goBack (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), AdminHomePage.class);

        startActivity(myIntent);
    }


}
