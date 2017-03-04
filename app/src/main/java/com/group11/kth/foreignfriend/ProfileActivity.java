package com.group11.kth.foreignfriend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener
                {


    // variables
    public static final int REQUEST_IMAGE_PICTURE = 1;
    public SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = this.getSharedPreferences(getString(R.string.user_log_status_file), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_profile);
        String name = sharedPref.getString(getString(R.string.user_name),"Name");
        String pictureUrl = sharedPref.getString(getString(R.string.user_profile_picture_url),"null");
        // clikcable text
        findViewById(R.id.settings_id).setOnClickListener(this);
        findViewById(R.id.contact_id).setOnClickListener(this);
        findViewById(R.id.delete_account_id).setOnClickListener(this);
        findViewById(R.id.sign_out_id).setOnClickListener(this);
        findViewById(R.id.editPicButton).setOnClickListener(this);
        TextView v = (TextView) findViewById(R.id.user_profile_name);
        v.setText(name);
        ImageView image = (ImageView) findViewById(R.id.user_profile_picture);
        /* load image */
        Picasso.with(this)
                .load(pictureUrl)
                .error(R.drawable.profile_barack)
                .into(image);
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        int id =  android.os.Process.myPid();
        android.os.Process.killProcess(id);
    }

    // handle change of background picture
    private void changeBackgroundPicture(){
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePicture, REQUEST_IMAGE_PICTURE);
        }
    }

    // handle click of text
    private void  contactsHandle(){
        Intent intent = new Intent(this, ContactsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    private void settingsHandle(){
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    private void deleteAccountHandle(){
        showDialog();
    }

    private void signOutHandle(){
        // Firebase sign out
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        sharedPref  = this.getSharedPreferences(getString(R.string.user_log_status_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.user_log_status),0);
        editor.commit();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);

    }

    @Override
    public void onClick(View view) {

        int i = view.getId();
        if(i == R.id.contact_id){
            contactsHandle();
        }
        else if(i == R.id.settings_id){
            settingsHandle();
        }
        else if(i == R.id.delete_account_id){
            deleteAccountHandle();
        }
        else if (i == R.id.sign_out_id){
           signOutHandle();
        }
        else if (i == R.id.editPicButton){
            changeBackgroundPicture();
        }
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_account_title)
                .setMessage(R.string.delete_account_message)
                .setPositiveButton(R.string.delete_account_ok, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // delete account and stop application
                        deleteAccount();
                    }
                });
        builder.setNegativeButton(R.string.delete_account_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // return to app
            }
        });

        builder.show();

    }


    private void deleteAccount(){
        /*
        * clear cache
        * delete account
         */


        onDestroy();
    }
}
