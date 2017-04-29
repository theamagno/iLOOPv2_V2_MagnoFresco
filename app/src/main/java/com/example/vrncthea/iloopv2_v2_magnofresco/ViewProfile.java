package com.example.vrncthea.iloopv2_v2_magnofresco;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.vrncthea.iloopv2_v2_magnofresco.AdminPages.AdminHomePage;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewProfile extends AppCompatActivity {

    TextView displayName;
    TextView displayUsername;
    TextView displayAccountType;
    String json = "";
    String usernameSess = "";
    SessionManager session;
    private HashMap<String, String> userSessType;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private static final String url_profile_details = "https://theamagno.000webhostapp.com/php/get_profile_details.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USER = "user";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_PASSWORD= "password";
    private static final String TAG_ACCOUNTTYPE = "accounttype";
    private static final String TAG_ACCOUNTSTATUS= "status";
    private static final String TAG_USERNAMESESS = "usernameSess";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        session = new SessionManager(getApplicationContext());

        // get user data from session
        HashMap<String, String> userSess = session.getLoginSession();

        // username
        String username = userSess.get(SessionManager.KEY_SESSUSERNAME);

        new GetProfileDetails().execute(username);

    }

    class GetProfileDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewProfile.this);
            pDialog.setMessage("Loading user details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(final String... args) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {

                        String username = args[0];

                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair(TAG_USERNAME, username));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_profile_details, "GET", params);

                        // check your log for json response
                        Log.d("Single User Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray userObj = json
                                    .getJSONArray(TAG_USER); // JSON Array

                            // get first product object from JSON Array
                            JSONObject user = userObj.getJSONObject(0);

                            displayName = (TextView) findViewById(R.id.displayName);
                            displayUsername = (TextView) findViewById(R.id.displayUsername);
                            displayAccountType = (TextView) findViewById(R.id.displayAccountType);

                            displayName.setText(user.getString(TAG_NAME));
                            displayUsername.setText(user.getString(TAG_USERNAME));
                            displayAccountType.setText(user.getString(TAG_ACCOUNTTYPE));

                        }else{
                            // profile with username not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }

    public void goBack (View buttonControl)
    {
        finish();
    }
}
