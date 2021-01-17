package com.mktech28.wsdownloader.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mktech28.wsdownloader.BuildConfig;
import com.mktech28.wsdownloader.ImageViewActivity;
import com.mktech28.wsdownloader.R;
import com.mktech28.wsdownloader.VideoPlayActivity;
import com.mktech28.wsdownloader.models.Constant;
import com.mktech28.wsdownloader.models.StoryModel;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Object> fileList;
    String path="";
    Uri uri;

    public StoryAdapter(Context mContext, ArrayList<Object> fileList) {
        this.mContext = mContext;
        this.fileList = fileList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(mContext).inflate(R.layout.all_status, parent, false);
       return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final StoryModel files = (StoryModel) fileList.get(position);

        if(files.getUri().toString().endsWith(".mp4")){
            holder.playIcon.setVisibility(View.VISIBLE);
        }else {
            holder.playIcon.setVisibility(View.INVISIBLE);
        }


        Glide.with(mContext)
                .load(files.getUri())
                .into(holder.saveImage);

        holder.shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File imageFile = new File(files.getPath());
                Uri photoUri = Uri.fromFile(imageFile);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    photoUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID +".provider", imageFile);
                }

                shareImageIntent(photoUri);
            }
        });


        holder.downloadId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkFolder();

                final String path = ((StoryModel)fileList.get(position)).getPath();
                final File file =  new File(path);

                String destPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.SAVE_FOLDER_NAME;
                File destFile = new File(destPath);

                try {
                    FileUtils.copyFileToDirectory(file,destFile);

                } catch (IOException e) {

                    e.printStackTrace();
                }

                MediaScannerConnection.scanFile(
                        mContext,
                        new String[]{destPath + files.getFilename()},
                        new String[]{"*/*"},
                        new MediaScannerConnection.MediaScannerConnectionClient() {
                            @Override
                            public void onMediaScannerConnected() {

                            }

                            @Override
                            public void onScanCompleted(String s, Uri uri) {

                            }
                        }
                );

                Toast.makeText(mContext, "Saved to: "+destPath + files.getFilename(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void shareImageIntent(Uri photoUri) {

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, photoUri);
        mContext.startActivity(Intent.createChooser(share, "Sharing.."));
    }

    private void checkFolder() {

        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.SAVE_FOLDER_NAME;
        File dir = new File(path);

        boolean isDirectoryCreated = dir.exists();
        if(!isDirectoryCreated){
            isDirectoryCreated = dir.mkdir();
        }
        if(isDirectoryCreated){
            Log.d("Folder","Already Created");
        }
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView saveImage, playIcon, downloadId,shareImageView;
        private CardView parent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            parent = itemView.findViewById(R.id.cardView);
            saveImage = itemView.findViewById(R.id.mainImageView);
            playIcon = itemView.findViewById(R.id.playButtonImage);
            downloadId = itemView.findViewById(R.id.imageViewDownload);
            shareImageView = itemView.findViewById(R.id.imageViewShare);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StoryModel modelStatus = (StoryModel) fileList.get(getAdapterPosition());

                    if(modelStatus.getUri().toString().endsWith(".mp4")){

                        Intent intent = new Intent(mContext, VideoPlayActivity.class);
                        intent.putExtra("video",modelStatus.getPath());
                        intent.putExtra("uri",modelStatus.getUri());
                        mContext.startActivity(intent);

                    }else{
                        Intent intent = new Intent(mContext, ImageViewActivity.class);
                        intent.putExtra("image",modelStatus.getPath());
                        intent.putExtra("uri",modelStatus.getUri());
                        mContext.startActivity(intent);
                    }
                }
            });



        }
    }
}
