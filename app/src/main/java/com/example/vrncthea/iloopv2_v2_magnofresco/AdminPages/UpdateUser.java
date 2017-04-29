package com.example.vrncthea.iloopv2_v2_magnofresco.AdminPages;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.vrncthea.iloopv2_v2_magnofresco.JSONParser;
import com.example.vrncthea.iloopv2_v2_magnofresco.R;

public class UpdateUser extends AppCompatActivity {

    EditText updateUsername;
    EditText updatePassword;
    EditText updateNameOfUser;
    EditText updateEmail;
    private RadioGroup userTypesGroup;
    private RadioButton chosenUserTypeButton;
    private RadioGroup accountStatusGroup;
    private RadioButton chosenAccountStatusButton;
    private RadioButton updateAccountStatus_Active;
    private RadioButton updateAccountStatus_Inactive;
    private RadioButton updateAccountType_Admin;
    private RadioButton updateAccountType_Org;
    String json = "";
    String pid;
    Button btnUpdate;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single product url
    private static final String url_user_details = "https://theamagno.000webhostapp.com/php/get_user_details.php";

    // url to update product
    private static final String url_update_user = "https://theamagno.000webhostapp.com/php/update_user.php";

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // save button
        btnUpdate = (Button) findViewById(R.id.updateUserButton);

        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        pid = i.getStringExtra(TAG_ID);

        // Getting complete product details in background thread
        new GetUserDetails().execute();

        updateNameOfUser = (EditText)findViewById(R.id.updateNameOfUser);
        updateUsername = (EditText)findViewById(R.id.updateUsername);
        updatePassword = (EditText)findViewById(R.id.updatePassword);
        updateEmail = (EditText)findViewById(R.id.updateEmail);

        // save button click event
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // RADIO BUTTON
                userTypesGroup = (RadioGroup) findViewById(R.id.updateAccountTypes);
                // get selected radio button from radioGroup
                int selectedUserType = userTypesGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                chosenUserTypeButton = (RadioButton) findViewById(selectedUserType);

                // RADIO BUTTON
                accountStatusGroup = (RadioGroup) findViewById(R.id.updateAccountStatus);
                // get selected radio button from radioGroup
                int selectedStatusType = accountStatusGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                chosenAccountStatusButton = (RadioButton) findViewById(selectedStatusType);

                // getting updated data from EditTexts
                String name = updateNameOfUser.getText().toString();
                String username = updateUsername.getText().toString();
                String email = updateEmail.getText().toString();
                String password = updatePassword.getText().toString();
                String accounttype = chosenUserTypeButton.getText().toString();
                String status = chosenAccountStatusButton.getText().toString();

                if(password.trim().equals(""))
                {

                    Toast.makeText(getApplicationContext(), "Please input password.", Toast.LENGTH_LONG).show();
                }
                else if (password.length() < 8)
                {
                    Toast.makeText(getApplicationContext(), "Password should not be less than eight letters.", Toast.LENGTH_LONG).show();
                }
                else if(status.trim().equals(""))
                {

                    Toast.makeText(getApplicationContext(), "Please choose account status.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    new SaveUserDetails().execute(name, username, email, password, accounttype, status);
                }
            }
        });

    }

    /**
     * Background Async Task to Get complete product details
     * */
    class GetUserDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdateUser.this);
            pDialog.setMessage("Loading user details. Please wait...");
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
                                url_user_details, "GET", params);

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

                            // product with this pid found
                            // Edit Text
                            updateNameOfUser = (EditText)findViewById(R.id.updateNameOfUser);
                            updateUsername = (EditText)findViewById(R.id.updateUsername);
                            updatePassword = (EditText)findViewById(R.id.updatePassword);
                            updateEmail = (EditText)findViewById(R.id.updateEmail);

                            accountStatusGroup = (RadioGroup) findViewById(R.id.updateAccountStatus);
                            RadioButton accountStatus_Active = (RadioButton) findViewById(R.id.updateAccountStatus_Active);
                            RadioButton accountStatus_Inactive = (RadioButton) findViewById(R.id.updateAccountStatus_Inactive);

                            userTypesGroup = (RadioGroup) findViewById(R.id.updateAccountTypes);
                            RadioButton updateAccountType_Admin = (RadioButton) findViewById(R.id.updateAccountType_Admin);
                            RadioButton updateAccountType_Org = (RadioButton) findViewById(R.id.updateAccountType_Org);

                            // display product data in EditText
                            updateNameOfUser.setText(user.getString(TAG_NAME));
                            updateNameOfUser.setEnabled(false);
                            updateUsername.setText(user.getString(TAG_USERNAME));
                            updateUsername.setEnabled(false);
                            updateEmail.setText(user.getString(TAG_EMAIL));
                            updatePassword.setText(user.getString(TAG_PASSWORD));

                            System.out.println(TAG_ACCOUNTTYPE);

                            if(user.getString(TAG_ACCOUNTTYPE).equalsIgnoreCase("Administrator"))
                            {
                                updateAccountType_Admin.setChecked(true);
                            }
                            else if (user.getString(TAG_ACCOUNTTYPE).toString().equalsIgnoreCase("Organization"))
                            {
                                updateAccountType_Org.setChecked(true);
                            }

                            for (int i = 0; i < userTypesGroup.getChildCount(); i++) {
                                userTypesGroup.getChildAt(i).setEnabled(false);
                            }

                            if(user.getString(TAG_ACCOUNTSTATUS).toString().equalsIgnoreCase("Active"))
                            {
                                accountStatus_Active.setChecked(true);
                            }
                            else if (user.getString(TAG_ACCOUNTSTATUS).toString().equalsIgnoreCase("Inactive"))
                            {
                                accountStatus_Inactive.setChecked(true);
                            }

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
            pDialog = new ProgressDialog(UpdateUser.this);
            pDialog.setMessage("Saving user ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {

            String name = args[0];
            String username = args[1];
            String email = args[2];
            String password = args[3];
            String accounttype = args[4];
            String status = args[5];

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", pid));
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("accounttype", accounttype));
            params.add(new BasicNameValuePair("status", status));

            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_user,
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
