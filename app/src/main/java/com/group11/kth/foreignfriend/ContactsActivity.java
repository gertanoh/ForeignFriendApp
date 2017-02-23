package com.group11.kth.foreignfriend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        String name = LoginActivity.user.getEmail();
        TextView t = (TextView)findViewById(R.id.email_contact_id);
        t.setText(name);
    }
}
