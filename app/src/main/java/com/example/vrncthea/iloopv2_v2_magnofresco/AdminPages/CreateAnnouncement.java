package com.example.vrncthea.iloopv2_v2_magnofresco.AdminPages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.vrncthea.iloopv2_v2_magnofresco.JSONParser;
import com.example.vrncthea.iloopv2_v2_magnofresco.R;
import com.example.vrncthea.iloopv2_v2_magnofresco.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateAnnouncement extends AppCompatActivity {

    SessionManager session;
    private HashMap<String, String> userSessType;

    // Progress Dialog
    JSONParser jsonParser = new JSONParser();
    EditText inputAnnouncementTitle;
    EditText inputAnnouncementDetails;
    String json = "";
    private ProgressDialog pDialog;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    private static final String url_create_announcement = "https://theamagno.000webhostapp.com/php/create_announcement.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_announcement);

        session = new SessionManager(getApplicationContext());

        // get user data from session
        HashMap<String, String> user = session.getLoginSession();

        // username
        String username = user.get(SessionManager.KEY_SESSUSERNAME);

        inputAnnouncementTitle = (EditText)findViewById(R.id.createAnnouncementTitle);
        inputAnnouncementDetails = (EditText)findViewById(R.id.createAnnouncementDetails);

        Button createAnnouncementButton = (Button) findViewById(R.id.createAnnouncementButton);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // button click event
        createAnnouncementButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread

                session = new SessionManager(getApplicationContext());

                // get user data from session
                HashMap<String, String> user = session.getLoginSession();

                // username
                String username = user.get(SessionManager.KEY_SESSUSERNAME);

                String title = inputAnnouncementTitle.getText().toString();
                String details = inputAnnouncementDetails.getText().toString();
                String postedby = user.get(SessionManager.KEY_SESSUSERNAME);

                if(title.trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please input name.", Toast.LENGTH_LONG).show();
                }
                else if(details.trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please input username.", Toast.LENGTH_LONG).show();
                }
                else
                {

                    System.out.println(title);
                    System.out.println(details);
                    System.out.println(postedby);

                    new CreateNewAnnouncement().execute(title, details, postedby);
                }

            }
        });
    }

    /**
     * Background Async Task to Create new product
     * */
    class CreateNewAnnouncement extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateAnnouncement.this);
            pDialog.setMessage("Creating Announcement..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            String title = args[0];
            String details = args[1];
            String postedby = args[2];

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("title", title));
            params.add(new BasicNameValuePair("details", details));
            params.add(new BasicNameValuePair("postedby", postedby));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_announcement,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), Announcements.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }

    public void goBack (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), Announcements.class);

        startActivity(myIntent);
    }

}
