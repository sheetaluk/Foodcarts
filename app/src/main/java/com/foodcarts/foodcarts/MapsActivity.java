package com.foodcarts.foodcarts;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private static final String TAG = "MapsActivity";
    ArrayList<Foodcart> mFoodcarts;

    private final double DEFAULT_LAT = 37.776775;
    private final double DEFAULT_LNG = -122.416791;
    private final int DEFAULT_ZOOM = 13;


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

    private Void addUserPinToMap(double lat, double lng) {
        LatLng latLng = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(latLng).title("User Marker").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        // I think zoom >=14 crashes my emulator.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));

        return null;
    }

    private Void addFoodcartMarkerToMap(double lat, double lng, Map<String, String> info) {
        LatLng latLng = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("User Marker")
                .title(info.get("applicant"))
                .snippet(info.get("address") + info.get("fooditems")));

        // I think zoom >=14 crashes my emulator.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));

        return null;
    }

    void setupFoodcartMarkers() {
        for (int i = 0; i < mFoodcarts.size(); i++) {

            Map<String, String> info = new HashMap<String, String>();
            info.put("address", mFoodcarts.get(i).getmAddress());
            info.put("applicant", mFoodcarts.get(i).getmApplicant());
            info.put("fooditems", mFoodcarts.get(i).getmFooditems());

            addFoodcartMarkerToMap(Double.parseDouble(mFoodcarts.get(i).getmLatitude()), Double.parseDouble(mFoodcarts.get(i).getmLongitude()), info);
        }
    }

    private void addMarkersToMap(double lat, double lng) {
        // add user pin to map
        addUserPinToMap(lat, lng);

        // add foodcart markers
        Map<String, Double> latlng = new HashMap<String, Double>();
        latlng.put("lat", lat);
        latlng.put("lng", lng);
        new FetchFoodcartsTask().execute(latlng);
    }

    private class FetchFoodcartsTask extends AsyncTask<Map<String, Double>, Void, ArrayList<Foodcart>> {
        @Override
        protected ArrayList<Foodcart> doInBackground(Map<String, Double>... params) {
            Double lat = params[0].get("lat");
            Double lng = params[0].get("lng");
            return new FoodcartsFetcher().fetchFoodcarts(lat.doubleValue(), lng.doubleValue());
        }

        @Override
        protected void onPostExecute(ArrayList<Foodcart> foodcarts) {
            mFoodcarts = foodcarts;
            setupFoodcartMarkers();
        }
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

        addMarkersToMap(DEFAULT_LAT, DEFAULT_LNG);

        mMap.setOnMapClickListener(new OnMapClickListener() {
            @Override
            public void onMapClick(LatLng newLatLng) {
                mMap.clear();
                addMarkersToMap(newLatLng.latitude, newLatLng.longitude);
            }

        });


    }
}
