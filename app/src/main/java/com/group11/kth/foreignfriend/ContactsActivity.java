package com.group11.kth.foreignfriend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StreamDownloadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {


    boolean value_contacts_updated = false;
    public SharedPreferences sharedPref;
    final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sharedPref = this.getSharedPreferences(getString(R.string.user_log_status_file), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_contacts);
        String email = sharedPref.getString(getString(R.string.user_email),null);
        String name = sharedPref.getString(getString(R.string.user_name),null);
        String pictureUrl = sharedPref.getString(getString(R.string.user_profile_picture_url),"null");
        String id = sharedPref.getString(getString(R.string.user_id),"null");
        TextView tName = (TextView)findViewById(R.id.user_profile_name);
        tName.setText(name);
        TextView tEmail = (TextView)findViewById(R.id.email_contact_id);
        tEmail.setText(email);
        final TextView tPhone = (TextView) findViewById(R.id.phoneNumber);
        final TextView tWhatsapp = (TextView) findViewById(R.id.whatsapp);
         /* get number from firebase database */
        rootRef.child(getString(R.string.Users)).child(id).child(getString(R.string.Phone))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tPhone.setText((String)dataSnapshot.getValue());
                       // Toast.makeText(getApplicationContext(), "CONTACTS ACTIVITY", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        rootRef.child(getString(R.string.Users)).child(id).child(getString(R.string.Whatsapp))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tWhatsapp.setText((String)dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        Button edit = (Button)findViewById(R.id.edit_btn);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View mview = getLayoutInflater().inflate(R.layout.dialog_edit_numbers, null);
                AlertDialog.Builder m  = new AlertDialog.Builder(ContactsActivity.this);
                m.setView(mview);
                m.setTitle("ForeignFriend");

                final EditText mwhatsapp = (EditText) mview.findViewById(R.id.whatsapp);
                final EditText mphone = (EditText) mview.findViewById(R.id.phone_number);
                m.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // set Phone numbers
                        tPhone.setText(mwhatsapp.getText().toString());
                        tWhatsapp.setText(mphone.getText().toString());
                        value_contacts_updated = true;
                    }
                });
                m.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                m.show();
            }
        });


        Button done = (Button) findViewById(R.id.donebtn);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // compare and save to database
                if(value_contacts_updated){
                    // update phone numbers
                    Map<String , Object> values = new HashMap<String, Object>();
                    values.put(getString(R.string.Users)+"/"+LoginActivity.id+"/"+getString(R.string.Phone),
                            (Object)tPhone.getText().toString());
                    values.put(getString(R.string.Users)+"/"+LoginActivity.id+"/"+getString(R.string.Whatsapp),
                            (Object)tWhatsapp.getText().toString());
                    rootRef.updateChildren(values);


                }

                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        /* load image */
        ImageView profile_picture = (ImageView)findViewById(R.id.user_profile_picture);
       // Utils.loadImage(profile_picture, LoginActivity.id, getApplicationContext());
        Picasso.with(this)
                .load(pictureUrl)
                .placeholder(R.drawable.profile_barack)
                .error(R.drawable.profile_barack)
                .into(profile_picture);


        /* get number from firebase database */
        rootRef.child(getString(R.string.Users)).child(id).child(getString(R.string.Phone))
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    tPhone.setText((String)dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        rootRef.child(getString(R.string.Users)).child(id).child(getString(R.string.Whatsapp))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tWhatsapp.setText((String)dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onClick(View v) {


    }
}
