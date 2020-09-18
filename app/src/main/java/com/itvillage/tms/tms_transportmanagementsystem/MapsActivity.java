package com.itvillage.tms.tms_transportmanagementsystem;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itvillage.tms.tms_transportmanagementsystem.dto.response.UserDetailsResponse;
import com.itvillage.tms.tms_transportmanagementsystem.util.Utility;

import androidx.fragment.app.FragmentActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String requestType,userId;
    Handler handler;

    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        requestType = getIntent().getExtras().getString("requestType");
        Log.d("Request Id", requestType);
        Toast.makeText(this, "" + requestType, Toast.LENGTH_SHORT).show();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        userId= UserDetailsResponse.userId;
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

        mMap = googleMap;
        // Add a marker in Sydney and move the camera


//            handler = new Handler();
//            final Runnable r = new Runnable() {
//                public void run() {
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    myRef = database.getReference("Locations");
                    // Read from the database
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshots) {
                            mMap.clear();
                            for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                                //TODO: Check bus status in here
                                Double lat = (Double) dataSnapshot.child("lat").getValue();
                                Double longi = (Double) dataSnapshot.child("long").getValue();
                                String name = dataSnapshot.child("nameEditText").getValue().toString();
                                String phoneNumber = dataSnapshot.child("phoneNumberEditText").getValue().toString();
                                String requestType = dataSnapshot.child("type").getValue().toString();
                                LatLng sydney = new LatLng(lat, longi);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, longi), 15f));
                                if (requestType.equals("Hospital")) {
                                    mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)).position(sydney).title(name + "(" + phoneNumber+")"));
                                } else if(requestType.equals("Police Station")){
                                    mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)).position(sydney).title(name + "(" + phoneNumber+")"));

                                }
                                else if(requestType.equals("Fire Station")){
                                    mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)).position(sydney).title(name + "(" + phoneNumber+")"));

                                }
                                else if(requestType.equals("ATM Booth")){
                                    mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)).position(sydney).title(name + "(" + phoneNumber+")"));

                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Not found",Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "Latitude Longitude not found.", Toast.LENGTH_SHORT).show();
                        }
                    });

//                    handler.postDelayed(this, 1000);
//                }
//            };

           // handler.postDelayed(r, 1000);
            Toast.makeText(getApplicationContext(), "All", Toast.LENGTH_SHORT).show();



    }

    private void getLocation() {
    }
}
