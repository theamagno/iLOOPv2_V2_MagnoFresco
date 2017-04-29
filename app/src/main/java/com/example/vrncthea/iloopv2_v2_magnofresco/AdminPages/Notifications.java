package com.example.vrncthea.iloopv2_v2_magnofresco.AdminPages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.vrncthea.iloopv2_v2_magnofresco.R;

public class Notifications extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
    }

    public void goBack (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), ChooseUsers.class);

        startActivity(myIntent);
    }

}
