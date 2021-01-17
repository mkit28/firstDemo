package com.mktech28.wsdownloader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mktech28.wsdownloader.adapters.StoryAdapter;
import com.mktech28.wsdownloader.models.Constant;
import com.mktech28.wsdownloader.models.StoryModel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    StoryAdapter storyAdapter;
    File[] files;
    ArrayList<Object> fileList = new ArrayList<>();

    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        // for Admob Ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        initViews();

        Dexter.withContext(MainActivity.this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                        setUpRefreshLayout();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.actions_share){
            shareApp();
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareApp() {

        Intent intentShareApp = new Intent(Intent.ACTION_SEND);
        intentShareApp.setType("text/plain");
        String shareBody = "Save your WhatsApp Statuses (Pictures and Videos) no need to ask for statuses https://play.google.com/store/apps/details?id=" + this.getPackageName();
        intentShareApp.putExtra(Intent.EXTRA_SUBJECT, "WhatsApp Status Downloader");
        intentShareApp.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intentShareApp,"Share Via"));

    }

    private void initViews() {
        swipeRefreshLayout = findViewById(R.id.swipRefreshLayout);
        recyclerView = findViewById(R.id.recyclerViewSwip);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               swipeRefreshLayout.setRefreshing(true);
               setUpRefreshLayout();

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(MainActivity.this, "Refresh!", Toast.LENGTH_SHORT).show();
                    }
                },2000);
            }
        });

    }

    private void setUpRefreshLayout() {
        fileList.clear();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        storyAdapter = new StoryAdapter(MainActivity.this, getData());
        recyclerView.setAdapter(storyAdapter);

    }

    private ArrayList<Object> getData() {


        StoryModel f;
        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.FOLDER_NAME + "Media/.Statuses";
        File targetDir = new File(targetPath);

        files = targetDir.listFiles();


           try{
               for (File file : files) {

                   f = new StoryModel();
                   f.setUri(Uri.fromFile(file));
                   f.setPath(file.getAbsolutePath());
                   f.setFilename(file.getName());

                   if (!f.getUri().toString().endsWith(".nomedia")) {
                       fileList.add(f);
                   }
               }
           }catch (Exception e){
               e.printStackTrace();
           }

        return fileList;
    }
}