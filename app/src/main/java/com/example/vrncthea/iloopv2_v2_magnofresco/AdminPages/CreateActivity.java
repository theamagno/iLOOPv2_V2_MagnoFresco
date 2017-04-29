package com.example.vrncthea.iloopv2_v2_magnofresco.AdminPages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.vrncthea.iloopv2_v2_magnofresco.OrgPages.OrgViewActivities;
import com.example.vrncthea.iloopv2_v2_magnofresco.R;
import com.example.vrncthea.iloopv2_v2_magnofresco.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateActivity extends AppCompatActivity {

    // Progress Dialog
    JSONParser jsonParser = new JSONParser();
    EditText inputActivityTitle;
    EditText inputActivityDetails;
    Spinner chooseLocation;
    private RadioGroup activityStatusGroup;
    private RadioButton activityStatus;
    String json = "";
    String chosenLocationString = "";
    private ProgressDialog pDialog;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    private static final String url_create_activity = "https://theamagno.000webhostapp.com/php/create_activity.php";

    SessionManager session;
    private HashMap<String, String> userSessType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        session = new SessionManager(getApplicationContext());

        // get user data from session


        inputActivityTitle = (EditText)findViewById(R.id.createActivityTitle);
        inputActivityDetails = (EditText)findViewById(R.id.createActivityDetails);

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

                chosenLocationString = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        Button createActivityButton = (Button) findViewById(R.id.createActivitytButton);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // button click event
        createActivityButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread

                HashMap<String, String> user = session.getLoginSession();

                // username
                String username = user.get(SessionManager.KEY_SESSUSERNAME);

                Spinner locationSpinner = (Spinner) findViewById(R.id.activityLocation_spinner);
                String text = locationSpinner.getSelectedItem().toString();

                String title = inputActivityTitle.getText().toString();
                String details = inputActivityDetails.getText().toString();
                String postedby = user.get(SessionManager.KEY_SESSUSERNAME);
                String chosenLocation = locationSpinner.getSelectedItem().toString();

                if(title.trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please input title.", Toast.LENGTH_LONG).show();
                }
                else if(details.trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please enter activity details.", Toast.LENGTH_LONG).show();
                }
                else if(chosenLocation.trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please choose location.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    System.out.println(title);
                    System.out.println(details);
                    System.out.println(chosenLocation);
                    System.out.println(postedby);

                    new CreateNewActivity().execute(title, details, chosenLocation, postedby);
                }

            }
        });

    }

    /**
     * Background Async Task to Create new product
     * */
    class CreateNewActivity extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateActivity.this);
            pDialog.setMessage("Creating Activity..");
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
            String chosenLocation = args[2];
            String status = "Pending";
            String postedby = args[3];

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("title", title));
            params.add(new BasicNameValuePair("details", details));
            params.add(new BasicNameValuePair("chosenlocation", chosenLocation));
            params.add(new BasicNameValuePair("status", status));
            params.add(new BasicNameValuePair("postedby", postedby));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_activity,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), ChooseActivities.class);
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
        this.finish();
    }
}
