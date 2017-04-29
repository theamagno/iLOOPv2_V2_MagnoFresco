package com.example.vrncthea.iloopv2_v2_magnofresco.AdminPages;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.vrncthea.iloopv2_v2_magnofresco.JSONParser;
import com.example.vrncthea.iloopv2_v2_magnofresco.R;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrgUsers extends ListActivity {
    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> usersList;

    private static String url_all_orgs = "http://theamagno.000webhostapp.com/php/get_all_organization.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERS = "users";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PASSWORD = "password";
    private static final String TAG_ACCOUNTTYPE = "accounttype";
    private static final String TAG_ACCOUNTSTATUS = "status";

    JSONArray users = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_users);

        // Hashmap for ListView
        usersList = new ArrayList<HashMap<String, String>>();

        new LoadAllUsers().execute();

        ListView lv = getListView();

        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        UpdateUser.class);
                // sending pid to next activity
                in.putExtra(TAG_ID, pid);

                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });
    }

    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllUsers extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(OrgUsers.this);
            pDialog.setMessage("Loading users. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All users from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_orgs, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All AllUsers: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // users found
                    // Getting Array of AllUsers
                    users = json.getJSONArray(TAG_USERS);

                    // looping through All users
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject c = users.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String username = c.getString(TAG_USERNAME);
                        String accounttype = c.getString(TAG_ACCOUNTTYPE);
                        String status = c.getString(TAG_ACCOUNTSTATUS);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, id);
                        map.put(TAG_NAME, name);
                        map.put(TAG_USERNAME, "Username: " + username);
                        map.put(TAG_ACCOUNTTYPE, "Type: " + accounttype);
                        map.put(TAG_ACCOUNTSTATUS, "Status: " + status);

                        // adding HashList to ArrayList
                        usersList.add(map);
                    }
                } else {
                    // no users found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(),
                            CreateUser.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
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
            // dismiss the dialog after getting all users
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            OrgUsers.this, usersList,
                            R.layout.list_user, new String[] {
                            TAG_ID,
                            TAG_NAME,
                            TAG_USERNAME,
                            TAG_ACCOUNTTYPE,
                            TAG_ACCOUNTSTATUS},
                            new int[] {
                                    R.id.pid,
                                    R.id.nameview,
                                    R.id.usernameview,
                                    R.id.accountTypeview,
                                    R.id.accountStatusview });
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }

    public void goBack (View buttonControl)
    {
        Intent myIntent = new Intent(getApplicationContext(), ChooseUsers.class);

        startActivity(myIntent);
    }

}