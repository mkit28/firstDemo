package com.mktech28.wsdownloader.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mktech28.wsdownloader.R;
import com.mktech28.wsdownloader.activity.DrawerLayoutActivity;
import com.mktech28.wsdownloader.activity.ImageViewerActivity;
import com.mktech28.wsdownloader.models.ModelStatuses;
import com.mktech28.wsdownloader.utils.Config;
import com.mktech28.wsdownloader.utils.Observer;
import com.mktech28.wsdownloader.utils.Subject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;


public class WAPictureAdapter extends RecyclerView.Adapter<WAPictureAdapter.MyViewHolder> implements Subject {

    private final String TAG = "PictureAdapter";
    public int count = Config.count;
    public List<Observer> observers;
    String listenerValue = "";
    DrawerLayoutActivity drawer = new DrawerLayoutActivity();
    private Context mContext;
    private ArrayList<ModelStatuses> arrayList;


    public WAPictureAdapter(Context mContext, ArrayList<ModelStatuses> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        observers = new ArrayList<>();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pictures, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ModelStatuses current = arrayList.get(position);
        Glide.with(mContext)
                .load(current.getFull_path())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void register(Observer observer) {

        if(!observers.contains(observer)){
            observers.add(observer);
        }
    }

    @Override
    public void unregister(Observer observer) {

        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {

        for(final Observer observer : observers){
            Log.v("KKKKK", ""+listenerValue);
            observer.update(listenerValue, mContext);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private CardView mCardView;
        private ImageView imageView;
        private LinearLayout btn_download, btn_share;
        private ImageButton img_btn_download, img_btn_share;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mCardView = itemView.findViewById(R.id.card_view_item_pictuer);
            imageView = itemView.findViewById(R.id.imageView);
            btn_download = itemView.findViewById(R.id.btn_download);

            btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ModelStatuses modelStatuses = arrayList.get(getAdapterPosition());
                    String path = "";
                    /* type == 0 (WhatsApp Normal)
                      type == 1 (WhatsApp GB);
                      type == 2 (WhatsApp Business)
                     */
                    if(modelStatuses.getType() == 0){
                        path = Config.WhatsAppSaveStatus;

                    }else if(modelStatuses.getType() == 1){
                        path = Config.GBWhatsAppSaveStatus;

                    }else if(modelStatuses.getType() == 2){
                        path = Config.BusinessWhatsAppSaveStatus;

                    }
                    copyFileOrDirectory(modelStatuses.getPath(), path);
                    Toast.makeText(mContext, modelStatuses.getPath(), Toast.LENGTH_SHORT).show();
                    sharePerAds();
                }
            });

            btn_share = itemView.findViewById(R.id.btn_share);
            btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ModelStatuses modelStatuses = arrayList.get(getAdapterPosition());

                    if(modelStatuses.getFull_path().endsWith(".jpg")){
                        shareVia("image/jpg", modelStatuses.getPath());
                    }else if(modelStatuses.getFull_path().endsWith(".mp4")){
                        shareVia("video/mp4", modelStatuses.getPath());
                    }
                    sharePerAds();
                }
            });

            img_btn_share = itemView.findViewById(R.id.img_btn_share);
            img_btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    ModelStatuses modelStatuses = arrayList.get(getAdapterPosition());
                    if(modelStatuses.getFull_path().endsWith(".jpg")){
                        shareVia("image/jpg", modelStatuses.getPath());
                    }else if(modelStatuses.getFull_path().endsWith(".mp4")){
                        shareVia("video/mp4", modelStatuses.getPath());
                    }
                    sharePerAds();
                }
            });

            img_btn_download = itemView.findViewById(R.id.img_btn_download);
            img_btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ModelStatuses modelStatuses = arrayList.get(getAdapterPosition());
                    String path = "";
                    /*
                      type == 0 (WhatsApp Normal)
                      type == 1 (WhatsApp GB)
                      type == 2 (WhatsApp Business)
                     */

                    if(modelStatuses.getType() == 0){
                        path = Config.WhatsAppSaveStatus;

                    }else if(modelStatuses.getType() == 1){
                        path = Config.GBWhatsAppSaveStatus;

                    }else if(modelStatuses.getType() == 2){
                        path = Config.BusinessWhatsAppSaveStatus;

                    }

                    if(modelStatuses.getFull_path().endsWith(".jpg")){
                        copyFileOrDirectory(modelStatuses.getPath(), path);
                        String hello = path + modelStatuses.getPath()+".jpg";
                        File hello2 = new File(hello);

                        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(hello2)));
                    }
                    else if(modelStatuses.getFull_path().endsWith(".mp4")){
                        copyFileOrDirectory(modelStatuses.getPath(),path);
                        String hello = path + modelStatuses.getPath()+".mp4";
                        File hello2 = new File(hello);

                        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(hello2)));

                    }
                    sharePerAds();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ModelStatuses modelStatuses = arrayList.get(getAdapterPosition());

                    Intent intent = new Intent(mContext, ImageViewerActivity.class);
                    intent.putExtra("image", modelStatuses.getFull_path());
                    intent.putExtra("type", ""+modelStatuses.getType());
                    intent.putExtra("atype","1");
                    mContext.startActivity(intent);
                }
            });

        }
    }

    private void sharePerAds() {
        int i;
        if(Config.getAllState(mContext).length() > 0){
            i = Integer.parseInt(Config.getAllState(mContext));
            if(i > count){
                allSharePreference(0);
            }else{
                i++;
                allSharePreference(i);
            }
        }else {
            i = 1;
            allSharePreference(i);
        }
        listenerValue = String.valueOf(i);
        register(drawer);
        notifyObservers();
        unregister(drawer);
    }


    private void allSharePreference(int i) {
        SharedPreferences preferences = mContext.getSharedPreferences("PREFRENCE",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ALL", String.valueOf(i));
        editor.commit();
    }

    private void shareVia(String type, String path) {

        Intent shareingIntent = new Intent(Intent.ACTION_SEND);
        shareingIntent.setType(type);
        File fileToShare = new File(path);
        Uri uri = Uri.fromFile(fileToShare);
        shareingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        mContext.startActivity(Intent.createChooser(shareingIntent, "Share via.."));

    }

    private void copyFileOrDirectory(String srcDir, String dstDir) {
        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if(src.isDirectory()){
                String files[] = src.list();
                int filesLength = files.length;

                for(int i=0; i<filesLength; i++){
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);
                }
            }else
            {
                copyFile(src, dst);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void copyFile(File sourceFile, File destFile) throws IOException{
        if(!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if(!destFile.exists()){
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
            Toast.makeText(mContext, "Picture Saved..", Toast.LENGTH_SHORT).show();
        }finally {
            if(source != null){
                source.close();
            }
            if(destination != null){
                destination.close();
            }
        }

    }


}
