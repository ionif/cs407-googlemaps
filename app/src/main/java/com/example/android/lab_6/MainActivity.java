package com.example.android.lab_6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity {

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;

    private final LatLng mDestinationLatLng = new LatLng(43.0753, -89.4042);
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            googleMap.addMarker(new MarkerOptions().position(mDestinationLatLng).title("Destination"));
        });
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        displayMyLocation();
    }

    private void displayMyLocation() {
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);

        if(permission == PackageManager.PERMISSION_DENIED) {
            Log.i("derp", "denied");
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            Log.i("derp", "accepted");
            mFusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(this, task -> {
                        Location mLastKnownLocation = task.getResult();
                        //Log.d("coords", String.valueOf(mLastKnownLocation.getLatitude()) + ","+ String.valueOf(mLastKnownLocation.getLongitude()));
                        if(task.isSuccessful() && mLastKnownLocation != null) {
                            mMap.addPolyline(new PolylineOptions().add(
                                    new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()),
                            mDestinationLatLng));

                            mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()))
                                .title("My Location"));
                        }
                    });
        }
    }
}
