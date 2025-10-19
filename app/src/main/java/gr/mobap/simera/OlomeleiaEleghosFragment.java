package gr.mobap.simera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import gr.mobap.AndroidNetworkUtility;
import gr.mobap.R;

public class OlomeleiaEleghosFragment extends Fragment {
    private WebView webView;
    private Bundle webViewBundle;
    String url = "https://www.hellenicparliament.gr/Koinovouleftikos-Elenchos/Deltio-Epikairon-Erotiseon";
    private static final String TAG = "OlomeleiaEleghosFrag";

    public OlomeleiaEleghosFragment() {
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
            webView.getSettings().setDisplayZoomControls(false);
            webView.getSettings().supportZoom();
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            if (webViewBundle == null) { //Κώδικας για webView save State
                webView.loadUrl(url);
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