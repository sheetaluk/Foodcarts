package com.foodcarts.foodcarts;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.IOException;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private final double DEFAULT_LAT = 37.776775;
    private final double DEFAULT_LNG = -122.416791;
    private final String FOODCARTS_URL = "http://vast-castle-9076.herokuapp.com/api/v1.0/nearest_foodcarts";
    private static final String TAG = "MapsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private class FetchFoodcartsTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params) {
            String result = "[]";

            try {
                Log.i(TAG, "content of url:" + result);
                result = new FoodcartsFetcher().getUrl(params[0]);
                Log.i(TAG, "content of url:" + result);
            } catch(IOException ioe) {
                Log.e(TAG, "failed:" + ioe);
            }
            return result;
        }
    }


    private Void addUserPinToMap(double lat, double lng) {
        LatLng latLng = new LatLng(DEFAULT_LAT, DEFAULT_LNG);
        mMap.addMarker(new MarkerOptions().position(latLng).title("User Marker").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        // I think zoom >=14 crashes my emulator.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

        return null;
    }

    private String getFoodcartsEndpoint(double lat, double lng) {
      return FOODCARTS_URL + "?user_lat=" + String.valueOf(lat) + "&user_long=" + String.valueOf(lng);
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }



    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        // add user pin to map
        addUserPinToMap(DEFAULT_LAT, DEFAULT_LNG);

        // fetch foodcarts
        new FetchFoodcartsTask().execute(getFoodcartsEndpoint(DEFAULT_LAT, DEFAULT_LNG));
    }
}
