package gr.mobap.video;
// http://qthttp.apple.com.edgesuite.net/1010qwoeiuryfg/sl.m3u8
// http://streamer-cache.grnet.gr/parliament/hls/webtv.m3u8
// http://streamer-cache.grnet.gr/parliament/hls/webtv2.m3u8

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.Actions;

import gr.mobap.AnalyticsApplication;
import gr.mobap.AndroidNetworkUtility;
import gr.mobap.MainActivity;
import gr.mobap.R;

public class LiveVideoDioActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.content_video);
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        // [END shared_tracker]
        AndroidNetworkUtility androidNetworkUtility = new AndroidNetworkUtility();
        if (androidNetworkUtility.isConnected(this)) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            VideoView emVideoView = findViewById(R.id.video_play_activity_video_view);
            assert emVideoView != null;
            emVideoView.setVideoURI(Uri.parse("http://streamer-cache.grnet.gr/parliament/hls/webtv2_640_640x360/index.m3u8"));
            emVideoView.start();
        } else {
            // display error
            Toast.makeText(this, getString(R.string.aneu_diktiou),
                    Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> {
                Intent i = new Intent(LiveVideoDioActivity.this, MainActivity.class);
                startActivity(i);
                // close this activity
                finish();
            }, 1000); // wait for 1 second
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public Action getAction() {
        return Actions.newView("LiveVideoDio Page", "http://www.mobap.gr/livevideodioactivity");
    }

    @Override
    protected void onStart() {
        super.onStart();
   /* If you’re logging an action on an item that has already been added to the index,
   you don’t have to add the following update line. See
   https://firebase.google.com/docs/app-indexing/android/personal-content#update-the-index for
   adding content to the index */
        FirebaseAppIndex.getInstance().update();
        FirebaseUserActions.getInstance().start(getAction());
    }

    @Override
    protected void onStop() {
        FirebaseUserActions.getInstance().end(getAction());
        super.onStop();
    }

}