package gr.mobap;
//Κώστας Αναγνώστου
//Περί Βουλής
//Costas Anagnostou
//Peri Voulis

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.installations.FirebaseInstallations;

import java.io.File;

import gr.mobap.diafaneia.Diafaneia;
import gr.mobap.ekdoseis.EkdoseisActivity;
import gr.mobap.images.Image;
import gr.mobap.komma.KommActivity;
import gr.mobap.mps.MpsActivity;
import gr.mobap.organosi.OrganosiActivity;
import gr.mobap.rss.activities.NeaActivity;
import gr.mobap.simera.CalendarActivity;
import gr.mobap.video.LiveVideoActivity;

public class MainActivity extends Base
        implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private Button mStatusTextView;
    private ImageView person;
    private CardView main1;
    private CardView main2;
    private CardView main3;
    private CardView main4;
    private CardView main5;
    private CardView main6;
    private CardView main7;
    private CardView main8;
    private CardView main9;
    /**
     * The {link Tracker} used to record screen views.
     */

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressBar mRegistrationProgressBar;
    private TextView mInformationTextView;
    private boolean isReceiverRegistered;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        String action = intent.getAction();
        String data = intent.getDataString();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        AppRate.app_launched(this);

        // π.χ. στο onCreate ή όταν ανοίγει οθόνη:
        AnalyticsTrackers.logScreen("Main", "MainActivity");

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mStatusTextView = findViewById(R.id.status);
        person = findViewById(R.id.image_profile_picture);
        main1 = findViewById(R.id.main1);
        main2 = findViewById(R.id.main2);
        main3 = findViewById(R.id.main3);
        main4 = findViewById(R.id.main4);
        main5 = findViewById(R.id.main5);
        main6 = findViewById(R.id.main6);
        main7 = findViewById(R.id.main7);
        main8 = findViewById(R.id.main8);
        main9 = findViewById(R.id.main9);
// Button listeners
        findViewById(R.id.sign_in).setOnClickListener(this);
        findViewById(R.id.main1).setOnClickListener(this);
        findViewById(R.id.main2).setOnClickListener(this);
        findViewById(R.id.main3).setOnClickListener(this);
        findViewById(R.id.main4).setOnClickListener(this);
        findViewById(R.id.main5).setOnClickListener(this);
        findViewById(R.id.main6).setOnClickListener(this);
        findViewById(R.id.main7).setOnClickListener(this);
        findViewById(R.id.main8).setOnClickListener(this);
        findViewById(R.id.main9).setOnClickListener(this);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.GOOGLE_SIGN_IN_ID))
                .requestEmail()
                .build();
        // [END config_signin]
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                for (UserInfo profile : user.getProviderData()) {
                    // Id of the provider (ex: google.com)
                    String providerId = profile.getProviderId();

                    // UID specific to the provider
                    String uid = profile.getUid();

                    // Name, email address, and profile photo Url
                    String name = profile.getDisplayName();
                    String email = profile.getEmail();
                    Uri photoUrl = profile.getPhotoUrl();
                }
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
            // [START_EXCLUDE]
            updateUI(user);
            // [END_EXCLUDE]
        };
        // [END auth_state_listener]
        FirebaseInstallations.getInstance().getId()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            Log.d("Installations", "Installation ID: " + task.getResult());
                        } else {
                            Log.e("Installations", "Unable to get Installation ID");
                        }
                    }
                });
    }

    @Override
    public File getCacheDir() {
        // NOTE: this method is used in Android 2.1
        return getApplicationContext().getCacheDir();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }


    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(MainActivity.this, task -> {
                    Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "signInWithCredential", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                    // [START_EXCLUDE]
                    hideProgressDialog();
                    // [END_EXCLUDE]
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            // display error
            Toast.makeText(MainActivity.this, getString(R.string.aneu_diktiou),
                    Toast.LENGTH_SHORT).show();
        }
    }
    // [END signin]

    private void signOut() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Firebase sign out
            mAuth.signOut();
            // Google sign out
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    status -> updateUI(null));
        } else {
            // display error
            Toast.makeText(MainActivity.this, getString(R.string.aneu_diktiou),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void revokeAccess() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Firebase sign out
            mAuth.signOut();
            // Google revoke access
            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                    status -> updateUI(null));
        } else {
            // display error
            Toast.makeText(MainActivity.this, getString(R.string.aneu_diktiou),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            mStatusTextView.setText(getString(R.string.google_status_fmt, user.getDisplayName()));
            String photoUrl = getString(R.string.firebase_status_fmt, user.getPhotoUrl());

            Glide.with(this).applyDefaultRequestOptions(new RequestOptions().centerCrop()).asBitmap().load(photoUrl).into(new BitmapImageViewTarget(person) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    person.setImageDrawable(circularBitmapDrawable);
                }
            });
            findViewById(R.id.image_profile_picture).setVisibility(View.VISIBLE);
            findViewById(R.id.image_empty_picture).setVisibility(View.GONE);
            findViewById(R.id.sign_in).setVisibility(View.GONE);
            findViewById(R.id.status).setVisibility(View.VISIBLE);
            findViewById(R.id.status).setOnClickListener(this);
            findViewById(R.id.main1).setOnClickListener(this);
            findViewById(R.id.main2).setOnClickListener(this);
            findViewById(R.id.main3).setOnClickListener(this);
            findViewById(R.id.main4).setOnClickListener(this);
            findViewById(R.id.main5).setOnClickListener(this);
            findViewById(R.id.main6).setOnClickListener(this);
            findViewById(R.id.main7).setOnClickListener(this);
            findViewById(R.id.main8).setOnClickListener(this);
            findViewById(R.id.main9).setOnClickListener(this);
        } else {
            mStatusTextView.setText(null);
            findViewById(R.id.image_empty_picture).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_in).setVisibility(View.VISIBLE);
            findViewById(R.id.image_profile_picture).setVisibility(View.GONE);
            findViewById(R.id.status).setVisibility(View.GONE);
            findViewById(R.id.main1).setOnClickListener(this);
            findViewById(R.id.main2).setOnClickListener(this);
            findViewById(R.id.main3).setOnClickListener(this);
            findViewById(R.id.main4).setOnClickListener(this);
            findViewById(R.id.main5).setOnClickListener(this);
            findViewById(R.id.main6).setOnClickListener(this);
            findViewById(R.id.main7).setOnClickListener(this);
            findViewById(R.id.main8).setOnClickListener(this);
            findViewById(R.id.main9).setOnClickListener(this);

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    // παράδειγμα event: click σε κουμπί με menu id
    private void logMenuClick(int menuId) {
        android.os.Bundle b = new android.os.Bundle();
        b.putString("menu_id", String.valueOf(menuId));
        AnalyticsTrackers.logEvent("menu_click", b);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.sign_in:
                logMenuClick(R.id.sign_in);
                signIn();
                break;
            case R.id.status:
                logMenuClick(R.id.status);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setItems(new CharSequence[]
                                {"Άκυρο", "Έξοδος", "Αποσύνδεση"},
                        (dialog, which) -> {
                            // The 'which' argument contains the index position
                            // of the selected item
                            switch (which) {
                                case 0:
                                    Intent a = new Intent(MainActivity.this, MainActivity.class);
                                    startActivity(a);
                                    break;
                                case 1:
                                    signOut();
                                    break;
                                case 2:
                                    revokeAccess();
                                    break;
                            }
                        });
                builder.create().show();
                break;
            case R.id.main1:
                logMenuClick(R.id.main1);
                Intent a = new Intent(MainActivity.this, OrganosiActivity.class);
                startActivity(a);
                break;
            case R.id.main2:
                logMenuClick(R.id.main2);
                Intent b = new Intent(MainActivity.this, KommActivity.class);
                startActivity(b);
                break;
            case R.id.main3:
                logMenuClick(R.id.main3);
                Intent c = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(c);
                break;
            case R.id.main4:
                logMenuClick(R.id.main4);
                Intent d = new Intent(MainActivity.this, EkdoseisActivity.class);
                startActivity(d);
                break;
            case R.id.main5:
                logMenuClick(R.id.main5);
                Intent e = new Intent(MainActivity.this, Image.class);
                startActivity(e);
                break;
            case R.id.main6:
                logMenuClick(R.id.main6);
                Intent f = new Intent(MainActivity.this, LiveVideoActivity.class);
                startActivity(f);
                break;
            case R.id.main7:
                logMenuClick(R.id.main7);
                Intent g = new Intent(MainActivity.this, NeaActivity.class);
                startActivity(g);
                break;
            case R.id.main8:
                logMenuClick(R.id.main8);
                Intent h = new Intent(MainActivity.this, MpsActivity.class);
                startActivity(h);
                break;
            case R.id.main9:
                logMenuClick(R.id.main9);
                Intent j = new Intent(MainActivity.this, Diafaneia.class);
                startActivity(j);
                break;
        }
    }

}