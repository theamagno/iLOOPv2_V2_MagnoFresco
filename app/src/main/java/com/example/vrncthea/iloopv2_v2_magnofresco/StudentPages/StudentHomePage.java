package com.example.vrncthea.iloopv2_v2_magnofresco.StudentPages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.vrncthea.iloopv2_v2_magnofresco.AdminPages.Announcements;
import com.example.vrncthea.iloopv2_v2_magnofresco.AdminPages.ApprovedActivities;
import com.example.vrncthea.iloopv2_v2_magnofresco.AdminPages.ChooseActivities;
import com.example.vrncthea.iloopv2_v2_magnofresco.AdminPages.ChooseUsers;
import com.example.vrncthea.iloopv2_v2_magnofresco.LoginPage;
import com.example.vrncthea.iloopv2_v2_magnofresco.R;
import com.example.vrncthea.iloopv2_v2_magnofresco.SessionManager;

import java.util.HashMap;

public class StudentHomePage extends AppCompatActivity {

//    SessionManager session;
//    private HashMap<String, String> userSessType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home_page);

//        session = new SessionManager(getApplicationContext());

//        // get user data from session
//        HashMap<String, String> user = session.getLoginSession();
//
//        // username
//        String username = user.get(SessionManager.KEY_SESSUSERNAME);
//
//        Toast.makeText(getApplicationContext(), "Welcome, " + username, Toast.LENGTH_LONG).show();
    }

    public void goToAnnouncementsPage (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), StudAnnouncements.class);

        startActivity(myIntent);
    }


    public void goToActivitiesPage (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), StudActivities.class);

        startActivity(myIntent);
    }

    public void logOut (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), LoginPage.class);

        startActivity(myIntent);
    }
}
