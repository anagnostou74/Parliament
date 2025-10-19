package gr.mobap.youtube;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import gr.mobap.Base;
import gr.mobap.R;

public class YoutubePlayerActivity extends Base {

    private WebView webView;
    private ProgressBar progress;
    private String videoId;

    @Override
    protected String screenName() { return "YouTube Player"; }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        // Σύνδεσε το drawer του Base (το πεδίο στο Base πρέπει να είναι protected)
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        videoId = getIntent().getStringExtra("video_id");
        if (videoId == null || videoId.isEmpty()) {
            Toast.makeText(this, "Άκυρο video id", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        webView  = findViewById(R.id.playerWebView);
        progress = findViewById(R.id.playerProgress);

        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            s.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }

        webView.addJavascriptInterface(new JsBridge(), "Android");

        webView.setWebViewClient(new WebViewClient() {
            @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (progress != null) progress.setVisibility(View.VISIBLE);
            }
            @Override public void onPageFinished(WebView view, String url) {
                if (progress != null) progress.setVisibility(View.GONE);
            }
            @Override public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError err) {
                if (progress != null) progress.setVisibility(View.GONE);
                openInYouTubeAppOrBrowser(videoId);
                finish();
            }
        });
        webView.setWebChromeClient(new WebChromeClient());

        // IFrame API ώστε να πιάνουμε error codes (150/151/152/153 κ.λπ.) και να κάνουμε fallback
        String html = buildHtml(videoId);
        webView.loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "UTF-8", null);
    }

    private String buildHtml(String id) {
        return "<!DOCTYPE html><html><head><meta name='viewport' content='width=device-width, initial-scale=1'/>"
                + "<style>html,body,#player{margin:0;padding:0;height:100%;width:100%;background:#000}</style>"
                + "<script src='https://www.youtube.com/iframe_api'></script>"
                + "<script>"
                + "var player;"
                + "function onYouTubeIframeAPIReady(){"
                + "  player = new YT.Player('player', {"
                + "    videoId: '" + id + "',"
                + "    playerVars: { 'autoplay':1, 'playsinline':1, 'rel':0, 'modestbranding':1 },"
                + "    events: {"
                + "      'onError': function(e){ try{ Android.onPlayerError(e.data); }catch(ex){} },"
                + "      'onReady': function(e){ try{ Android.onPlayerReady(); }catch(ex){} }"
                + "    }"
                + "  });"
                + "}"
                + "</script></head>"
                + "<body><div id='player'></div></body></html>";
    }

    private class JsBridge {
        @JavascriptInterface public void onPlayerReady() { /* no-op */ }
        @JavascriptInterface public void onPlayerError(int code) {
            runOnUiThread(() -> {
                openInYouTubeAppOrBrowser(videoId);
                finish();
            });
        }
    }

    private void openInYouTubeAppOrBrowser(String id) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id)));
        } catch (Exception ignore) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + id)));
        }
    }

    @Override protected void onPause() { super.onPause(); if (webView!=null){ webView.onPause(); webView.pauseTimers(); } }
    @Override protected void onResume() { super.onResume(); if (webView!=null){ webView.onResume(); webView.resumeTimers(); } }
    @Override protected void onDestroy() {
        if (webView != null) { webView.removeAllViews(); webView.destroy(); }
        super.onDestroy();
    }
}
