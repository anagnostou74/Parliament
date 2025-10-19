package gr.mobap;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import gr.mobap.simera.SessionReminderReceiver;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;

public class AnalyticsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Init Firebase Analytics helper
        AnalyticsTrackers.init(getApplicationContext());

        // Προαιρετικό: διαφορετική συμπεριφορά σε debug
        if (BuildConfig.DEBUG) {
            // Ενεργοποιεί/βοηθάει DebugView (μαζί με adb property)
            FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true);
        }

        try { FirebaseDatabase.getInstance().setPersistenceEnabled(true); } catch (Exception ignore) {}
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = getSystemService(NotificationManager.class);
            NotificationChannel ch = new NotificationChannel(
                    SessionReminderReceiver.CHANNEL_ID,
                    getString(R.string.channel_calendar_session),
                    NotificationManager.IMPORTANCE_HIGH
            );
            ch.setDescription(getString(R.string.channel_calendar_session_desc));
            nm.createNotificationChannel(ch);
        }
    }

}
