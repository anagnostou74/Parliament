/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gr.mobap.youtube;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gr.mobap.Base;      // η δική σου Base activity
import gr.mobap.R;

public class YoutubeFeedActivity extends Base {

    private RecyclerView recycler;
    private View progress;
    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private final RssFetcher fetcher = new RssFetcher();

    @Override
    protected String screenName() { return "YouTube"; }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube); // το shell που περιλαμβάνει app_bar_youtube
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recycler = findViewById(R.id.videosRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        YoutubeAdapter adapter = new YoutubeAdapter(video -> {
            Intent i = new Intent(this, YoutubePlayerActivity.class);
            i.putExtra("video_id", video.videoId);
            startActivity(i);
        });
        recycler.setAdapter(adapter);

        // Μικρό progress view: μπορείς να χρησιμοποιήσεις ένα ProgressBar στο app_bar ή απλό full-screen overlay
        progress = findViewById(android.R.id.progress); // ή φτιάξε δικό σου αν δεν υπάρχει
        if (progress != null) progress.setVisibility(View.VISIBLE);

        String channelId = getString(R.string.yt_channel_id);
        io.execute(() -> {
            try {
                List<YoutubeVideo> items = fetcher.fetch(channelId);
                runOnUiThread(() -> {
                    if (progress != null) progress.setVisibility(View.GONE);
                    if (items.isEmpty()) {
                        Toast.makeText(this, "Δεν βρέθηκαν βίντεο.", Toast.LENGTH_SHORT).show();
                    } else {
                        adapter.submit(items);
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    if (progress != null) progress.setVisibility(View.GONE);
                    Toast.makeText(this, "Σφάλμα φόρτωσης ροής", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        io.shutdownNow();
    }
}