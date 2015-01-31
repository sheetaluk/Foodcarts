package com.foodcarts.foodcarts;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sheetaluk on 1/28/15.
 */

public class FoodcartsFetcher {

    public static final String TAG = "FoodcartsFetcher";

    private static final String ENDPOINT = "http://vast-castle-9076.herokuapp.com/api/v1.0/nearest_foodcarts";

    byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public ArrayList<Foodcart> fetchFoodcarts(double lat, double lng) {
        ArrayList<Foodcart> foodcarts = new ArrayList<Foodcart>();

        try {
            String url = Uri.parse(ENDPOINT).buildUpon().appendQueryParameter("user_lat", String.valueOf(lat)).appendQueryParameter("user_long", String.valueOf(lng)).build().toString();
            String jsonString = getUrl(url);

            parseFoodcarts(foodcarts, new JSONArray(jsonString));
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse foodcarts", je);
        }
        return foodcarts;
    }

    void parseFoodcarts(ArrayList<Foodcart> foodcarts, JSONArray foodcartsArray) throws JSONException {
        for (int i = 0; i < foodcartsArray.length(); i++) {
            JSONObject jb = (JSONObject) foodcartsArray.getJSONObject(i);

            Foodcart fc = new Foodcart();
            fc.setmLatitude(jb.getString("latitude"));
            fc.setmLongitude(jb.getString("longitude"));
            fc.setmAddress(jb.getString("address"));
            fc.setmApplicant(jb.getString("applicant"));
            fc.setmFooditems(jb.getString("fooditems"));

            foodcarts.add(fc);
        }
    }
}
