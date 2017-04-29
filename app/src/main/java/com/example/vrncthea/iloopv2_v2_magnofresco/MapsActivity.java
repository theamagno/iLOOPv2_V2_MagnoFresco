package com.example.vrncthea.iloopv2_v2_magnofresco;

import android.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vrncthea.iloopv2_v2_magnofresco.AdminPages.UpdateActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.InfoWindowAdapter {

    private GoogleMap mMap;

    TextView activityTitle;
    TextView activityDetails;
    TextView activityPostedBy;
    TextView activityTimePosted;
    TextView activityStatus;
    String json = "";
    String pid;
    String usernameSess = "";
    SessionManager session;
    private HashMap<String, String> userSessType;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private static final String url_activity_details = "https://theamagno.000webhostapp.com/php/get_activity_details.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ACTIVITIES = "activities";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DETAILS = "details";
    private static final String TAG_CHOSENLOCATION = "chosenlocation";
    private static final String TAG_STATUS= "status";

    Marker activityMarker;
    LocationManager lm;
    LocationListener ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        pid = i.getStringExtra(TAG_ID);

        // Getting complete product details in background thread
        new GetActivityDetails().execute();

        activityTitle = (TextView) findViewById(R.id.activityTitle);
        activityDetails = (TextView)findViewById(R.id.activityDetails);
        activityPostedBy = (TextView)findViewById(R.id.activityPostedBy);
        activityTimePosted = (TextView)findViewById(R.id.activityTimePosted);
        activityStatus = (TextView)findViewById(R.id.activityStatus);

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
            pDialog = new ProgressDialog(MapsActivity.this);
            pDialog.setMessage("Loading map details. Please wait...");
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

                            View view = getLayoutInflater().inflate(R.layout.info_window, null);

                            LatLng activityLoc = new LatLng (14.609828, 120.991973);

                            if(activity.getString(TAG_CHOSENLOCATION).equalsIgnoreCase("Roque Ruano Building (Engineering)"))
                            {
                                activityLoc = new LatLng (14.609828, 120.991973);
                            }
                            else if(activity.getString(TAG_CHOSENLOCATION).equalsIgnoreCase("Albertus Magnus Building (CTHM)"))
                            {
                                activityLoc = new LatLng (14.610733, 120.991387);
                            }
                            else if(activity.getString(TAG_CHOSENLOCATION).equalsIgnoreCase("Beato Angelico Building (Architecture)"))
                            {
                                activityLoc = new LatLng (14.6081455,120.9893627);
                            }
                            else if(activity.getString(TAG_CHOSENLOCATION).equalsIgnoreCase("St. Raymund of Penafort Building (AB)"))
                            {
                                activityLoc = new LatLng (14.610990, 120.989207);
                            }
                            else if(activity.getString(TAG_CHOSENLOCATION).equalsIgnoreCase("Martin de Porres Building (Medicine)"))
                            {
                                activityLoc = new LatLng (14.611577, 120.989539);
                            }
                            else if(activity.getString(TAG_CHOSENLOCATION).equalsIgnoreCase("UST Main Building"))
                            {
                                activityLoc = new LatLng (14.6099156, 120.9882577);
                            }
                            else if(activity.getString(TAG_CHOSENLOCATION).equalsIgnoreCase("Benavides Park"))
                            {
                                activityLoc = new LatLng (14.6093861,120.9894432);
                            }
                            else if(activity.getString(TAG_CHOSENLOCATION).equalsIgnoreCase("BGPOP Building"))
                            {
                                activityLoc = new LatLng (14.6081455,120.9893627);
                            }
                            else if(activity.getString(TAG_CHOSENLOCATION).equalsIgnoreCase("Quadricentenial Park"))
                            {
                                activityLoc = new LatLng (14.609817,120.9889175);
                            }
                            else if(activity.getString(TAG_CHOSENLOCATION).equalsIgnoreCase("UST Library"))
                            {
                                activityLoc = new LatLng (14.610304,120.9882868);
                            }
                            else if(activity.getString(TAG_CHOSENLOCATION).equalsIgnoreCase("Seminary Gym"))
                            {
                                activityLoc = new LatLng (14.6097501,120.9867514);
                            }
                            else if(activity.getString(TAG_CHOSENLOCATION).equalsIgnoreCase("Football Field"))
                            {
                                activityLoc = new LatLng (14.6090285,120.9893987);
                            }
                            else if(activity.getString(TAG_CHOSENLOCATION).equalsIgnoreCase("Tan Yan Kee Building"))
                            {
                                activityLoc = new LatLng (14.6110114,120.9885613);
                            }
                            else if(activity.getString(TAG_CHOSENLOCATION).equalsIgnoreCase("Quadricentenial Pavilion Building"))
                            {
                                activityLoc = new LatLng (14.609167, 120.991418);
                            }

                            activityMarker = mMap.addMarker(new MarkerOptions().position(activityLoc));

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setInfoWindowAdapter(this);
        LatLng ustLocation = new LatLng(14.609656, 120.989638);

        CameraPosition ust_ortho = new CameraPosition.Builder().target(ustLocation).bearing(-43f).zoom(17.5f).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(ust_ortho));

        mMap.getUiSettings().setZoomControlsEnabled(true);

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != 1)
        {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
