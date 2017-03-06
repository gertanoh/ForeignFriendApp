package com.group11.kth.foreignfriend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    public  FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public ProgressDialog bar;
    public static String id = null;
    public String name;
    public String email;

    private static final String TAG = "LoginActivity";
    public SharedPreferences sharedPref;
    public SharedPreferences.Editor editor;
    public static FirebaseUser user;

    public AccessTokenTracker tracker;
    public ProfileTracker profileTracker;

    public static String pictureUrl;
    public AccessToken accessToken;
    public Profile profile;

    public static int connect_value = -1 ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        /* get status */
        sharedPref  = this.getSharedPreferences(getString(R.string.user_log_status_file), Context.MODE_PRIVATE);

        connect_value = sharedPref.getInt(getString(R.string.user_log_status), -1);
        if(connect_value == 1)
        {
            openMapActivity();
        }
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        //Get the connectivity
        ConnectivityManager con = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nt = con.getActiveNetworkInfo();
        if(nt == null || !nt.isConnected()){
            Toast.makeText(this,"You need Internet Connection",Toast.LENGTH_SHORT).show();
        }

        callbackManager = CallbackManager.Factory.create();
        tracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                    accessToken = currentAccessToken;
            }
        };
        accessToken = AccessToken.getCurrentAccessToken();
        tracker.startTracking();
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                    // change profile picture
            }
        };
        profileTracker.startTracking();
        loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                /* retrieve user info */
                editor = sharedPref.edit();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            JSONObject data = response.getJSONObject();
                            String old_id;
                            id = object.getString("id");
                            name = object.getString("name");
                            email = object.getString("email");
                            pictureUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                            editor.putString(getString(R.string.user_id),id);
                            /* write id to real time database
                             * Image url to real time database
                             */
                            editor.putString(getString(R.string.user_name),name);
                            editor.putString(getString(R.string.user_email),email);
                            editor.putString(getString(R.string.user_profile_picture_url), pictureUrl);
                            old_id = sharedPref.getString(getString(R.string.user_id), "null");
                            editor.commit();

                          //  Toast.makeText(LoginActivity.this, "id:"+id, Toast.LENGTH_LONG).show();
                            //Toast.makeText(LoginActivity.this, "old id"+old_id, Toast.LENGTH_LONG).show();

                            if(!(id.equals(old_id))){
                              //  Toast.makeText(LoginActivity.this, "INSIDE IF-STATEMENt, NEW USER", Toast.LENGTH_LONG).show();

                                rootRef.child(getString(R.string.Users)).child(id).child(getString(R.string.Phone)).setValue("");
                                rootRef.child(getString(R.string.Users)).child(id).child(getString(R.string.Whatsapp)).setValue("");

                            }

                            rootRef.child(getString(R.string.Users)).child(id).child(getString(R.string.Mail)).setValue(email);
                            rootRef.child(getString(R.string.Users)).child(id).child(getString(R.string.Name)).setValue(name);
                            rootRef.child(getString(R.string.Users)).child(id).child("logedin").setValue("true");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields","id,name,email,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();

                editor.putInt(getString(R.string.user_log_status),1);
                editor.commit();
                openMapActivity();


            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }

            /* async task to download profile picture */

        });


    }

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // it is facebbok login
        // pass the value to the call back
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithCredential", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed badly.",
                            Toast.LENGTH_SHORT).show();
                }
                hideProgressDialog();



            }
        });
    }
    public void openMapActivity(){


        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
        startActivity(intent);
    }


    public void showProgressDialog() {
        if (bar == null) {
            bar = new ProgressDialog(this);
            bar.setMessage(getString(R.string.bar_message));
            bar.setIndeterminate(true);
        }

        bar.show();
    }
    public void hideProgressDialog() {
        if (bar != null && bar.isShowing()) {
            bar.dismiss();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        tracker.stopTracking();
        profileTracker.stopTracking();
    }
}
