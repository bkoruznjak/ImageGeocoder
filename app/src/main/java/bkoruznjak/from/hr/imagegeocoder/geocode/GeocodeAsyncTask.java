package bkoruznjak.from.hr.imagegeocoder.geocode;

/**
 * Created by bkoruznjak on 18/07/2017.
 */


import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import bkoruznjak.from.hr.imagegeocoder.view.activity.MainActivity;

public class GeocodeAsyncTask extends AsyncTask<Float, Void, Address> {

    private final Activity mActivity;
    private String errorMessage = "";
    private float latitude = 0.0f;
    private float longitude = 0.0f;

    public GeocodeAsyncTask(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        ((MainActivity) mActivity).manageProgressBar(true);
    }

    @Override
    protected Address doInBackground(Float... params) {
        Geocoder geocoder = new Geocoder(mActivity, Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(params[0], params[1], 1);
            latitude = params[0];
            longitude = params[1];
        } catch (IOException ioException) {
            errorMessage = "Service Not Available";
            Log.e("žžž", errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            errorMessage = "Invalid Latitude or Longitude Used";
            Log.e("žžž", errorMessage + ". " +
                    "Latitude = " + params[0] + ", Longitude = " +
                    params[1], illegalArgumentException);
        }

        if (addresses != null && addresses.size() > 0)
            return addresses.get(0);

        return null;
    }

    protected void onPostExecute(Address address) {
        ((MainActivity) mActivity).manageProgressBar(false);
        if (address != null) {
            String addressName = "";
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressName += " --- " + address.getAddressLine(i);
            }
            ((MainActivity) mActivity).addImageMarker(latitude, longitude, addressName);
            Toast.makeText(mActivity, addressName, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mActivity, "No known image address", Toast.LENGTH_SHORT).show();
        }
    }
}