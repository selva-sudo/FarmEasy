package com.selvaraj.buyerapp.activity;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.selvaraj.buyerapp.R;
import com.selvaraj.buyerapp.directionhelpers.FetchURL;
import com.selvaraj.buyerapp.directionhelpers.TaskLoadedCallback;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, TaskLoadedCallback {

    private static final String TAG = DisplayActivity.class.getSimpleName();
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private ArrayList<LatLng> mMarkerPoints = new ArrayList<LatLng>();
    private NotificationManager notificationManager;
    int notificationCount = 1;
    private Marker marker;
    private Polyline currentPolyline;
    private MarkerOptions place1, place2;
    private Context context;
    private int count = 0;
    private LatLng myPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        context = getApplicationContext();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 234);
            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 234);
            return;
        }
        mMap.setMyLocationEnabled(true);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double lat, lng;
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                            myPoint = new LatLng(lat, lng);
                            if (count == 0) {
                                drawMarker(myPoint);
                                place1 = new MarkerOptions().position(myPoint).title("You");
                                count = 1;
                            }
                        }
                    }
                });
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
    }

    public double calculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Radius * c;
    }

    private void drawMarker(LatLng point) {
        MarkerOptions options = new MarkerOptions();
        if (mMarkerPoints.size() < 2) {
            mMarkerPoints.add(point);
        } else {
            mMarkerPoints.remove(1);
            place2 = new MarkerOptions().position(point).title("Bus");
            mMarkerPoints.add(point);
        }

        options.position(point);

        if (mMarkerPoints.size() == 1) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMap.addMarker(options).setTitle("You");
            return;

        } else if (mMarkerPoints.size() == 2) {
            Log.d(TAG, "Got two Locations");

            place2 = new MarkerOptions().position(point).title("Bus");

            if (place1 != null) {
                String url = getDirectionsUrl(place1.getPosition(), place2.getPosition());
                new FetchURL(DisplayActivity.this).execute(url, "driving");
            }
            double distance;
            distance = calculationByDistance(mMarkerPoints.get(0), mMarkerPoints.get(1));
            notificationCount = notificationCount + 1;
            double dis = Math.round(distance * 100) / 100.0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                buildNotification(dis);
            }
            if (marker != null) {
                marker.remove();
            }
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            marker = mMap.addMarker(options);
            marker.setTitle("Bus");
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(marker.getPosition());
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
            return;
        }
        marker = mMap.addMarker(options);
    }

    private void buildNotification(double distance) {
        String id = "Channel id";
        String title = "Transport Tracker";
        String stop = "stop";
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                notificationManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);
            builder.setContentTitle(title)                           // required
                    .setContentText(getString(R.string.notification_text) + " Only " + String.valueOf(distance) + " Km") // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentTitle(getString(R.string.app_name))
                    .setTicker(title)
                    .setSmallIcon(R.drawable.ic_stat_name);
            Notification notification = builder.build();
            notificationManager.notify(NOTIFY_ID, notification);
        } else {*/
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, id);
        builder.setContentTitle(title)                            // required
                .setSmallIcon(R.drawable.ic_stat_name)   // required
                .setContentText(getString(R.string.notification_text)) // required
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(false)
                .setContentTitle(getString(R.string.app_name))
                .setTicker(title)
                .setPriority(Notification.PRIORITY_HIGH);
    }


    private void setMarker(DataSnapshot dataSnapshot) {
        String key = dataSnapshot.getKey();
        HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
        double lat = Double.parseDouble(value.get("latitude").toString());
        double lng = Double.parseDouble(value.get("longitude").toString());
        LatLng location = new LatLng(lat, lng);
        LatLng busPoint = location;
        place2 = new MarkerOptions().position(new LatLng(lat, lng)).title("bus");
        drawMarker(location);
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + "driving";
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat, lng;
        lat = location.getLatitude();
        lng = location.getLongitude();
        myPoint = new LatLng(lat, lng);
        place1 = new MarkerOptions().position(myPoint).title("You");
        if (count == 0) {
            drawMarker(myPoint);
            count = 1;
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}
