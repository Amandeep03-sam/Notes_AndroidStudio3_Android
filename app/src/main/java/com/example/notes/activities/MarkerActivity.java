package com.example.notes.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.notes.R;
import com.example.notes.model.Note;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MarkerActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_CODE = 1;

    private GoogleMap map;
    private Location currentLocation;

    Note note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        Intent intent= getIntent();
        note= (Note) intent.getSerializableExtra("data");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setOnMapLongClickListener(latLng -> {

        });

        map.setOnMarkerClickListener(marker -> {

                if (currentLocation != null) {
                    Toast.makeText(MarkerActivity.this, note.getTitle(), Toast.LENGTH_LONG).show();
                    showAlert(note.getTitle() + "\n" + note.getDescription()  + "\n" + note.getCategory() + "\n");
                } else {
                    Toast.makeText(MarkerActivity.this, "", Toast.LENGTH_LONG).show();
                }

            return false;
        });

        map.setOnPolylineClickListener(polyline -> {

        });


        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("System out", "onMarkerDragStart..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
            }



            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub
                Log.i("System out", "onMarkerDrag...");
            }
        });

        if (checkPermission())
            requestPermission();
        else {
            new Handler().postDelayed(() -> {
                    LatLng selectedLocation = new LatLng(Double.parseDouble(note.getLatitude()), Double.parseDouble(note.getLongitude()));
                    setNoteCurrent(selectedLocation);
            }, 2000);
        }

    }


    private boolean checkPermission() {
        int isGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return isGranted != PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (checkPermission())
                requestPermission();
            else {
                new Handler().postDelayed(() -> {
                    LatLng selectedLocation = new LatLng(Double.parseDouble(note.getLatitude()), Double.parseDouble(note.getLongitude()));
                    setNoteCurrent(selectedLocation);
                }, 2000);
            }
        }
    }

    private void setNoteCurrent(LatLng location) {
        currentLocation = new Location("myLocation");
        currentLocation.setLatitude(location.latitude);
        currentLocation.setLongitude(location.longitude);
        MarkerOptions markerOptions = new MarkerOptions().position(location)
                .title(note.getTitle())
                .snippet(note.getDescription());
        map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 8));
    }


    @SuppressWarnings("deprecation")
    private void showAlert(String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(
                MarkerActivity.this).create();
        alertDialog.setTitle("");
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", (dialog, which) -> dialog.dismiss());

        // Showing Alert
        alertDialog.show();
    }




}
