package gr.mobap.video;
// http://qthttp.apple.com.edgesuite.net/1010qwoeiuryfg/sl.m3u8
// http://streamer-cache.grnet.gr/parliament/hls/webtv.m3u8
// http://streamer-cache.grnet.gr/parliament/hls/webtv2.m3u8
// http://playertest.longtailvideo.com/adaptive/wowzaid3/playlist.m3u8

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.ProgressBar;
import android.graphics.Bitmap;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import gr.mobap.Base;

import gr.mobap.R;

public class LiveVideoDioActivity extends Base {

    private WebView webView;
    private ProgressBar progress;

    private static final String DIAVLOS_EMBED =
            "https://diavlos-cache.cnt.grnet.gr/app/index.html#/el/embed/room/6015?autoplay=true";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_video);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        webView  = findViewById(R.id.liveWebView);
        progress = findViewById(R.id.videoProgress);

        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        s.setBuiltInZoomControls(false);
        s.setDisplayZoomControls(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            s.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (progress != null) progress.setVisibility(View.VISIBLE);
            }

            @Override public void onPageFinished(WebView view, String url) {
                if (progress != null) progress.setVisibility(View.GONE);
            }

            @Override public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError err) {
                if (progress != null) progress.setVisibility(View.GONE);
                Toast.makeText(LiveVideoDioActivity.this, "Αποτυχία φόρτωσης ροής", Toast.LENGTH_SHORT).show();
            }
        });

        // Προαιρετικά, για προόδους φόρτωσης στο action bar αν θες:
        webView.setWebChromeClient(new WebChromeClient() {
            @Override public void onProgressChanged(WebView view, int newProgress) {
                // Αν χρησιμοποιείς indeterminate ProgressBar στο AppBar, μπορείς να τον εμφανίζεις/κρύβεις εδώ.
                if (progress != null && newProgress >= 90) progress.setVisibility(View.GONE);
            }

            // ΣΗΜΕΙΩΣΗ: ΔΕΝ υλοποιούμε onShowCustomView/onHideCustomView -> δεν πάμε σε full-screen.
            // Έτσι, η μπάρα της εφαρμογής μένει πάντα ορατή όσο παίζει το video.
        });

        webView.loadUrl(DIAVLOS_EMBED);
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override protected void onPause() {
        if (webView != null) { webView.onPause(); webView.pauseTimers(); }
        super.onPause();
    }

    @Override protected void onResume() {
        super.onResume();
        if (webView != null) { webView.onResume(); webView.resumeTimers(); }
    }

    @Override protected void onDestroy() {
        if (webView != null) {
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}