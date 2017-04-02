package com.example.hubert.organiser;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.FloatProperty;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Hubert on 2017-03-25.
 */





public class GpsService extends Service {
    private static final String TAG = "GPSTEST";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;
        Float minDist = 300f;


        public LocationListener(String provider) {
            Log.d(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);

        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            Log.d(TAG, "location changed: " + Double.toString( location.getLatitude() ) + " " + Double.toString( location.getAltitude() ));


            DataBase db = new DataBase(getApplicationContext());
            String[] setNames = {"id","title","description","priority","day","month","year","time","details","checked"};
            Cursor el = db.getTasks(setNames);
            while (el.moveToNext()){
                Task ntask = new Task();
                    ntask.setTask(el.getInt(0), el.getString(1), el.getString(2), el.getInt(3), el.getInt(4), el.getInt(5), el.getInt(6), el.getInt(7), el.getString(8), el.getInt(9)>0 );
                if(!ntask.getChecked() && ntask.getDetails()!= null) {
                    try {
                        String coords = (new JSONObject(ntask.getDetails())).getString("Lokalizacja");
                        if(!coords.equals("")) {
                            Log.d("TAG", "COORDS OF TASK " + coords);
                            double lat1 = location.getLatitude();
                            double long1 = location.getLongitude();


                            String[] latlong = coords.split(",");
                            double lat2 = Double.parseDouble(latlong[0]);
                            double long2 = Double.parseDouble(latlong[1]);

                            float[] dist = new float[1];
                            Location.distanceBetween(lat1, long1, lat2, long2, dist);
                            Log.d("TAG", "dist" + Float.toString(dist[0]));

                            if (dist[0] <= minDist) {
                                Log.d("TAG", "checked!");
                                ntask.setChecked(true);
                                db.setCheckedTask(ntask.getId());
                                ((Tasks) getApplicationContext()).clearList();
                                ((Tasks) getApplicationContext()).loadTasks();
                                Toast.makeText(getApplication(), "You arrived on place: " + ntask.getTitle(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) { }
                }

            }



            //////////////////////////////
            //check for tasks to uncheck//
            //////////////////////////////
        }




        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        mLocationManager.removeUpdates(mLocationListeners[i]);
                        return;
                    }

                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.d(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}