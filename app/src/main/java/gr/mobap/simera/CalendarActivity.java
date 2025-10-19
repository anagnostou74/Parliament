package gr.mobap.simera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;
import androidx.viewpager2.widget.ViewPager2;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import androidx.core.content.ContextCompat;

import gr.mobap.Base;
import gr.mobap.R;

public class CalendarActivity extends Base implements NavigationView.OnNavigationItemSelectedListener,
        CalendarAdapter.PermissionHost{
    Integer actionBarHeight = null;
    public String TAG = getClass().getSimpleName();

    private ViewPager2 mViewPager;
    @Nullable private Runnable onNotifGranted;
    private ActivityResultLauncher<String> notifPermissionLauncher;
    private FragmentStateAdapter mPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // launcher για POST_NOTIFICATIONS (register ΠΑΝΤΑ στο onCreate)
        notifPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                    if (granted && onNotifGranted != null) onNotifGranted.run();
                    onNotifGranted = null;
                });

        mViewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tabs);

        mPagerAdapter = new FragmentStateAdapter(this) {
            private final androidx.fragment.app.Fragment[] frags = new androidx.fragment.app.Fragment[] {
                    new CalendarFragment(),
                    new OlomeleiaEleghosFragment()
            };
            @Override public int getItemCount() { return frags.length; }
            @Override public androidx.fragment.app.Fragment createFragment(int position) { return frags[position]; }
        };
        mViewPager.setAdapter(mPagerAdapter);

        new TabLayoutMediator(tabLayout, mViewPager, (tab, position) -> {
            tab.setText(position == 0 ? getString(R.string.calendar) : getString(R.string.today_el));
        }).attach();
    }

    // PermissionHost υλοποίηση
    @Override
    public void requestPostNotificationsIfNeeded(Runnable onGranted) {
        if (Build.VERSION.SDK_INT < 33) { onGranted.run(); return; }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            onGranted.run(); return;
        }
        onNotifGranted = onGranted;
        notifPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
    }
}
