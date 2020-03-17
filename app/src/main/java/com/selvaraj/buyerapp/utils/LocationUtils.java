package com.selvaraj.buyerapp.utils;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.selvaraj.buyerapp.base.BaseApplication;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationUtils {
    private Context context;
    private LocationCallBack locationCallBack;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public LocationUtils(FusedLocationProviderClient fusedLocationProviderClient, Context context, LocationCallBack locationCallBack) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;
        this.context = context;
        this.locationCallBack = locationCallBack;
    }

    @SuppressWarnings("MissingPermission")
    public void getLastLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            BaseApplication.getInstance().getUserManager().setVendorLocation(location);
                            new GetAddressAsyncTask(location, locationCallBack).execute();
                        }
                    }
                });
    }

    public interface LocationCallBack {
        void onLocationReceived(String location);
    }

    private class GetAddressAsyncTask extends AsyncTask<Void, Void, Void> {
        private Location location;
        private LocationCallBack locationCallBack;

        public GetAddressAsyncTask(Location location, LocationCallBack locationCallBack) {
            this.location = location;
            this.locationCallBack = locationCallBack;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                String lastKnownLocation = city + ", " + state + ", " + country + ", " + postalCode + ", " + knownName;
                BaseApplication.getInstance().getUserManager().setLastKnownLocation(lastKnownLocation);
                locationCallBack.onLocationReceived(lastKnownLocation);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
