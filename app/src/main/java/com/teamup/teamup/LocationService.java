package com.teamup.teamup;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

/**
 * Created by Rushabh on 12/5/15.
 */
public class LocationService {
    private static LocationService ourInstance = new LocationService();
    private LocationManager locationManager;
    private Location currentLocation;

    public static LocationService getInstance() {
        if(ourInstance == null ){
            ourInstance = new LocationService();
        }

        return ourInstance;
    }

    private LocationService() {
        Context appContext = Service.getInstance().getContext();
        locationManager = (LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener();

        if(checkPermission()) {
            System.out.println("Has permissions");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        }
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public boolean checkPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = Service.getInstance().getContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public Boolean displayGpsStatus() {
        ContentResolver contentResolver = Service.getInstance().getContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }


    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            currentLocation = loc;
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }
}
