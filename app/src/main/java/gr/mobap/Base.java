package gr.mobap;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import gr.mobap.diafaneia.Diafaneia;
import gr.mobap.ekdoseis.EkdoseisActivity;
import gr.mobap.government.GovActivity;
import gr.mobap.images.Image;
import gr.mobap.komma.KommActivity;
import gr.mobap.map.MapsActivity;
import gr.mobap.mps.MpsActivity;
import gr.mobap.organosi.OrganosiActivity;
import gr.mobap.rss.activities.DrastActivity;
import gr.mobap.rss.activities.EktheseisEpActivity;
import gr.mobap.rss.activities.EleghosActivity;
import gr.mobap.rss.activities.NeaActivity;
import gr.mobap.rss.activities.NsKatActivity;
import gr.mobap.rss.activities.NsPsActivity;
import gr.mobap.rss.activities.SinEpActivity;
import gr.mobap.simera.CalendarActivity;
import gr.mobap.video.LiveVideoActivity;
import gr.mobap.video.LiveVideoDioActivity;
import gr.mobap.video.LiveVideoThreeActivity;
import gr.mobap.vouli.Thesmos;
import gr.mobap.web.MailWeb;
import gr.mobap.web.SindesmoiActivity;
import gr.mobap.youtube.YoutubeFeedActivity;
import android.os.Bundle;
import android.view.Gravity;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;



/**
 * Created by kanag on 15/Οκτ/2016.
 */

public class Base extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public String TAG = getClass().getSimpleName();


    /**
     * Επέστρεψε λογικό screen name (π.χ. "Ekdoseis", "Main")
     */
    protected String screenName() {
        return "Main";
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Αν χρησιμοποιείται drawerLayout, πρέπει να το ορίσει το child activity (π.χ. MainActivity)
        // drawerLayout = findViewById(R.id.drawer_layout); // <-- Θέλει να το κάνει override το child

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerBackCallbackForApi33();
        } else {
            getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else {
                        setEnabled(false); // allow default behavior
                        Base.super.onBackPressed();
                    }
                }
            });
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    private void klisi() {
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        String phone = "+302103707000";
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

    private void mail() {
        AnalyticsApplication application = (AnalyticsApplication) getApplication();

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL,
                new String[]{"infopar@parliament.gr"});
        i.putExtra(Intent.EXTRA_SUBJECT,
                getString(R.string.epikoinonia_mail));
        i.putExtra(Intent.EXTRA_TEXT, getString(R.string.main_epikoinonia));
        try {
            startActivity(Intent.createChooser(i, getString(R.string.apostoli)));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, getString(R.string.aneu),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent i = new Intent(Base.this, MainActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_thesmos) {
            Intent i = new Intent(Base.this, Thesmos.class);
            startActivity(i);
        } else if (id == R.id.nav_organosi) {
            Intent i = new Intent(Base.this, OrganosiActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_kommata) {
            Intent i = new Intent(Base.this, KommActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_vouleutes) {
            Intent i = new Intent(Base.this, MpsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_simera) {
            Intent i = new Intent(Base.this, CalendarActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_sindesmoi) {
            Intent i = new Intent(Base.this, SindesmoiActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_gov) {
            Intent i = new Intent(Base.this, GovActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_ekdoseis) {
            Intent i = new Intent(Base.this, EkdoseisActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_photo) {
            Intent i = new Intent(Base.this, Image.class);
            startActivity(i);
        } else if (id == R.id.nav_kanali) {
            Intent i = new Intent(Base.this, YoutubeFeedActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_live_ena) {
            Intent i = new Intent(Base.this, LiveVideoActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_live_dio) {
            Intent i = new Intent(Base.this, LiveVideoDioActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_live_three) {
            Intent i = new Intent(Base.this, LiveVideoThreeActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_nea) {
            Intent i = new Intent(Base.this, NeaActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_diafaneia) {
            Intent i = new Intent(Base.this, Diafaneia.class);
            startActivity(i);
        } else if (id == R.id.nav_eleghos) {
            Intent i = new Intent(Base.this, EleghosActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_katatethenta) {
            Intent i = new Intent(Base.this, NsKatActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_psifisthenta) {
            Intent i = new Intent(Base.this, NsPsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_sinedriaseis_epitropon) {
            Intent i = new Intent(Base.this, SinEpActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_ektheseis) {
            Intent i = new Intent(Base.this, EktheseisEpActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_drastiriotites) {
            Intent i = new Intent(Base.this, DrastActivity.class);
            startActivity(i);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Για Android 13+ (API 33)
    private final OnBackInvokedCallback backInvokedCallback = () -> {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finish(); // ή super.onBackPressed() αν θες παλιότερη συμπεριφορά
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void registerBackCallbackForApi33() {
        OnBackInvokedDispatcher dispatcher = getOnBackInvokedDispatcher();
        dispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                backInvokedCallback
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getOnBackInvokedDispatcher().unregisterOnBackInvokedCallback(backInvokedCallback);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_klisi:
                // User chose the "Settings" item, show the app settings UI...
                klisi();
                return true;
            case R.id.action_mail:
                // User chose the "Settings" item, show the app settings UI...
                mail();
                return true;
            case R.id.action_map:
                // User chose the "Settings" item, show the app settings UI...
                Intent i = new Intent(Base.this, MapsActivity.class);
                startActivity(i);
                return true;
            case R.id.action_peri:
                Intent p = new Intent(Base.this, PeriActivity.class);
                startActivity(p);
                return true;
            case R.id.menu_refresh:
                refresh();
                return true;
            case R.id.menu_share:
                shareSocial();
                return true;
            case R.id.menu_play:
                try {
                    String url = "https://play.google.com/store/apps/dev?id=9092318171925704848";
                    Intent m = new Intent(Intent.ACTION_VIEW);
                    m.setData(Uri.parse(url));
                    startActivity(m);
                } catch (ActivityNotFoundException activityException) {
                    Log.e("navigate Site", "Site failed", activityException);
                }
                return true;
            case R.id.menu_fb:
                Intent k = new Intent(Base.this, Share.class);
                startActivity(k);
                return true;
            case R.id.nav_diktio:
                Intent d = new Intent(Base.this, MailWeb.class);
                startActivity(d);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareSocial() {
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        CharSequence subject = "Δες τι βρήκα!";
        CharSequence text = "Αξίζει να κατεβάσεις αυτή την εφαρμογή για να βλέπεις εργασίες, που επηρεάζουν το δικό σου σήμερα " +
                "https://play.google.com/store/apps/details?id=gr.mobap";
        Uri imageUri = Uri.parse("android.resource://" + getPackageName() + "/drawable/" + "share");


        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject.toString());
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        //startActivity(Intent.createChooser(intent, "Κοινοποίηση εφαρμογής"));

        // get available share intents
        List<Intent> targets = new ArrayList<>();
        Intent template = new Intent(Intent.ACTION_SEND);
        template.setType("text/plain");
        List<ResolveInfo> candidates = this.getPackageManager().
                queryIntentActivities(template, 0);

        // remove facebook which has a broken share intent
        for (ResolveInfo candidate : candidates) {
            String packageName = candidate.activityInfo.packageName;
            if (!packageName.equals("com.facebook.katana")) {
                Intent target = new Intent(android.content.Intent.ACTION_SEND);
                target.setType("text/plain");
                target.putExtra(Intent.EXTRA_TEXT, text);
                target.putExtra(Intent.EXTRA_SUBJECT, subject);
                target.setPackage(packageName);
                targets.add(target);
            }
        }
        Intent chooser = Intent.createChooser(targets.remove(0), "Κοινοποίηση εφαρμογής");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targets.toArray(new Parcelable[]{}));
        startActivity(chooser);
    }

    private void refresh() {
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        String sn = screenName();
        if (sn != null && !sn.isEmpty()) {
            AnalyticsTrackers.logScreen(sn, getClass().getSimpleName());
        }
    }


}
