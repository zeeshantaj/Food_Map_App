package com.example.food_map_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int FIRE_PERMISSION_CODE = 1;
    private Location currentLocation;
    private LatLngBounds karachiBounds;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        setToolbar();
    }
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FIRE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;
                    mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MainActivity.this);
                }
            }
        });
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        // Define LatLngBounds for Karachi
        LatLng southwest = new LatLng(24.7408, 66.9881); // Southwest bounds of Karachi
        LatLng northeast = new LatLng(25.2736, 67.4204); // Northeast bounds of Karachi

        karachiBounds = new LatLngBounds(southwest, northeast);

        map.setLatLngBoundsForCameraTarget(karachiBounds);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(karachiBounds.getCenter(), 12.0f));

        map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (!karachiBounds.contains(map.getCameraPosition().target)) {
                    // If the camera moves outside Karachi bounds, move it back to Karachi
                    map.moveCamera(CameraUpdateFactory.newLatLngBounds(karachiBounds, 0));
                }
            }
        });

//        map.addMarker(new MarkerOptions()
//                .position(sydney)
//                .title("My Location"));
//        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FIRE_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else {
                Toast.makeText(this, "Location permission is denied Please allow the Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_type_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.normal){
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        if (id == R.id.satellite) {
            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        if (id == R.id.hybird) {
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
        if (id == R.id.terrain) {
            map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }

        return super.onOptionsItemSelected(item);
    }
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {


        }
    }
}