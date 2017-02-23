package com.group11.kth.foreignfriend;

import android.content.DialogInterface;
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
import java.util.ArrayList;

// Axel Hult 2017-02-14:
// Acitivity for applying filters to the results on the map
// From youtube tutorial https://www.youtube.com/watch?v=wfADRuyul04

public class FilterActivity extends AppCompatActivity {

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);


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
