package com.example.dragonball;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //Al mover
    Button btLocation;
    TextView textView1,textView2,textView3,textView4,textView5;
    FusedLocationProviderClient fusedLocationProviderClient;


    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;
    int whip = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btLocation=findViewById(R.id.bt_location);
        textView1= findViewById(R.id.text_view1);
        textView2= findViewById(R.id.text_view2);
        textView3= findViewById(R.id.text_view3);
        textView4= findViewById(R.id.text_view4);
        textView5= findViewById(R.id.text_view5);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    getLocation();

                }else{
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
             }
            }
        );


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor == null)
            stop();
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                float x = event.values[0];
                if (x < -5 && whip == 0) {

                    whip++;
                    getWindow().getDecorView().setBackgroundColor(Color.BLACK);

                }
                if (x > 5 && whip == 1) {
                    whip++;
                    getWindow().getDecorView().setBackgroundColor(Color.RED);

                }
                if (whip == 2) {
                    whip = 0;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }

        };
        start();
    }

    private void start() {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void stop() {
        sensorManager.unregisterListener(sensorEventListener);
    }

    @SuppressLint("MissingPermission")
    private void getLocation(){

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if(location != null){
                    try {
                          Geocoder geocoder= new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        textView1.setText(Html.fromHtml("<font color='<#6200EE'><b>Latitude :</b><br></font>"+addresses.get(0).getLatitude()));
                        textView2.setText(Html.fromHtml("<font color='<#6200EE'><b>Longitude :</b><br></font>"+addresses.get(0).getLongitude()));
                        textView3.setText(Html.fromHtml("<font color='<#6200EE'><b>Country Name :</b><br></font>"+addresses.get(0).getCountryName()));
                        textView4.setText(Html.fromHtml("<font color='<#6200EE'><b>Locality :</b><br></font>"+addresses.get(0).getLocality()));
                        textView5.setText(Html.fromHtml("<font color='<#6200EE'><b>Adress :</b><br></font>"+addresses.get(0).getAddressLine(0)));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}