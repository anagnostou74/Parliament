package gr.mobap.periodiko;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import gr.mobap.AnalyticsApplication;
import gr.mobap.AndroidNetworkUtility;
import gr.mobap.Base;
import gr.mobap.R;
import gr.mobap.ekdoseis.EkdoseisData;

public class PeriodikoActivity extends Base {
    private FirebaseAnalytics mFirebaseAnalytics;
    private WebView webView;
    private Bundle webViewBundle;
    private ProgressDialog progress;
    public String TAG = getClass().getSimpleName();
    private String mPostKey;
    public static final String EXTRA_POST_KEY = "post_key";
    private DatabaseReference mEkdoseisReference;
    private ValueEventListener mEkdoseiListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        // Get post key from intent
        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }
        // Initialize Database
        mEkdoseisReference = FirebaseDatabase.getInstance().getReference()
                .child("ekdoseis").child(mPostKey);

        Log.d(TAG, "Value is: " + mEkdoseisReference);

        setContentView(R.layout.activity_web);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                EkdoseisData ekdoseis = dataSnapshot.getValue(EkdoseisData.class);
                // [START_EXCLUDE]

                String url = ekdoseis.url;

                AndroidNetworkUtility androidNetworkUtility = new AndroidNetworkUtility();
                if (androidNetworkUtility.isConnected(PeriodikoActivity.this)) {
                    webView = findViewById(R.id.webView);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.getSettings().setBuiltInZoomControls(true);
                    webView.getSettings().supportZoom();
                    webView.getSettings().setUseWideViewPort(true);
                    webView.getSettings().setLoadWithOverviewMode(true);
                    progress = ProgressDialog.show(PeriodikoActivity.this, "Παρακαλώ περιμένετε...",
                            "Φορτώνει η σελίδα", true);
                    progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    webView.setWebViewClient(new WebViewClient() {

                        public void onPageFinished(WebView view, String url) {
                            if (progress != null)
                                progress.dismiss();
                        }
                    });
                    if (webViewBundle == null) { //Κώδικας για webView save State
                        webView.loadUrl(url);
                    } else {
                        webView.restoreState(webViewBundle);
                    }

                } else {
                    Toast.makeText(PeriodikoActivity.this, getString(R.string.aneu_diktiou),
                            Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(PeriodikoActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mEkdoseisReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mEkdoseiListener = postListener;

    }


    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mEkdoseisReference != null) {
            mEkdoseisReference.removeEventListener(mEkdoseiListener);
        }

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onPause() {
        super.onPause();
        if (webView == null) {
            onDestroy();
        } else {
            webViewBundle = new Bundle();
            webView.saveState(webViewBundle);
        }
    }
}