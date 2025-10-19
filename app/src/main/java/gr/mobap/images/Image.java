package gr.mobap.images;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import gr.mobap.AnalyticsApplication;
import gr.mobap.AndroidNetworkUtility;
import gr.mobap.Base;
import gr.mobap.MainActivity;
import gr.mobap.R;

public class Image extends Base {
    public static final String[] titles = new String[]{"Τίτλος", "Τίτλος", "Τίτλος", "Τίτλος", "Τίτλος",
            "Τίτλος", "Τίτλος", "Τίτλος", "Τίτλος", "Τίτλος"};
    public static String[] IMGS = {
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2009_03_18_014.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/1%20IMG_2008_02_22_9_63a.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2008_10_21_030.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/Img2005-03-22_20.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_30_2a%20copy.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_02_17_9_6.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2012_06_28_9_102a.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2009_03_04_9_39.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2009_11_20_9_27%20copy.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/Img2004-12-13_06%20copy.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2016_03_01_069.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2009_01_13_9_9a.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/img2005_10_14_12%20copy.JPG",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2008_10_21_044.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_30_9_26%20copy.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_29_9_96%20copy.JPG",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/Img2004-12-13_008a.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_29_9_72%20copy.JPG",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2016_03_01_057.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2%20IMG_2009_02_11_013.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2009_11_20_9_23%20copy.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2%20IMG_2009_01_30_9_70.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2008_10_20_9_7.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2009_11_20_33a%20copy.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2009_03_04_9_47a%20copy.JPG",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_29_9_88%20copy.JPG",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2%CE%B1%20IMG_2009_02_11_069.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_29_9_102%20copy.JPG",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_30_9_16%20copy.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2008_04_17_48.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/3%20IMG_2008_09_29_9_148b.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2009_08_25_18a%20copy.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/img2007_02_16_3.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_29_9_56%20copy.JPG",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/3%20IMG_2009_01_30_9_55.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_30_9_38a%20copy.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_02_17_9_23.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/3a%20IMG_2008_09_29_9_144%20copy.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_30_9_48%20copy.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/4%20IMG_2009_01_30_9_74.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_29_9_44%20copy.JPG",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_29_9_65%20copy.JPG",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2009_08_25_47a%20copy.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/3b%20IMG_2009_08_21_16.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_30_9_52%20copy.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_02_17_9_31.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/5%20IMG_2009_01_30_9_72.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_29_9_124a%20copy.JPG",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2008_10_11_9_11%20copy.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_02_17_9_37.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/4%20IMG_2008_02_22_9_135.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2008_09_22_9_1a.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_29_1%20copy.JPG",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/4c%20IMG_2008_02_27_99_6a.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/4b%20IMG_2008_02_27_99_14a.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_29_7%20copy.JPG",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/6%20IMG_2008_02_22_9_11.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/5%20IMG_2008_02_22_9_123.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/6%20IMG_2009_04_29_027.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_29_9_1%20copy.JPG",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_29_9_8%20copy.JPG",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/7%20%20%20%20%20120x70_final%20_xorisandra.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2008_09_29_9_21a.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_03_05_9_48a.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2008_02_27_58a.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2008_12_02_9_121%20copy.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2008_02_27_68c.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_29_9_23%20copy.JPG",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_29_9_31%20copy.JPG",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2008_02_27_77a_hor.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_29_9_93%20copy.JPG",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2009_08_21_54.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_29_009%20copy.JPG",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2008_12_09_9_73b.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2008_09_29_9_91a.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2008_10_17_9_226%20copy.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_29_018%20copy.JPG",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/IMG_2008_02_27_86a.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/2010_04_29_9_106%20copy.JPG",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/P7130030a.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/00155%20copy.JPG",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/Img2004_02_20_64.jpg",
            "https://www.hellenicparliament.gr/UserFiles/130dd656-40e0-4140-baf7-ff5d9eaa975d/8%20IMG_2008_10_17_9_273.jpg",


    };
    GalleryAdapter mAdapter;
    RecyclerView mRecyclerView;
    ArrayList<ImageModel> data = new ArrayList<>();
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setContentView(R.layout.activity_images);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data2 = intent.getData();

        AndroidNetworkUtility androidNetworkUtility = new AndroidNetworkUtility();
        if (androidNetworkUtility.isConnected(this)) {
            for (int i = 0; i < IMGS.length; i++) {
                ImageModel imageModel = new ImageModel();
                imageModel.setName("Φωτογραφία " + i);
                imageModel.setUrl(IMGS[i]);
                data.add(imageModel);
            }
            mRecyclerView = findViewById(R.id.list);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            mRecyclerView.setHasFixedSize(true);
            mAdapter = new GalleryAdapter(Image.this, data);
            mRecyclerView.setAdapter(mAdapter);

            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                    (view, position) -> {

                        Intent intent1 = new Intent(Image.this, DetailActivity.class);
                        intent1.putParcelableArrayListExtra("data", data);
                        intent1.putExtra("pos", position);
                        startActivity(intent1);

                    }));
        } else {
            // display error
            Toast.makeText(this, getString(R.string.aneu_diktiou),
                    Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> {
                Intent i = new Intent(Image.this, MainActivity.class);
                // close this activity
                finish();
            }, 1000); // wait for 1 second
        }
    }
}