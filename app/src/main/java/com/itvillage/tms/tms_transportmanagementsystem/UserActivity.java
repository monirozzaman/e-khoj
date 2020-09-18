package com.itvillage.tms.tms_transportmanagementsystem;


import android.*;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itvillage.tms.tms_transportmanagementsystem.asystask.LocationAsynTask;
import com.itvillage.tms.tms_transportmanagementsystem.dto.response.UserDetailsResponse;
import com.itvillage.tms.tms_transportmanagementsystem.util.Utility;

import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import im.delight.android.location.SimpleLocation;


public class UserActivity extends AppCompatActivity {
    Button hospitalCard, policeStationCard, fireServiceCard, atmBoothCard,helpCard;
    ImageView setting;
    TextView namTextView, emailTextView;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

        hospitalCard = findViewById(R.id.hospital);
        policeStationCard = findViewById(R.id.police_station);
        fireServiceCard = findViewById(R.id.fire_service);
        atmBoothCard = findViewById(R.id.atm_booth);
        helpCard = findViewById(R.id.help);

        setting = findViewById(R.id.setting);

        namTextView = findViewById(R.id.nameTextView);

        emailTextView = findViewById(R.id.emailTextView);

        setProfileDetails();

        hospitalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nexiActivity("Hospital");
            }
        });
        policeStationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nexiActivity("Police Station");
            }
        });
        fireServiceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nexiActivity("Fire Station");
            }
        });
        atmBoothCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nexiActivity("ATM Booth");
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nexiActivity("setting");
            }
        });
        helpCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAHelpRequestIn();
            }
        });



    }

    private void sendAHelpRequestIn() {
        if (isLocationEnabled()) {

            LocationAsynTask locationAsynTask = new LocationAsynTask(getApplicationContext());
            locationAsynTask.execute();
            try {
                SimpleLocation simpleLocation = locationAsynTask.get();
                if (simpleLocation == null) {
                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent2 = new Intent();
                    intent2.setAction(Intent.ACTION_SEND);
                    intent2.setType("text/html");
                    intent2.putExtra(Intent.EXTRA_EMAIL, new String[]{"eproni29@gmail.com"});
                    intent2.putExtra(Intent.EXTRA_SUBJECT, "Need Help");
                    intent2.putExtra(Intent.EXTRA_TEXT, "https://maps.google.com/?ll="+simpleLocation.getLatitude()+","+simpleLocation.getLongitude());
                    startActivity(intent2);

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

    private void setProfileDetails() {

        emailTextView.setText(UserDetailsResponse.email);
        //TODO : add Designation in DesignationTextView
        emailTextView.setText(UserDetailsResponse.email);

    }



    private void nexiActivity(String requestType) {
        Log.e("-----------------",requestType);
        namTextView.setText(UserDetailsResponse.firstName + " " + UserDetailsResponse.lastName);
        Intent intent = new Intent(UserActivity.this, MapsActivity.class);
        intent.putExtra("requestType",requestType);
        startActivity(intent);
    }
    private boolean isLocationEnabled() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

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
                                ActivityCompat.requestPermissions(UserActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }

    }
}
