package com.group11.kth.foreignfriend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener
                {


    // variables
    public static final int REQUEST_IMAGE_PICTURE = 1;
   // private EditText
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String name = LoginActivity.user.getDisplayName();
        TextView t = (TextView)findViewById(R.id.user_profile_name);
        t.setText(name);

       /* Uri picture = LoginActivity.user.getPhotoUrl();
        ImageView v = (ImageView) findViewById(R.id.user_profile_picture);
        v.setImageURI(null);
        v.setImageURI(picture);*/
        // clikcable text
        findViewById(R.id.settings_id).setOnClickListener(this);
        findViewById(R.id.contact_id).setOnClickListener(this);
        findViewById(R.id.delete_account_id).setOnClickListener(this);
        findViewById(R.id.sign_out_id).setOnClickListener(this);
        findViewById(R.id.editPicButton).setOnClickListener(this);
        TextView v = (TextView) findViewById(R.id.user_bio);
        TextView v1 = (TextView) findViewById(R.id.general_des);
        //v1.addTextChangedListener(TextWatcher);
        //v.setText(v1.getText());
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

  /*  private void signOutHandle(){
        // Firebase sign out
        LoginActivity.mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    }
                });
    }*/

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
           // signOutHandle();
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
