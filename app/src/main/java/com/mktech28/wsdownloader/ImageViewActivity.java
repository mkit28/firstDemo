package com.mktech28.wsdownloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.mktech28.wsdownloader.models.Constant;
import com.mktech28.wsdownloader.models.StoryModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class ImageViewActivity extends AppCompatActivity {

    private List<Observer> observers;
    String image_path = "", package_name = "";
    ImageView  imageView;
    private ImageView btnDownloadImage, btnShareImage;
    String destPath="";
    Uri uri;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        imageView = findViewById(R.id.imageViewLinear);
        observers = new ArrayList<>();


        Intent intent = getIntent();
        if(intent != null){
            image_path = intent.getStringExtra("image");
                uri = intent.getParcelableExtra("uri");
                imageView.setImageURI(uri);

            if(image_path != null){
                Glide.with(this)
                        .load(image_path)
                        .into(imageView);

            }
        }

        btnShareImage = findViewById(R.id.linearShareImageView);
        btnDownloadImage = findViewById(R.id.linearDownloadImageView);

        btnDownloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.SAVE_FOLDER_NAME;
                downloadImages(image_path,destPath);
            }
        });

        btnShareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(image_path != null){
                    shareImage();
                }
            }
        });


    }

    public void shareImage() {
        File file = new File(uri.getPath());
        Intent i  = new Intent(Intent.ACTION_SEND);
        Uri myUri = FileProvider.getUriForFile(this,getApplicationContext().getPackageName()+".provider",file);

        i.setDataAndType(myUri,"image/*");
        i.putExtra(Intent.EXTRA_STREAM, myUri);
        startActivity(i);

//
//        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//        sharingIntent.setType("image/jpg");
//        File fileToShare = new File(image_path);
//        Uri uri = Uri.fromFile(fileToShare);
//        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        startActivity(Intent.createChooser(sharingIntent, "Share Via"));
    }

    private void downloadImages(String srcDir, String dstDir) {

    try {
        File src = new File(srcDir);
        File dst = new File(dstDir, src.getName());

        if(src.isDirectory()){
            String files[] = src.list();
            int filesLenth = files.length;

            for(int i=0; i<filesLenth; i++){
                String src1 = (new File(src, files[i]).getPath());
                String dst1 = dst.getPath();

                downloadImages(src1, dst1);
            }
        }else{
            copyFile(src, dst);
        }
        }catch (Exception e){
        e.printStackTrace();
    }
    }

    public void copyFile(File sourceFile, File destFile) throws IOException {
        if(!destFile.getParentFile().exists())
            destFile.getParentFile().mkdir();

        if(!destFile.exists()){
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source,0,source.size());
            Toast.makeText(this, "Image Saved "+ destFile.toString()+ new StoryModel().getFilename(), Toast.LENGTH_SHORT).show();
        }finally {
            if(source != null){
                source.close();
            }
            if(destination != null){
                destination.close();
            }
        }
    }

    // for image share




}