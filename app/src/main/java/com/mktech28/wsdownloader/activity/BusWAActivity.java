package com.mktech28.wsdownloader.activity;

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
import com.mktech28.wsdownloader.fragments.BWAPictureFragment;
import com.mktech28.wsdownloader.fragments.BWAUserTimeLineUserFragment;
import com.mktech28.wsdownloader.fragments.BWAVideoFragment;

import java.util.ArrayList;
import java.util.List;

public class BusWAActivity extends AppCompatActivity {

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
                BusWAActivity.super.onBackPressed();
            }
        });
        toolbar.setTitle("WhatsApp Business Status");
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
        BusWAActivity.ViewPagerAdapter adapter = new BusWAActivity.ViewPagerAdapter(BusWAActivity.this, getSupportFragmentManager());
        adapter.addFragment(new BWAPictureFragment(), "Picture");
        adapter.addFragment(new BWAVideoFragment(), "Videos");
        adapter.addFragment(new BWAUserTimeLineUserFragment(), "UserTimeLine");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private Context mContext;

        public ViewPagerAdapter(Context context, FragmentManager manager) {
            super(manager);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}