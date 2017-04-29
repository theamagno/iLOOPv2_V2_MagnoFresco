package com.example.vrncthea.iloopv2_v2_magnofresco.AdminPages;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.vrncthea.iloopv2_v2_magnofresco.JSONParser;
import com.example.vrncthea.iloopv2_v2_magnofresco.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CreateUser extends AppCompatActivity {

    // Progress Dialog
    JSONParser jsonParser = new JSONParser();
    EditText inputNameOfUser;
    EditText inputUsername;
    EditText inputPassword;
    EditText inputEmail;
    private RadioGroup userTypesGroup;
    private RadioButton chosenUserTypeButton;
    private RadioGroup accountStatusGroup;
    private RadioButton chosenAccountStatusButton;
    String json = "";
    private ProgressDialog pDialog;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    private static final String url_create_user = "https://theamagno.000webhostapp.com/php/create_user.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        inputNameOfUser = (EditText)findViewById(R.id.inputNameOfUser);
        inputUsername = (EditText)findViewById(R.id.inputUsername);
        inputEmail = (EditText)findViewById(R.id.inputEmail);
        inputPassword = (EditText)findViewById(R.id.inputPassword);

        Button createUserButton = (Button) findViewById(R.id.createUserButton);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // button click event
       createUserButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread

                // RADIO BUTTON
                userTypesGroup = (RadioGroup) findViewById(R.id.accountTypes);
                // get selected radio button from radioGroup
                int selectedUserType = userTypesGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                chosenUserTypeButton = (RadioButton) findViewById(selectedUserType);

                // RADIO BUTTON
                accountStatusGroup = (RadioGroup) findViewById(R.id.accountStatus);
                // get selected radio button from radioGroup
                int selectedStatusType = accountStatusGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                chosenAccountStatusButton = (RadioButton) findViewById(selectedStatusType);

                String name = inputNameOfUser.getText().toString();
                String username = inputUsername.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                String accounttype = chosenUserTypeButton.getText().toString();
                String status = chosenAccountStatusButton.getText().toString();

                if(name.trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please input name.", Toast.LENGTH_LONG).show();
                }
                else if(username.trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please input username.", Toast.LENGTH_LONG).show();
                }
                else if(password.trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please input password.", Toast.LENGTH_LONG).show();
                }
                else if (password.length() < 8)
                {
                    Toast.makeText(getApplicationContext(), "Password should not be less than eight letters.", Toast.LENGTH_LONG).show();
                }
                else if(accounttype.trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please choose account type.", Toast.LENGTH_LONG).show();
                }
                else if(status.trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please choose account status.", Toast.LENGTH_LONG).show();
                }
                else if(!isValidEmaillId(email.trim()))
                {
                    Toast.makeText(getApplicationContext(), "Invalid email address.", Toast.LENGTH_SHORT).show();
                }
                else if(username.contains(" "))
                {
                    Toast.makeText(getApplicationContext(), "Username cannot contain spaces.", Toast.LENGTH_SHORT).show();
                }
                else if(password.contains(" "))
                {
                    Toast.makeText(getApplicationContext(), "Password cannot contain spaces.", Toast.LENGTH_SHORT).show();
                }
                else if(email.contains(" "))
                {
                    Toast.makeText(getApplicationContext(), "Email address cannot contain spaces.", Toast.LENGTH_SHORT).show();
                }
                else if(!name.matches("[a-zA-Z0-9.? ]*"))
                {
                    Toast.makeText(getApplicationContext(), "Name cannot contain special characters.", Toast.LENGTH_SHORT).show();
                }
                else if(!username.matches("[a-zA-Z0-9.? ]*"))
                {
                    Toast.makeText(getApplicationContext(), "Username cannot contain special characters.", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    System.out.println(name);
                    System.out.println(username);
                    System.out.println(email);
                    System.out.println(password);
                    System.out.println(accounttype);
                    System.out.println(status);

                    new CreateNewUser().execute(name, username, email, password, accounttype, status);
                }

            }
        });
    }

    /**
     * Background Async Task to Create new product
     * */
    class CreateNewUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateUser.this);
            pDialog.setMessage("Creating User..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            String name = args[0];
            String username = args[1];
            String email = args[2];
            String password = args[3];
            String accounttype = args[4];
            String status = args[5];

            if(accounttype.equalsIgnoreCase("Administrator"))
            {
                username = "AD"+username;
            }
            else if(accounttype.equalsIgnoreCase("Organization"))
            {
                username = "ORG"+username;
            }

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("accounttype", accounttype));
            params.add(new BasicNameValuePair("status", status));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_user,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), ChooseUsers.class);
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
        Intent myIntent = new Intent(getApplicationContext(), ChooseUsers.class);

        startActivity(myIntent);
    }

    private boolean isValidEmaillId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

}