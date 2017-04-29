package com.example.vrncthea.iloopv2_v2_magnofresco.OrgPages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.vrncthea.iloopv2_v2_magnofresco.AdminPages.CreateActivity;
import com.example.vrncthea.iloopv2_v2_magnofresco.R;

public class OrgViewActivities extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_view_activities);
    }

    public void goToAllActivitiesPage (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), CreateActivity.class);

        startActivity(myIntent);
    }

    public void goToApprovedPage (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), OrgApprovedActivities.class);

        startActivity(myIntent);
    }

    public void goToPendingPage (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), OrgPendingActivities.class);

        startActivity(myIntent);
    }

    public void gotToDeclinedPage (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), OrgDeclinedActivities.class);

        startActivity(myIntent);
    }

    public void goBack (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), OrgHomePage.class);

        startActivity(myIntent);
    }
}
