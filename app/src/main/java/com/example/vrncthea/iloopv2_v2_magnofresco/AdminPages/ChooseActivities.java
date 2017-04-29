package com.example.vrncthea.iloopv2_v2_magnofresco.AdminPages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.vrncthea.iloopv2_v2_magnofresco.R;

public class ChooseActivities extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_activities);
    }

    public void goToAllActivitiesPage (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), Activities.class);

        startActivity(myIntent);
    }

    public void goToApprovedPage (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), ApprovedActivities.class);

        startActivity(myIntent);
    }

    public void goToPendingPage (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), PendingActivities.class);

        startActivity(myIntent);
    }

    public void gotToDeclinedPage (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), DeclinedActivities.class);

        startActivity(myIntent);
    }

    public void goBack (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), AdminHomePage.class);

        startActivity(myIntent);
    }

}
