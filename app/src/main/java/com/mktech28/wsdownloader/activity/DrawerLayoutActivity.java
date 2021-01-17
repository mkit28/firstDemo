package com.mktech28.wsdownloader.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.mktech28.wsdownloader.R;
import com.mktech28.wsdownloader.adapters.ViewPagerAdapter;
import com.mktech28.wsdownloader.fragments.WAPictureFragment;
import com.mktech28.wsdownloader.fragments.WAVideoFragment;
import com.mktech28.wsdownloader.fragments.WAUserTimeLineFragment;
import com.mktech28.wsdownloader.utils.Observer;

public class DrawerLayoutActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Observer {

    private final String TAG = "Drawer_TAG";
    boolean doubleBackToExitPressedOnce = false;
    NavigationView navigationView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_layout);

        tabLayout = findViewById(R.id.tabLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("WhatsApp Status");
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = findViewById(R.id.navigation_View);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        viewPager.setOffscreenPageLimit(0);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }if(getSupportFragmentManager().getBackStackEntryCount() == 0){
            if(doubleBackToExitPressedOnce){
                finish();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            },2000);
        }else{
        super.onBackPressed();
       }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_settings){
            shareApp();
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareApp() {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBody = "Download your whatsApp Statuses(Picture & Videos) no need to ask for statuses https://play.google.com/store/apps/details?id="+this.getPackageName();
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,"WhatsApp Status Downloader");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(shareIntent, "Share via.."));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here

        int id = item.getItemId();
        if (id == R.id.nav_simple) {

        } else if (id == R.id.nav_business) {
            startActivity(new Intent(DrawerLayoutActivity.this, BusWAActivity.class));
        } else if (id == R.id.nav_gb) {
            startActivity(new Intent(DrawerLayoutActivity.this, GBWAActivity.class));
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            shareApp();
        } else if (id == R.id.nav_send) {
            startActivity(new Intent(DrawerLayoutActivity.this, HelpActivity.class));

        } else if (id == R.id.nav_rate) {
            //rateUsOnPlayStore();
        } else if (id == R.id.nav_privacy_policy) {
//            try {
//                Uri uri = Uri.parse("https://google.com/"); // missing 'http://' will cause crashed
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//            } catch (Exception ex) {
//
//            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void update(String value, Context context) {

    }

    private void setupViewPager(ViewPager viewPager) {

       ViewPagerAdapter adapter = new ViewPagerAdapter(DrawerLayoutActivity.this, getSupportFragmentManager());
       adapter.addFragment(new WAPictureFragment(),"Picture");
       adapter.addFragment(new WAVideoFragment(),"Videos");
       adapter.addFragment(new WAUserTimeLineFragment(),"UserTimeLine");
       viewPager.setAdapter(adapter);
       viewPager.setCurrentItem(0);

    }




}