package com.group11.kth.foreignfriend;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

// Axel Hult 2017-02-14:
// Acitivity for applying filters to the results on the map
// From youtube tutorial https://www.youtube.com/watch?v=wfADRuyul04

public class FilterActivity extends AppCompatActivity {


    // FireBase, refernce to
    //public DatabaseReference mRootRef;


    // Buttons and views for fields and courses
    Button fields, courses;
    TextView fieldsSelected,courseSelected;

    //Arrays for courses, choices and list for checked items
    String[]coursearray;
    boolean[] checkedcourses;
    ArrayList<Integer>courselist = new ArrayList<>();

    String[]fieldsarray;
    boolean[] checkedfields;
    ArrayList<Integer>fieldslist = new ArrayList<>();
    public SharedPreferences sharedPref;


    // Make these static to keep the value

    static String user_latitude;
    static  String user_longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = this.getSharedPreferences(getString(R.string.user_log_status_file), Context.MODE_PRIVATE);

        final String id = sharedPref.getString(getString(R.string.user_id), "NoFacebookID");
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        SharedPreferences  mPrefs;

        setContentView(R.layout.activity_filter);

        // Reference to the ROOT!
        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        // Refernce to subfield!!
        //final DatabaseReference subref = mRootRef.child("test");

        // ............... Hashmaps ..............
        // Holds courses and fields selected
        // Uppdated in the "done" button

        final HashMap<String,Object> fieldsmap = new HashMap<String, Object>();

        final HashMap<String,Object> coursemap = new HashMap<String, Object>();



        // .................. ACTIONS FOR NAVBAR ........................
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    if (item.getItemId()==R.id.profile_id) {

                        Intent intent2;
                        intent2 = new Intent(getApplicationContext(), ProfileActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent2);

                    }

                if (item.getItemId()==R.id.home_id){

                        Intent intent1;
                        intent1 = new Intent(getApplicationContext(), MapsActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent1);
                }
                return false;
            }
        });


        // ................... ACTION FOR DONE BUTTON ....................

        Button donebutton = (Button) findViewById(R.id.donebtn);
        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //i++;

                // ........... Update courses and fields tied to a specific user ..............

                mRootRef.child("Users").child(id).child("filters").child("fields").updateChildren(fieldsmap);
                mRootRef.child("Users").child(id).child("filters").child("courses").updateChildren(coursemap);


                // ...... Get latitude value........
                mRootRef.child("Users").child(id).child("location").child("latitude").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        // ... get the users longitude
                        user_latitude=snapshot.getValue().toString();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

               // mRootRef.child("Users").child(id).child("location").child("longitude").get;

                //..... Get longitude value ........
                mRootRef.child("Users").child(id).child("location").child("longitude").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        // ... get the users longitude
                        user_longitude=snapshot.getValue().toString();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



                Toast.makeText(FilterActivity.this, "long"+user_longitude, Toast.LENGTH_LONG).show();
                Toast.makeText(FilterActivity.this, "lat"+user_latitude, Toast.LENGTH_LONG).show();

                // ..... Update longitude and latitude in the courses/fields

                //mRootRef.child("Users").child(id).child("filters").child("courses").child("he1208").setValue()





                //String lat = mRootRef.child("Users").child(id).child("location").child("latitude").get;
                //Toast.makeText(FilterActivity.this, lat, Toast.LENGTH_LONG).show();

                // String longit =


                //mRootRef.child("filters").child("courses").child("he1208").child(id).child("latitude").setValue(mRootRef.child("Users").child(id).child("location"));

                // Loop for updating courses

                // ................ Overwrite old ones ...................
                /*
                for (int j = 0; j < 6; j++){

                    String n = String.valueOf(j);
                    mRootRef.child("users").child("axelhult").child("filters").child("courses").child(n).setValue(0);
                }

                for (int k=0; k<(courselist.size()); k++) {
                    String n = String.valueOf(k);
                    mRootRef.child("users").child("axelhult").child("filters").child("courses").child(n).setValue((coursearray[courselist.get(k)]));

                }
                */

                // Loop for updating fields in database

                // Max 6 fields right now!

                // ........... Overwrite old ones .....................
                /*
                for (int p = 0; p < 6; p++){

                    String m = String.valueOf(p);
                    mRootRef.child("users").child("axelhult").child("filters").child("fields").child(m).setValue(0);

                }
                */


                //mRootRef.child("users").child("axelhult").child("filters").child("fields").removeValue();
                //HashMap<String,Object> updateFilters = new HashMap<String, Object>();
                //updateFilters.put("ID001",true);
                //updateFilters.put("HL1208", true);
                //updateFilters.put("MM0000", true);

               // updateFilter.put(111, null

                /*
                for (int i=0; i<(fieldslist.size()); i++) {
                    String s = String.valueOf(i);
                    mRootRef.child("users").child("axelhult").child("filters").child("fields").child(s)
                            .setValue((fieldsarray[fieldslist.get(i)]));

                }
                */

               // mRootRef.child("users").child("axelhult").child("filters").child("fields").updateChildren(updateFilters);





                // ..........................................................
                Intent intent3 = new Intent (getApplicationContext(), MapsActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent3);
            }
        });


        fields = (Button) findViewById(R.id.fieldbtn);
        courses = (Button) findViewById(R.id.coursebtn);

        fieldsSelected = (TextView) findViewById(R.id.fieldsview);
        courseSelected = (TextView) findViewById(R.id.courseview);

        //Pass item from listitem
        coursearray=getResources().getStringArray(R.array.courses);
        fieldsarray=getResources().getStringArray(R.array.fields);

        // empty boolean array of correct size
        checkedcourses=new boolean[coursearray.length];
        checkedfields=new boolean[fieldsarray.length];

        fields.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(FilterActivity.this);
                mbuilder.setTitle("FIELDS");
                mbuilder.setMultiChoiceItems(fieldsarray, checkedfields, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int pos, boolean isChecked) {

                        //When user selects item... add OR remove
                        if(isChecked){

                            if(! fieldslist.contains(pos)){
                                fieldslist.add(pos);

                                //updateFilters2.put("hej", true);
                                //updateFilters2.put(fieldsarray[fieldslist.get(pos)].toString(), true);

                                //mRootRef.child("Users").child("55").child("test").setValue(coursearray[fieldslist.get(1)]);

                            }
                            }else if(fieldslist.contains(pos)){

                                //mRootRef.child("users").child("axelhult").child("filtes").child("fields").removeValue(fieldsarray[fieldslist.get(pos)]

                               // updateFilters2.put(fieldsarray[fieldslist.get(pos)].toString(), null);

                                //updateFilters2.put(fieldslist.get(pos).toString(), null);
                                fieldslist.remove(Integer.valueOf(pos));


                        }
                    }
                });

                //
                mbuilder.setCancelable(false);
                mbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";

                        // clear old ones...
                        fieldsmap.put("Chemistry", null);
                        fieldsmap.put("Math", null);
                        fieldsmap.put("IT", null);
                        fieldsmap.put("Business", null);
                        fieldsmap.put("Physics", null);


                        for (int i=0;i< fieldslist.size(); i++){
                            item = item+"   "+fieldsarray[fieldslist.get(i)];
                            //add comma, tab, new line or somehting here?

                            fieldsmap.put(fieldsarray[fieldslist.get(i)], true); // update new ones

                        }
                        fieldsSelected.setText(item);
                    }
                });

                //Dismiss button
                mbuilder.setNegativeButton("Dissmiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                //Clear all selections
                mbuilder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // clear old ones...

                        fieldsmap.put("Chemistry", null);
                        fieldsmap.put("Math", null);
                        fieldsmap.put("IT", null);
                        fieldsmap.put("Business", null);
                        fieldsmap.put("Physics", null);


                        for (int i =0; i<checkedfields.length; i++){
                            checkedfields[i]=false;
                            fieldslist.clear();
                            fieldsSelected.setText("No fields selected");

                        }
                    }
                });
                AlertDialog mdialog = mbuilder.create();
                mdialog.show();
            }
        });     //fieldslistener done



        //..................................................................
        //                  SAME STUFF FOR COURSES...
        //...................................................................

        courses.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(FilterActivity.this);
                mbuilder.setTitle("COURSES");
                mbuilder.setMultiChoiceItems(coursearray, checkedcourses, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int pos, boolean isChecked) {

                        //When user selects item... add OR remove
                        if(isChecked){
                            if(! courselist.contains(pos)){
                                courselist.add(pos);
                            }
                        }else if(courselist.contains(pos)){
                            courselist.remove(Integer.valueOf(pos));
                        }
                    }
                });

                //

                mbuilder.setCancelable(false);
                mbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";

                        // clear old ones...
                        coursemap.put("ID2216", null);
                        coursemap.put("IS1200", null);
                        coursemap.put("HE1208", null);
                        coursemap.put("SK1101", null);
                        coursemap.put("AD001", null);
                        coursemap.put("AC0002", null);

                        for (int i=0;i< courselist.size(); i++){
                            item = item+"   "+coursearray[courselist.get(i)];
                            //add comma, tab, new line or somehting here?

                            coursemap.put(coursearray[courselist.get(i)], true);
                        }
                        courseSelected.setText(item);
                    }
                });

                //Dismiss button
                mbuilder.setNegativeButton("Dissmiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                //Clear all selections
                mbuilder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        coursemap.put("ID2216", null);
                        coursemap.put("IS1200", null);
                        coursemap.put("HE1208", null);
                        coursemap.put("SK1101", null);
                        coursemap.put("AD001", null);
                        coursemap.put("AC0002", null);

                        for (int i =0; i<checkedcourses.length; i++){
                            checkedcourses[i]=false;
                            courselist.clear();
                            courseSelected.setText("No fields selected");

                        }
                    }
                });
                AlertDialog mdialog = mbuilder.create();
                mdialog.show();
            }
        });



    }


} //activity
