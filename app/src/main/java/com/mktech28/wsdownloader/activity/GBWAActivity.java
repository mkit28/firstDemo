package com.mktech28.wsdownloader.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.mktech28.wsdownloader.R;
import com.mktech28.wsdownloader.fragments.GBWAPictureFragment;
import com.mktech28.wsdownloader.fragments.GBWAUserTimeLineFragment;
import com.mktech28.wsdownloader.fragments.GBWAVideoFragment;

import java.util.ArrayList;
import java.util.List;

public class GBWAActivity extends AppCompatActivity {

    private final String TAG = DrawerLayoutActivity.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gb);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GBWAActivity.super.onBackPressed();
            }
        });
        toolbar.setTitle("GBWhatsApp Status");
        //setSupportActionBar(toolbar);


        viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(0);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setupViewPager(ViewPager viewPager) {

        GBWAActivity.ViewPagerAdapte adapter = new GBWAActivity.ViewPagerAdapte(GBWAActivity.this, getSupportFragmentManager());
        adapter.addFragment(new GBWAPictureFragment(), "Picture");
        adapter.addFragment(new GBWAVideoFragment(), "Videos");
        adapter.addFragment(new GBWAUserTimeLineFragment(), "UserTimeLine");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    class ViewPagerAdapte extends FragmentPagerAdapter{

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private Context mContext;

        public ViewPagerAdapte(Context context, FragmentManager manager) {
            super(manager);
            mContext = context;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}