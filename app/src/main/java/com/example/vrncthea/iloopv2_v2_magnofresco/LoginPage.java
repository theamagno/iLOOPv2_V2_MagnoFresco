package com.example.vrncthea.iloopv2_v2_magnofresco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vrncthea.iloopv2_v2_magnofresco.AdminPages.AdminHomePage;
import com.example.vrncthea.iloopv2_v2_magnofresco.AdminPages.CreateUser;
import com.example.vrncthea.iloopv2_v2_magnofresco.OrgPages.OrgHomePage;
import com.example.vrncthea.iloopv2_v2_magnofresco.StudentPages.StudentHomePage;

import java.util.HashMap;

public class LoginPage extends Activity {

    private static final String TAG = CreateUser.class.getSimpleName();
    private Button btnLogin;
    EditText inputUsername;
    EditText inputPassword;
    private ProgressDialog pDialog;
    SessionManager session;
    private HashMap<String, String> userSessType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        session = new SessionManager(getApplicationContext());
    }

    public void LogIn (View buttonControl)
    {
        inputUsername = (EditText)findViewById(R.id.editText_USERNAME);
        inputPassword = (EditText)findViewById(R.id.editText_PASSWORD);

        String userType = inputUsername.getText().toString().substring(0,2);
        String username = inputUsername.getText().toString();
        String password = inputPassword.getText().toString();

        if(username.trim().length() > 0 && password.trim().length() > 0)
        {

            if (userType.equalsIgnoreCase("AD")) {
                session.createLoginSession(username);
                Intent myIntent = new Intent(this, AdminHomePage.class);
                session.setUserType("Administrator");
                startActivity(myIntent);
                finish();
            } else if (userType.equalsIgnoreCase("OR")){
                session.createLoginSession(username);
                Intent myIntent = new Intent(this, OrgHomePage.class);
                session.setUserType("Organization");
                startActivity(myIntent);
                finish();
            }

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please enter your username and password.", Toast.LENGTH_SHORT).show();
        }
    }

    public void LogInStudent (View buttonControl)
    {
        Intent myIntent = new Intent(this, StudentHomePage.class);
        startActivity(myIntent);
        finish();
    }
}
