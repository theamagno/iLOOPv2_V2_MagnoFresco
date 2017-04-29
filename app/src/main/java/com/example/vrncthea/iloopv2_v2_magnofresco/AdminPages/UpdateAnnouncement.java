package com.example.vrncthea.iloopv2_v2_magnofresco.AdminPages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpdateAnnouncement extends AppCompatActivity {

    EditText updateAnnouncementTitle;
    EditText updateAnnouncementDetails;
    String json = "";
    String pid;
    Button btnUpdate;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single product url
    private static final String url_announcement_details = "https://theamagno.000webhostapp.com/php/get_announcement_details.php";

    // url to update product
    private static final String url_update_announcement = "https://theamagno.000webhostapp.com/php/update_announcement.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ANNOUNCEMENTS = "announcements";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DETAILS = "details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_announcement);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnUpdate = (Button) findViewById(R.id.updateAnnouncementButton);

        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        pid = i.getStringExtra(TAG_ID);

        // Getting complete product details in background thread
        new GetAnnouncementDetails().execute();

        updateAnnouncementTitle = (EditText)findViewById(R.id.updateAnnouncementTitle);
        updateAnnouncementDetails = (EditText)findViewById(R.id.updateAnnouncementDetails);

        // save button click event
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // getting updated data from EditTexts
                String title = updateAnnouncementTitle.getText().toString();
                String details = updateAnnouncementDetails.getText().toString();

                if(title.trim().equals(""))
                {

                    Toast.makeText(getApplicationContext(), "Please enter a title.", Toast.LENGTH_LONG).show();
                }
                else if(details.trim().equals(""))
                {

                    Toast.makeText(getApplicationContext(), "Please enter announcement details.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    new UpdateAnnouncement.SaveAnnouncementDetails().execute(title, details);
                }
            }
        });
    }

    /**
     * Background Async Task to Get complete product details
     * */
    class GetAnnouncementDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdateAnnouncement.this);
            pDialog.setMessage("Loading announcement details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair(TAG_ID, pid));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_announcement_details, "GET", params);

                        // check your log for json response
                        Log.d("Announcement Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray userObj = json
                                    .getJSONArray(TAG_ANNOUNCEMENTS); // JSON Array

                            // get first product object from JSON Array
                            JSONObject user = userObj.getJSONObject(0);

                            // product with this pid found
                            // Edit Text
                            updateAnnouncementTitle = (EditText)findViewById(R.id.updateAnnouncementTitle);
                            updateAnnouncementDetails = (EditText)findViewById(R.id.updateAnnouncementDetails);

                            // display product data in EditText
                            updateAnnouncementTitle.setText(user.getString(TAG_TITLE));
                            updateAnnouncementDetails.setText(user.getString(TAG_DETAILS));

                        }else{
                            // product with pid not found
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

    /**
     * Background Async Task to  Save product Details
     * */
    class SaveAnnouncementDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdateAnnouncement.this);
            pDialog.setMessage("Saving user ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {

            String title = args[0];
            String details = args[1];

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", pid));
            params.add(new BasicNameValuePair("title", title));
            params.add(new BasicNameValuePair("details", details));

            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_announcement,
                    "POST", params);

            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about product update
                    setResult(100, i);
                    finish();
                } else {
                    // failed to update product
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
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
        }
    }
    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(String file_url) {
        // dismiss the dialog once product deleted
        pDialog.dismiss();

    }

    public void goBack (View buttonControl)
    {
        this.finish();
    }

}

