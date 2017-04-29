package com.example.vrncthea.iloopv2_v2_magnofresco.AdminPages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

public class UpdateActivity extends AppCompatActivity {

    EditText updateTitle;
    EditText updateDetails;
    Spinner updateLocation;
    EditText updateEmail;
    private RadioGroup activityStatusGroup;
    private RadioButton activityStatus;
    String json = "";
    String pid;
    Button btnUpdate;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single product url
    private static final String url_activity_details = "https://theamagno.000webhostapp.com/php/get_activity_details.php";

    // url to update product
    private static final String url_update_activity = "https://theamagno.000webhostapp.com/php/update_activity.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ACTIVITIES = "activities";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DETAILS = "details";
    private static final String TAG_CHOSENLOCATION = "chosenlocation";
    private static final String TAG_STATUS= "status";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // save button
        btnUpdate = (Button) findViewById(R.id.updateActivitytButton);

        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        pid = i.getStringExtra(TAG_ID);

        // Getting complete product details in background thread
        new GetActivityDetails().execute();

        updateTitle = (EditText)findViewById(R.id.updateActivityTitle);
        updateDetails = (EditText)findViewById(R.id.updateActivityDetails);

        Spinner chooseLocation = (Spinner) findViewById(R.id.activityLocation_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.location_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        chooseLocation.setAdapter(adapter);

        chooseLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        // save button click event
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // RADIO BUTTON
                activityStatusGroup = (RadioGroup) findViewById(R.id.activityStatusGroup);
                // get selected radio button from radioGroup
                int selectedActivityStatus = activityStatusGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                activityStatus = (RadioButton) findViewById(selectedActivityStatus);

                Spinner locationSpinner = (Spinner) findViewById(R.id.activityLocation_spinner);
                String text = locationSpinner.getSelectedItem().toString();

                // getting updated data from EditTexts
                String title = updateTitle.getText().toString();
                String details = updateDetails.getText().toString();
                String chosenLocation = locationSpinner.getSelectedItem().toString();
                String status = activityStatus.getText().toString();

                if(title.trim().equals(""))
                {

                    Toast.makeText(getApplicationContext(), "Please input title.", Toast.LENGTH_LONG).show();
                }
                else if (details.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please input details.", Toast.LENGTH_LONG).show();
                }
                else if(status.trim().equals(""))
                {

                    Toast.makeText(getApplicationContext(), "Please choose activity status.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    new SaveUserDetails().execute(title, details, chosenLocation, status);
                }
            }
        });

    }

    /**
     * Background Async Task to Get complete product details
     * */
    class GetActivityDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdateActivity.this);
            pDialog.setMessage("Loading activity details. Please wait...");
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
                                url_activity_details, "GET", params);

                        // check your log for json response
                        Log.d("Single Activity Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray userObj = json
                                    .getJSONArray(TAG_ACTIVITIES); // JSON Array

                            // get first product object from JSON Array
                            JSONObject activity = userObj.getJSONObject(0);

                            // product with this pid found
                            // Edit Text
                            updateTitle = (EditText)findViewById(R.id.updateActivityTitle);
                            updateDetails = (EditText)findViewById(R.id.updateActivityDetails);

                            activityStatusGroup = (RadioGroup) findViewById(R.id.activityStatusGroup);
                            RadioButton activityStatus_Approved = (RadioButton) findViewById(R.id.activityStatus_Approved);
                            RadioButton activityStatus_Declined = (RadioButton) findViewById(R.id.activityStatus_Declined);

                            // display product data in EditText
                            updateTitle.setText(activity.getString(TAG_TITLE));
                            updateDetails.setText(activity.getString(TAG_DETAILS));

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
    class SaveUserDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdateActivity.this);
            pDialog.setMessage("Saving activity ...");
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
            String chosenLocation = args[2];
            String status = args[3];

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", pid));
            params.add(new BasicNameValuePair("title", title));
            params.add(new BasicNameValuePair("details", details));
            params.add(new BasicNameValuePair("chosenLocation", chosenLocation));
            params.add(new BasicNameValuePair("status", status));

            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_activity,
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