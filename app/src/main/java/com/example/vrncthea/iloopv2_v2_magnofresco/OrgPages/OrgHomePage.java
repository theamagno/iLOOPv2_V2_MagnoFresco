package com.example.vrncthea.iloopv2_v2_magnofresco.OrgPages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.vrncthea.iloopv2_v2_magnofresco.AdminPages.Announcements;
import com.example.vrncthea.iloopv2_v2_magnofresco.R;
import com.example.vrncthea.iloopv2_v2_magnofresco.SessionManager;
import com.example.vrncthea.iloopv2_v2_magnofresco.ViewProfile;

import java.util.HashMap;

public class OrgHomePage extends AppCompatActivity {

    SessionManager session;
    private HashMap<String, String> userSessType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_home_page);
        session = new SessionManager(getApplicationContext());

        // get user data from session
        HashMap<String, String> user = session.getLoginSession();

        // username
        String username = user.get(SessionManager.KEY_SESSUSERNAME);

        Toast.makeText(getApplicationContext(), "Welcome, " + username, Toast.LENGTH_LONG).show();

    }

    public void goToProfile (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), ViewProfile.class);

        startActivity(myIntent);
    }


    public void goToAnnouncements (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), Announcements.class);

        startActivity(myIntent);
    }

    public void goToActivities (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), OrgViewActivities.class);

        startActivity(myIntent);
    }

    public void logOut (View buttonControl)
    {
        session.logoutUser();
    }
}
