package gr.mobap;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

public class PeriActivity extends Base {

    private FirebaseAnalytics mFirebaseAnalytics;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peri);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button button2 = findViewById(R.id.btnMail);
        if (button2 != null) {
            button2.setOnClickListener(v -> mail());
        }

        Button button3 = findViewById(R.id.btnVathm);
        if (button3 != null) {
            button3.setOnClickListener(v -> launchMarket());
        }

        final String htmlText = getResources().getString(R.string.sxetikaKeim);
        TextView tv = findViewById(R.id.sxetikaKeim2);
        if (tv != null) {
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            tv.setText(Html.fromHtml(htmlText));
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


    private void mail() {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL,
                new String[]{"anagnostou74@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT,
                getString(R.string.sxetika_mail));
        i.putExtra(Intent.EXTRA_TEXT, getString(R.string.main_me));
        try {
            startActivity(Intent.createChooser(i,
                    getString(R.string.apostoli)));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, getString(R.string.aneu),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void launchMarket() {

        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, getString(R.string.failed2), Toast.LENGTH_LONG)
                    .show();
        }
    }

}