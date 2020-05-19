package com.ipk.mobilodev;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


public class LocationActivity extends AppCompatActivity implements LocationListener {

    private FusedLocationProviderClient fusedLocationClient;
    private double loc_latitude, loc_longitude;
    Button btn_pull,btn_send;
    TextView txt;

    //ActivityRecog
    private TextView stillCounterText, movingCounterText, tiltingCounterText, activity, confid;
    private int stillCount, tiltingCount, movingCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        btn_pull=findViewById(R.id.loc_pull);
        btn_send=findViewById(R.id.loc_send);
        txt=findViewById(R.id.loc_text);

        //activityRecog
        stillCounterText = findViewById(R.id.loc_still);
        movingCounterText = findViewById(R.id.loc_running);
        tiltingCounterText=findViewById(R.id.loc_tilting);
        activity=findViewById(R.id.loc_activity);
        confid=findViewById(R.id.loc_confidence);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION)!=PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            loc_latitude=location.getLatitude();
                            loc_longitude=location.getLongitude();
                        }
                    }
                });
        btn_pull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("takip","girdi");
                txt.setText("latitude: "+loc_latitude+"\nlongitude: "+ loc_longitude);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String whatsAppMessage = "https://maps.google.com/?q=" + loc_latitude + "," + loc_longitude;
                //https://maps.google.com/maps?saddr=   //kullanırsan sadece o nokta değil oradan gidilecek yer soruyor!!!
                try{
                    // Geocoder geocoder=new Geocoder(getApplicationContext());
                    //List<Address> address= geocoder.getFromLocation(loc_latitude, loc_longitude, 1);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, whatsAppMessage);
                    //sendIntent.putExtra(Intent.EXTRA_TEXT, address.get(0).toString());
                    sendIntent.setType("text/plain");
                    //sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //activityrecog
        /*startTrackingActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalBroadcastManager.getInstance(LocationActivity.this).registerReceiver(mActivityBroadcastReceiver,
                        new IntentFilter("activity_intent"));

                startService(new Intent(LocationActivity.this, ActivityDetectionService.class));
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("takip", "onStart():start ActivityDetectionService");
        LocalBroadcastManager.getInstance(this).registerReceiver(mActivityBroadcastReceiver,
                new IntentFilter("activity_intent"));

        startService(new Intent(this, ActivityDetectionService.class));
    }

    BroadcastReceiver mActivityBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Log.d(TAG, "onReceive()");
            if (intent.getAction().equals("activity_intent")) {
                int type = intent.getIntExtra("type", -1);
                int confidence = intent.getIntExtra("confidence", 0);
                handleUserActivity(type, confidence);
            }
        }
    };

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //activityrecog
    private void handleUserActivity(int type, int confidence) {
        String label = "Unknown";
        switch (type) {
            case DetectedActivity.ON_FOOT:
            case DetectedActivity.WALKING:
            case DetectedActivity.ON_BICYCLE:
            case DetectedActivity.TILTING:{
                label="Tilting";
                break;
            }
            case DetectedActivity.IN_VEHICLE:
            case DetectedActivity.RUNNING: {
                label = "Moving";
                break;
            }
            case DetectedActivity.STILL: {
                label = "Still";
                break;
            }
        }

        Log.d("takip", "broadcast:onReceive(): Activity is " + label
                + " and confidence level is: " + confidence);

        activity.setText("Aktivite: "+label);
        confid.setText("Yüzde: "+confidence+"");

        if(label == "Moving" ){
            movingCounterText.setText("Moving Counter: " + ++movingCount);
        }else if(label == "Still"){
            stillCounterText.setText("Still Counter: " + ++stillCount);
        }else if(label == "Tilting"){
            tiltingCounterText.setText("Tilting Counter: "+ ++tiltingCount);
        }


    }

    //request updates eklenebilir :)

    //eski bebek kod
    /*private FusedLocationProviderClient fusedLocationClient;  //bunda locationListener app
    private double loc_latitude, loc_longitude;
    Button btn_pull,btn_send;
    TextView txt;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        btn_pull=findViewById(R.id.loc_pull);
        btn_send=findViewById(R.id.loc_send);
        txt=findViewById(R.id.loc_text);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            Log.d("takip", "disconnect");
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.d("takip","hello");
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            loc_latitude=location.getLatitude();
                            loc_longitude=location.getLongitude();
                        }
                        else{
                            Log.d("takip","null");
                        }
                    }
                });


        btn_pull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("takip","girdi");
                txt.setText("latitude: "+loc_latitude+"\nlongitude: "+ loc_longitude);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String whatsAppMessage = "https://maps.google.com/?q=" + loc_latitude + "," + loc_longitude;
                //https://maps.google.com/maps?saddr=   //kullanırsan sadece o nokta değil oradan gidilecek yer soruyor!!!
                try{
                   // Geocoder geocoder=new Geocoder(getApplicationContext());
                    //List<Address> address= geocoder.getFromLocation(loc_latitude, loc_longitude, 1);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, whatsAppMessage);
                    //sendIntent.putExtra(Intent.EXTRA_TEXT, address.get(0).toString());
                    sendIntent.setType("text/plain");
                    //sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onLocationChanged(Location location) {

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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }*/
}
