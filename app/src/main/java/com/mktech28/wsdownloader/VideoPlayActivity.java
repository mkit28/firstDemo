package com.mktech28.wsdownloader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.mktech28.wsdownloader.models.Constant;
import com.mktech28.wsdownloader.models.StoryModel;
import com.mktech28.wsdownloader.utils.Observer;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class VideoPlayActivity extends AppCompatActivity {

    String video_path="";
    VideoView videoView;
    public List<Observer> observers;
    String listenerValue;
    private int position = 0;
    private ImageView shareImageView, downloadImageView;
    ArrayList<Object>  fileList;
    String path;
    Uri uri;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();

        shareImageView = findViewById(R.id.linearShareImageView);
        downloadImageView = findViewById(R.id.linearDownloadImageView);



        videoView = findViewById(R.id.video_view);

        Intent intent = getIntent();
        if(intent != null){
            video_path = intent.getStringExtra("video");
            uri = intent.getParcelableExtra("uri");
            videoView.setVideoURI(uri);

            videoView.setVideoPath(video_path);
            videoView.start();

        }

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        try {
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {

                    videoView.seekTo(position);
                    if(position == 0){
                        videoView.start();
                    }else{
                        videoView.resume();
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }



        downloadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.SAVE_FOLDER_NAME;
                downloadVideos(video_path, path);
            }
        });


        shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareVideo();
            }
        });
    }

    private void shareVideo() {

        File file = new File(uri.getPath());
        Intent shareVideoIntent = new Intent(Intent.ACTION_SEND);
        Uri myUri = FileProvider.getUriForFile(this,getApplicationContext().getPackageName() + ".provider", file);
        shareVideoIntent.setDataAndType(myUri,"video/*");
        shareVideoIntent.putExtra(Intent.EXTRA_STREAM, myUri);
        startActivity(shareVideoIntent);
    }


    private void downloadVideos(String srcDir, String dstDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if(src.isDirectory()){
                String files[] = src.list();
                int fileLength = files.length;
                for(int i=0; i<fileLength; i++){
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();

                    downloadVideos(src1, dst1);
                }
            }else{
                copyFile(src, dst);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void copyFile(File sourceFile, File destFile) throws IOException{
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
            destination.transferFrom(source, 0, source.size());
            Toast.makeText(getApplicationContext(), "Video Saved "+ destFile.toString()+ new StoryModel().getFilename(), Toast.LENGTH_SHORT).show();
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("Position",videoView.getCurrentPosition());
        videoView.pause();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt("Position");
        videoView.seekTo(position);
    }

    @Override
    protected void onResume() {
        videoView.start();
        super.onResume();
    }
}