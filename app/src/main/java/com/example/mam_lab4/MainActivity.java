package com.example.mam_lab4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private LocationManager lm;
    private LocationListener listener;
    private TextView tv;
    private TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new MyListener();
        tv = (TextView) findViewById(R.id.loctv);
        tv2 = (TextView) findViewById(R.id.loctv2);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;
        }
        registerListener();
    }

    void registerListener() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(requestCode == 0){
                Toast.makeText(this, "Access to fine location granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class MyListener implements LocationListener {

        Location prevLocation = null;
        float speed;

        private void przetwarzajLokalizacje(Location location) {
            String info;
            info = "\nLatitude: " + location.getLatitude()+ "\nLongitude: " + location.getLongitude();
            if (prevLocation != null) {
                float bearing = prevLocation.bearingTo(location);
                float distance = prevLocation.distanceTo(location);
                tv2.setText("Previous Location:\n" + info);
                info += "\nDistance: "+distance+"\nBearing: "+bearing;
            }
            if(location.hasSpeed()){
                if(location.getSpeed() != 0) {
                    speed = location.getSpeed();
                    info = info + "\nSpeed: " + speed + " m/s";
                }
            }
            tv.setText("Current Location:\n" + info);
            prevLocation = location;
        }

        @Override
        public void onLocationChanged(Location location) {
            przetwarzajLokalizacje(location);
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){ }
        @Override
        public void onProviderEnabled(String provider) { }
        @Override
        public void onProviderDisabled(String provider) { }
    }


}
