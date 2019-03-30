package com.example.hikerswatch;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
           startListening();
        }
    }

    public void startListening(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        }
    }

    public void updateLocationInfo(Location location){
        TextView latext = (TextView) findViewById(R.id.textView2);
        TextView lotext = (TextView) findViewById(R.id.textView3);
        TextView acctext = (TextView) findViewById(R.id.textView4);
        TextView altext = (TextView) findViewById(R.id.altitude);
        TextView addtext = (TextView) findViewById(R.id.Address);

        latext.setText("Lattitude : " + location.getLatitude());
        lotext.setText("Longitude : " + location.getLongitude());
        acctext.setText("Accuracy : " + location.getAccuracy());
        altext.setText("Altitude : " + location.getAltitude());

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            String address = "Could not find";
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            if(addressList != null && addressList.size() > 0){
                if(addressList.get(0).getSubThoroughfare() != null){
                    address += addressList.get(0).getSubThoroughfare() + " ";
                }

                if(addressList.get(0).getThoroughfare() != null){
                    address += addressList.get(0).getThoroughfare() + "\n";
                }

                if(addressList.get(0).getLocality() != null){
                    address += addressList.get(0).getLocality() + "\n";
                }

                if(addressList.get(0).getPostalCode() != null){
                    address += addressList.get(0).getPostalCode() + "\n";
                }

                if(addressList.get(0).getCountryName() != null){
                    address += addressList.get(0).getCountryName() + "\n";
                }
            }
            addtext.setText(address);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager =(LocationManager)this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Log.i("Location info", location.toString());
                updateLocationInfo(location);
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
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                updateLocationInfo(location);
            }
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }
}
