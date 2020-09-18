package com.itvillage.tms.tms_transportmanagementsystem;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itvillage.tms.tms_transportmanagementsystem.asystask.LocationAsynTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import im.delight.android.location.SimpleLocation;

public class AdminActivity extends AppCompatActivity {
    Button fetchLocation,save;
    EditText nameEditText, phoneNumberEditText;
    TextView showLatLong;
    double lat,longi;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees_home);

        fetchLocation = findViewById(R.id.fetchLocation);
        save = findViewById(R.id.save);

        nameEditText = findViewById(R.id.name);
        phoneNumberEditText = findViewById(R.id.mobileName);
        showLatLong= findViewById(R.id.showLatLong);

        final Spinner userTypeSpinner = (Spinner) findViewById(R.id.spinner);


        List<String> categories = new ArrayList<String>();
        categories.add("Select Type");
        categories.add("Hospital");
        categories.add("Police Station");
        categories.add("Fire Station");
        categories.add("ATM Booth");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        userTypeSpinner.setAdapter(dataAdapter);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Locations").push();


                    myRef.child("lat").setValue(lat);
                    myRef.child("long").setValue(longi);
                    myRef.child("nameEditText").setValue(nameEditText.getText().toString());
                    myRef.child("type").setValue(userTypeSpinner.getSelectedItem().toString());
                    myRef.child("phoneNumberEditText").setValue(phoneNumberEditText.getText().toString());

                   Toast.makeText(getApplicationContext(),"Added Successful",Toast.LENGTH_SHORT).show();


            }
        });

        fetchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLocationEnabled()) {

                    LocationAsynTask locationAsynTask = new LocationAsynTask(getApplicationContext());
                    locationAsynTask.execute();
                    try {
                        SimpleLocation simpleLocation = locationAsynTask.get();
                        if (simpleLocation == null) {
                            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                        } else {
                            lat =simpleLocation.getLatitude();
                            longi = simpleLocation.getLongitude();
                            showLatLong.setText(lat+","+longi);
                            Log.d("Latitude", String.valueOf(simpleLocation.getLatitude()));
                            Log.d("Longitude", String.valueOf(simpleLocation.getLongitude()));
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


    }
    private boolean isLocationEnabled() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.app_name)
                        .setMessage("Need to open your gps.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(AdminActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        //  locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
}
