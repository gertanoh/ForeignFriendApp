package com.group11.kth.foreignfriend;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactsActivity extends AppCompatActivity {

    public SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = this.getSharedPreferences(getString(R.string.user_log_status_file), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_contacts);
        String email = sharedPref.getString(getString(R.string.user_email),null);
        String name = sharedPref.getString(getString(R.string.user_name),null);
        TextView tName = (TextView)findViewById(R.id.user_profile_name);
        tName.setText(name);
        TextView tEmail = (TextView)findViewById(R.id.email_contact_id);
        tEmail.setText(email);
        /* load image */
        ImageView profile_picture = (ImageView)findViewById(R.id.user_profile_picture);
        Utils.loadImage(profile_picture, LoginActivity.id, getApplicationContext());
    }
}
