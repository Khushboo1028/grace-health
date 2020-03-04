package com.replon.www.grace_thehealthapp.Nearby;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.CustomDialog;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;
import com.replon.www.grace_thehealthapp.Utility.LocationTrack;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PlacesActivity extends AppCompatActivity {

    ImageView back;
    String title;
    TextView tv_heading;

    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 69;
    public static final String TAG = "PlacesActivity";
    public static final int REQUEST_CHECK_SETTINGS = 23;

    LocationTrack locationTrack;
    double longitude,latitude;

    CustomDialog customDialog;

    RotateLoading rotateLoading;

    ArrayList<String> placesID;

    ArrayList<ContentsNearby> nearbyList;

    RecyclerView recyclerView;
    NearbyItemAdapter adapter;

    String googleAPIKey=null;

    private RequestQueue rq;
    private RequestQueue rq2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), PlacesActivity.this);

        setContentView(R.layout.activity_places);

        init();

        tv_heading.setText("Nearby " + title);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (isLocationAccessPermitted()){
            getLocation();
            getNearbyPlaces();
        }



    }

    private void init(){
        back = (ImageView) findViewById(R.id.back);

        tv_heading = (TextView) findViewById(R.id.tv_heading);

        title = getIntent().getStringExtra("title");

        rotateLoading = (RotateLoading) findViewById(R.id.rotateLoading);

        customDialog = new CustomDialog();

        placesID = new ArrayList<>();
        nearbyList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_nearby);
        recyclerView.setHasFixedSize(true);

        rq = Volley.newRequestQueue(this);

    }



    private void checkPermissions() {
        if (isLocationAccessPermitted()) {
            displayLocationSettingsRequest(this);
        } else {
            requestLocationAccessPermission();
        }
    }

    private boolean isLocationAccessPermitted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    private void requestLocationAccessPermission() {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        Log.i(TAG,"Request code "+ requestCode );
//        Log.i(TAG,"Result code "+ resultCode);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                Log.i(TAG,"GOING TO FETCH LOCATION CHECK SETTINGS");
                getNearbyPlaces();
            }else{
                customDialog.showMessageOneOption("Oh Snap!",
                        "Device location is not turned on. Please turn on device's location",
                        R.drawable.ic_error,
                        R.color.pink,
                        "Dismiss",
                        PlacesActivity.this);
            }
        }
    }

    private void getNearbyPlaces(){

        getLocation();

        String url = getUrl(latitude,longitude,title.toLowerCase().trim());
        rotateLoading.start();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{

                    JSONArray results = response.getJSONArray("results");

                    for (int i=0;i<results.length();i++){
                        placesID.add(results.getJSONObject(i).getString("place_id"));
//                        Log.i(TAG,"PLACE "+i + " " + placesID.get(i));
                        getEachPlaceDetails(placesID.get(i),results.getJSONObject(i).getString("name"));
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    rotateLoading.stop();
                    customDialog.showMessageOneOption("Oh Snap!",
                            "Some error occurred. Please try again later " + e.getMessage(),
                            R.drawable.ic_error,
                            R.color.pink,
                            "Dismiss",
                            PlacesActivity.this);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                rotateLoading.stop();
                customDialog.showMessageOneOption("Oh Snap!",
                        "Some error occurred. Please try again later. "+ error.getMessage(),
                        R.drawable.ic_error,
                        R.color.pink,
                        "Dismiss",
                        PlacesActivity.this);
            }
        });

        rq.add(jsonObjectRequest);


    }

    private void getEachPlaceDetails(final String placeID, final String name){

        String new_url = getPlaceUrl(placeID);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, new_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{

                    JSONObject result = response.getJSONObject("result");


                    nearbyList.add(new ContentsNearby(placeID,
                            name,
                            result.getString("international_phone_number"),
                            result.getString("formatted_address")));

                    if (adapter == null) {
                        rotateLoading.stop();
                        adapter = new NearbyItemAdapter(PlacesActivity.this, nearbyList);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }



                }catch(JSONException e){
                    e.printStackTrace();
                    rotateLoading.stop();
//                    customDialog.showMessageOneOption("Oh Snap!",
//                            "Some error occurred. Please try again later" + e.getMessage(),
//                            R.drawable.ic_error,
//                            R.color.pink,
//                            "Dismiss",
//                            PlacesActivity.this);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                rotateLoading.stop();
                customDialog.showMessageOneOption("Oh Snap!",
                        "Some error occurred. Please try again later " +error.getMessage(),
                        R.drawable.ic_error,
                        R.color.pink,
                        "Dismiss",
                        PlacesActivity.this);
            }
        }
        );

        rq.add(jsonObjectRequest);


    }


    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // now, you have permission go ahead
            displayLocationSettingsRequest(this);

        } else {

            if (ActivityCompat.shouldShowRequestPermissionRationale(PlacesActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // now, user has denied permission (but not permanently!)
                customDialog.showMessageTwoOptions("Oh Snap!",
                        "You have previously declined location permission.\n" +
                                "You must approve this permission in \"Permissions\" in the app settings on your device.",
                        R.drawable.ic_error,
                        R.color.pink,
                        "Permissions",
                        PlacesActivity.this);



            } else {

                // now, user has denied permission permanently!

                customDialog.showMessageTwoOptions("Oh Snap!",
                        "You have previously declined this permission.\n" +
                                "You must approve this permission in \"Permissions\" in the app settings on your device.",
                        R.drawable.ic_error,
                        R.color.pink,
                        "Permissions",
                        PlacesActivity.this);

            }

        }
        return;
    }

    private void getLocation()  {

        locationTrack = new LocationTrack(PlacesActivity.this);

            if (locationTrack.canGetLocation()) {

                longitude = locationTrack.getLongitude();
                latitude = locationTrack.getLatitude();

                Log.i(TAG, "Current location Latitude and longitude is " + latitude + " " + longitude);

            } else {
                Log.i(TAG, "Cannot find your location");
            }

    }



    private String getUrl(double latitude, double longitude, String place){

        //    https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=19.110165,72.837595&rankby=distance&keyword=blood%20bank&sensor=true&key=AIzaSyAl_W-16U6yJI1RLf0gZdDd_IHoXFNi6L0


        ApplicationInfo ai;
        try {
            ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            googleAPIKey = bundle.getString("com.google.android.geo.API_KEY");

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        place = place.replace(" ","%20");
//        place = place.replace("&","%26");

        place = place.replace("&","and");
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=" + latitude + "," + longitude);
        googlePlaceUrl.append("&rankby=distance");
        googlePlaceUrl.append("&keyword=" + place);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=" + googleAPIKey);

        Log.i(TAG,"URL IS" + googlePlaceUrl.toString());


        return googlePlaceUrl.toString();

    }


    public String getPlaceUrl(String place_id){

//        https://maps.googleapis.com/maps/api/place/details/json?place_id=ChIJIf891oS6j4ARE8UN-tzKh1k&fields=international_phone_number,formatted_address&key=AIzaSyAl_W-16U6yJI1RLf0gZdDd_IHoXFNi6L0

        StringBuilder googlePlaceDetailsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        googlePlaceDetailsUrl.append("place_id=" + place_id);
        googlePlaceDetailsUrl.append("&fields=international_phone_number,formatted_address");
        googlePlaceDetailsUrl.append("&key=" +googleAPIKey );
//        Log.d(TAG, "url = "+googlePlaceDetailsUrl.toString());
        return googlePlaceDetailsUrl.toString();
    }

    private void displayLocationSettingsRequest(Context context) {

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(PlacesActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_finish_activity,R.anim.exit_finish_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLocationAccessPermitted()){
            getLocation();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermissions();
    }
}
