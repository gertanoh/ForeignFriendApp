package com.group11.kth.foreignfriend;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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

    int i =0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = this.getSharedPreferences(getString(R.string.user_log_status_file), Context.MODE_PRIVATE);

        String id = sharedPref.getString(getString(R.string.user_id), "123455");
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.child("Users").child(id).child("Add a field").setValue("I don't study MAth");
        setContentView(R.layout.activity_filter);

        // Reference to the ROOT!
        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        // Refernce to subfield!!
        //final DatabaseReference subref = mRootRef.child("test");

        mRootRef.child("filtertest").setValue("in the filter activity");


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

                // Make call to server here??

                // Make call to server here?? ............................

                // Try writing zero-elementh to datbase
                //String coursezero = courselist.get(1).toString();
                i++;


                mRootRef.child("filtertest").setValue("done");

                mRootRef.child("filtertest").setValue(fieldslist.toString());




                // Loop for updating courses

                // ................ Overwrite old ones ...................
                for (int j = 0; j < 6; j++){

                    String n = String.valueOf(j);
                    mRootRef.child("users").child("axelhult").child("filters").child("courses").child(n).setValue(0);
                }

                for (int k=0; k<(courselist.size()); k++) {
                    String n = String.valueOf(k);
                    mRootRef.child("users").child("axelhult").child("filters").child("courses").child(n).setValue((coursearray[courselist.get(k)]));

                }


                // Loop for updating fields in database

                // Max 6 fields right now!

                // ........... Overwrite old ones .....................
                for (int p = 0; p < 6; p++){

                    String m = String.valueOf(p);
                    mRootRef.child("users").child("axelhult").child("filters").child("fields").child(m).setValue(0);
                }

                for (int i=0; i<(fieldslist.size()); i++) {
                    String s = String.valueOf(i);
                    mRootRef.child("users").child("axelhult").child("filters").child("fields").child(s).setValue((fieldsarray[fieldslist.get(i)]));

                }




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
                            }
                            }else if(fieldslist.contains(pos)){
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
                        for (int i=0;i< fieldslist.size(); i++){
                            item = item+"   "+fieldsarray[fieldslist.get(i)];
                            //add comma, tab, new line or somehting here?
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

                        for (int i=0;i< courselist.size(); i++){
                            item = item+"   "+coursearray[courselist.get(i)];
                            //add comma, tab, new line or somehting here?
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
