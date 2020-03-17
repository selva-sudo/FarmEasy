package com.selvaraj.buyerapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.selvaraj.buyerapp.R;
import com.selvaraj.buyerapp.base.BaseApplication;
import com.selvaraj.buyerapp.directionhelpers.FetchURL;
import com.selvaraj.buyerapp.directionhelpers.TaskLoadedCallback;
import com.selvaraj.buyerapp.model.Products;

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    private double vendorLat, vendorLng, userLat, userLng;
    private ArrayList markerPoints = new ArrayList();
    private Polyline polyline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Products selectedProduct = BaseApplication.getInstance().getUserManager().getSelectedProduct();
        vendorLat = selectedProduct.getLat();
        vendorLng = selectedProduct.getLng();
        userLat = BaseApplication.getInstance().getUserManager().getAuthUser().getLat();
        userLng = BaseApplication.getInstance().getUserManager().getAuthUser().getLng();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 234);
            return;
        }
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setMaxZoomPreference(16);
        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setTrafficEnabled(true);
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng vendorLL = new LatLng(vendorLat, vendorLng);
        LatLng buyerLL = new LatLng(userLat, userLng);
        markerPoints.add(vendorLL);
        markerPoints.add(buyerLL);
        MarkerOptions options = new MarkerOptions();
        options.position(vendorLL);
        options.title("Vendor");
        mMap.addMarker(options);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        MarkerOptions options1 = new MarkerOptions();
        options1.position(buyerLL);
        options1.title("You");
        mMap.addMarker(options1);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        String url = getUrl(options.getPosition(), options1.getPosition());
        new FetchURL(MapsActivity.this).execute(url, "driving");

    }


    private String getUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + "driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
    }


    @Override
    public void onTaskDone(Object... values) {
        if (polyline != null)
            polyline.remove();
        polyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}
