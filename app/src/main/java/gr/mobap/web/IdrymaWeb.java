package gr.mobap.web;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import gr.mobap.AndroidNetworkUtility;
import gr.mobap.R;

public class IdrymaWeb extends Fragment {

    private WebView webView;
    private Bundle webViewBundle;
    private ProgressDialog progress;

    public IdrymaWeb() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.fragment_web,
                container, false);
        AndroidNetworkUtility androidNetworkUtility = new AndroidNetworkUtility();
        if (androidNetworkUtility.isConnected(getActivity())) {
            webView = ll.findViewById(R.id.webViewfr);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().supportZoom();
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            progress = ProgressDialog.show(getActivity(), "Παρακαλώ περιμένετε...",
                    "Φορτώνει η σελίδα", true);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            webView.setWebViewClient(new WebViewClient() {

                public void onPageFinished(WebView view, String url) {
                    if (progress != null)
                        progress.dismiss();
                }
            });
            if (webViewBundle == null) { //Κώδικας για webView save State
                webView.loadUrl("http://foundation.parliament.gr");
            } else {
                webView.restoreState(webViewBundle);
            }

        } else {
            Toast.makeText(getActivity(), getString(R.string.aneu_diktiou),
                    Toast.LENGTH_SHORT).show();
        }
        return ll;
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