package gr.mobap.ekdoseis;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

import gr.mobap.AnalyticsApplication;
import gr.mobap.Base;
import gr.mobap.BuildConfig;
import gr.mobap.R;

public class DownloadAndOpen extends Base {

    public String TAG = getClass().getSimpleName();
    private String mPostKey;
    private FirebaseAnalytics mFirebaseAnalytics;
    public static final String EXTRA_POST_KEY = "post_key";
    private DatabaseReference mEkdoseisReference;
    private ValueEventListener mEkdoseiListener;
    private String pdfURL;
    public static final String downloadDirectory = "Vouli";

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

                //String pdfUrl = ekdoseis.url;
                //Log.e(TAG, pdfUrl);
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    File sdDir = Environment.getExternalStorageDirectory();
                    File newdir = new File(sdDir.getAbsolutePath() + "/" + downloadDirectory + "/PDF");
                    newdir.mkdirs();
                    Log.d(TAG, "Value is: newdir" + newdir);

                    try {
                        downloadAndOpenPDF(DownloadAndOpen.this, ekdoseis.url);
                        Log.d(TAG, "Value is: ekdoseis.url" + ekdoseis.url);

                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                    }

                    Log.e(TAG, ekdoseis.url);

                } else {
                    // display error
                    Toast.makeText(DownloadAndOpen.this, getString(R.string.aneu_diktiou),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(DownloadAndOpen.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mEkdoseisReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mEkdoseiListener = postListener;

    }

    public void downloadAndOpenPDF(final Context context, final String pdfUrl) {
        // Get filename
        final String filename = pdfUrl.substring(pdfUrl.lastIndexOf("/") + 1);
        final String decoded = Uri.decode(filename); //handles spaces at filename
        // The place where the downloaded PDF file will be put
        final File tempFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), decoded);
        // If we have downloaded the file before, just go ahead and show it(if its cached)

        if (tempFile.exists()) {
            Uri contentUri;
            if (Build.VERSION.SDK_INT >= 24) {
                contentUri = FileProvider.getUriForFile(DownloadAndOpen.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        tempFile);
                openPDF(context, contentUri);
                finish();
            } else {
                contentUri = Uri.fromFile(tempFile);
                openPDF(context, contentUri);
                finish();
            }
            return;
        }

// Show progress dialog while downloading
        final ProgressDialog progress = ProgressDialog.show(context, "Λήψη έκδοσης", "Περιμένετε να κατέβει το pdf.", true);

        // Create the download request
        DownloadManager.Request r = new DownloadManager.Request(Uri.parse(pdfUrl));
        r.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, decoded);
        final DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        //Broadcast receiver for when downloading the PDF is complete
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!progress.isShowing()) {
                    return;
                }
                context.unregisterReceiver(this);
                //Dismiss the progressDialog
                progress.dismiss();
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                Cursor c = dm.query(new DownloadManager.Query().setFilterById(downloadId));
                //if download was successful attempt to open the PDF
                if (c.moveToFirst()) {
                    int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        if (tempFile.exists()) {
                            Uri contentUri;
                            if (Build.VERSION.SDK_INT >= 24) {
                                contentUri = FileProvider.getUriForFile(DownloadAndOpen.this,
                                        getApplicationContext().getPackageName() + ".provider",
                                        tempFile);
                                openPDF(context, contentUri);
                                finish();
                            } else {
                                contentUri = Uri.fromFile(tempFile);
                                openPDF(context, contentUri);
                                finish();
                            }
                            return;
                        }
                    }
                }
                c.close();
            }
        };
        //Resister the receiver
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        // Enqueue the request
        dm.enqueue(r);
    }

    public static final void openPDF(Context context, Uri localUri) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.setDataAndType(localUri, "application/pdf");
        context.startActivity(i);
    }


}