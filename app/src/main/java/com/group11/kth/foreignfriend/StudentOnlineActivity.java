package com.group11.kth.foreignfriend;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class StudentOnlineActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mMail;
    private TextView mMessenger;
    private  TextView mWhatsapp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_online);

        // Clickable text
        mMail = (TextView) findViewById(R.id.mail_id);
        mMail.setOnClickListener(this);
        mMessenger = (TextView) findViewById(R.id.messenger_id);
        mMessenger.setOnClickListener(this);
        mWhatsapp = (TextView) findViewById(R.id.whatsapp_id);
        mWhatsapp.setOnClickListener(this);

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


    private void mailHandle(){

        Intent mainIntent = new Intent(Intent.ACTION_SEND);
        mainIntent.setData(Uri.parse("mailto:"));
        String[] recipient = {mMail.getText().toString()};
        mainIntent.putExtra(Intent.EXTRA_EMAIL, recipient);
        mainIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.email_subject);
        mainIntent.putExtra(Intent.EXTRA_TEXT,"Hey ");
        mainIntent.setType("message/rfc822");
        try {
            startActivity(Intent.createChooser(mainIntent, "Send Email"));
        }
        catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(this, "There is no mail clients installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void messengerHandle(){

    }

    private void whatsappHandle(){

        Uri uri = Uri.parse("smsto:"+"46729019405");
        Intent whatsIntent = new Intent(Intent.ACTION_SENDTO, uri );
        whatsIntent.putExtra(Intent.EXTRA_TEXT, "Hey ");
        whatsIntent.setType("text/plain");
        whatsIntent.setPackage("com.whatsapp");
        try {
            startActivity(whatsIntent);

        }
        catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Whatsapp is not installed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        if(i == R.id.mail_id){
            mailHandle();
        }
        else if( i == R.id.whatsapp_id){
            whatsappHandle();
        }
        else if(i == R.id.messenger_id){
            messengerHandle();
        }
    }
}
