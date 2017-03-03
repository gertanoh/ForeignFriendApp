package com.group11.kth.foreignfriend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import  com.google.android.gms.location.LocationListener;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import android.Manifest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener,
        GoogleMap.OnMarkerClickListener{

    public GoogleMap mMap;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


   /* List<String> coursearray = Arrays.asList(getResources().getStringArray(R.array.courses);
    List<String> fieldsarray = Arrays.asList(getResources().getStringArray(R.array.fields));*/
    /*List<String> coursearray = new ArrayList<String>(Arrays.asList("ID001","ID002","ID003","ID004","ID005"));
    List<String> fieldsarray = new ArrayList<String>(Arrays.asList("IT","Math","Physics"));*/
    List<String> coursearray = new ArrayList<String>(Arrays.asList("ID001"));
    List<String> fieldsarray = new ArrayList<String>(Arrays.asList("Physics"));

    private GoogleApiClient client;
    private LocationManager locationManager;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    HashMap<String,Marker> existingMarkers = new HashMap<String, Marker>();
    String userId = "juanluisrto";
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 5;


    DatabaseReference userRef = database.getReference("users/" + userId);
    DatabaseReference filtersRef = database.getReference("filters/");

     SharedPreferences  mPrefs;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        client.connect();

        //Saving location locally to use between activities
        mPrefs = this.getSharedPreferences("location",Context.MODE_PRIVATE);


        //Bottom navigation View
        findViewById(R.id.filter).setOnClickListener((View.OnClickListener) this);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile_id:
                        Intent intent;
                        intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        break;

                    case R.id.home_id:
                        Intent intent1;
                        intent1 = new Intent(getApplicationContext(), MapsActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent1);
                }
                return false;
            }
        });


    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
            }
        }
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
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);

<<<<<<< HEAD
=======

        //...............................................
        // Axel: added call to style the map
        // Go here to make a new style https://mapstyle.withgoogle.com/
        // Place the file in \app\src\main\res\raw

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.styled_map));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }
        //.....................................................


        LatLng student1 = new LatLng(59.346098, 18.072738);
        LatLng student2 = new LatLng(59.347970, 18.068914);
        LatLng student3 = new LatLng(59.349006, 18.074619);
        LatLng student4 = new LatLng(59.346477, 18.076880);
>>>>>>> b10f3c892bbba85d11cceb3cb226d0e1f284cff9


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            Toast.makeText(this, "Location not working", Toast.LENGTH_LONG).show();
        }




        float zoomLevel = (float) 16.0; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(59.346784, 18.070724), zoomLevel));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        android.location.LocationListener ll = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                mMap.animateCamera(cameraUpdate);
                updateLocation(latLng);
                Toast.makeText(MapsActivity.this, latLng.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras){
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

        };
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME,MIN_DISTANCE,ll);



        //Create listeners to the locations in the filters I am subscribed to

        final ChildEventListener filterListener = new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Marker m = mMap.addMarker(new MarkerOptions().position(parseLatLng(dataSnapshot)).title(dataSnapshot.getKey()));
                existingMarkers.put(dataSnapshot.getKey(), m);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
               Marker oldM = existingMarkers.get(dataSnapshot.getKey());
                //MarkerOptions replaceMarker = new MarkerOptions().position(parseLatLng(dataSnapshot)).title(dataSnapshot.getKey());
                Toast.makeText(MapsActivity.this, dataSnapshot.toString(), Toast.LENGTH_LONG).show();
               oldM.setPosition(parseLatLng(dataSnapshot));
                /*Marker newM = mMap.addMarker(replaceMarker);
                oldM.remove();
                existingMarkers.remove(dataSnapshot.getKey());
                existingMarkers.put(dataSnapshot.getKey(),newM);*/
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Marker m = existingMarkers.get(dataSnapshot.getKey());
                m.remove();
                existingMarkers.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        // takes care of creating and deleting listeners once filters are activated/deactivated
        ChildEventListener my_own_filters_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String path = dataSnapshot.getRef().getParent().getKey() + "/" + dataSnapshot.getValue(); //[course-field]/name
                filtersRef.child(path).addChildEventListener(filterListener);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String path = dataSnapshot.getRef().getParent().getKey() + "/" + dataSnapshot.getValue(); //[course-field]/name
                filtersRef.child(path).removeEventListener(filterListener);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

            //creates a listener to every course and field in which one is subscribed
        for ( String course : coursearray){
            filtersRef.child(course).addChildEventListener(filterListener);
        }
        for ( String field : fieldsarray){
            filtersRef.child(field).addChildEventListener(filterListener);
        }
       userRef.child("filters/courses").addChildEventListener(my_own_filters_listener);
       userRef.child("filters/fields").addChildEventListener(my_own_filters_listener);










    }
    public LatLng parseLatLng(DataSnapshot dataSnapShot){
        //String[] latlong = dataSnapShot.getValue().toString().split(",");
        double latitude = (double) dataSnapShot.child("latitude").getValue();
        double longitude = (double) dataSnapShot.child("longitude").getValue();
        LatLng latlng = new LatLng(latitude, longitude);
        return latlng;
    }





    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Maps Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public void goToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filter:
                Intent i = new Intent(getApplicationContext(), FilterActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
        }
    }


    void updateLocation(LatLng latLng){

        SharedPreferences.Editor editor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(latLng);
        editor.putString("location", json);
        editor.commit();



       userRef.child("location").setValue(latLng);

        Map<String, Object> filterUpdates = new HashMap<String, Object>();
        for (String ftoAdd :fieldsarray) {
            filterUpdates.put("fields/" + ftoAdd + "/" + userId, latLng);
        }
        for (String ctoAdd :coursearray){
            filterUpdates.put("courses/" + ctoAdd + "/" + userId, latLng);
        }

        filtersRef.updateChildren(filterUpdates); //updates the value of my location in every course adn field I am subscribed to

   }





    @Override
    public boolean onMarkerClick(Marker marker) {
        // to be detailed
        Intent intent = new Intent(this, StudentOnlineActivity.class);
        startActivity(intent);
        return false;
    }

}