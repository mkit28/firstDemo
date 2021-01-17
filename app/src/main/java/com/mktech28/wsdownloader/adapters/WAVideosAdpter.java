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

import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mktech28.wsdownloader.R;
import com.mktech28.wsdownloader.activity.DrawerLayoutActivity;
import com.mktech28.wsdownloader.activity.ImageViewerActivity;
import com.mktech28.wsdownloader.activity.VideoViewerActivity;
import com.mktech28.wsdownloader.models.ModelStatuses;
import com.mktech28.wsdownloader.utils.Config;
import com.mktech28.wsdownloader.utils.Observer;
import com.mktech28.wsdownloader.utils.Subject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class WAVideosAdpter extends RecyclerView.Adapter<WAVideosAdpter.MyViewHolder> implements Subject {


    private final String TAG = "mk";
    public int count = Config.count;
    public List<Observer> observers;
    String listenerValue = "";
    DrawerLayoutActivity drawer = new DrawerLayoutActivity();
    private Context acontext;
    private ArrayList<ModelStatuses> arrayList;

    public WAVideosAdpter(Context context, ArrayList<ModelStatuses> arrayList) {
        this.arrayList = arrayList;
        acontext = context;
        observers = new ArrayList<>();
    }

    @Override
    public WAVideosAdpter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_videos, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ModelStatuses current = arrayList.get(position);
        Glide.with(acontext).load(current.getFull_path())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void copyFileOrDirectory(String srcDir, String dstDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {

                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);

                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
            Toast.makeText(acontext, "Video Saved", Toast.LENGTH_SHORT).show();
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    public void shareVia(String type, String path) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType(type);
        File fileToShare = new File(path);
        Uri uri = Uri.fromFile(fileToShare);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        acontext.startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }

    public void allSharedPreference(int i) {
        SharedPreferences preferences = acontext.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ALL", String.valueOf(i));
        editor.commit();
    }

    public void sharePerAds() {
        int i;
        if (Config.getAllState(acontext).length() > 0) {
            i = Integer.parseInt(Config.getAllState(acontext));
            if (i > count) {

                allSharedPreference(0);
            } else {
                i++;
                allSharedPreference(i);
            }
        } else {
            i = 1;
            allSharedPreference(i);
        }
        listenerValue = String.valueOf(i);
        register(drawer);
        notifyObservers();
        unregister(drawer);
    }

    @Override
    public void register(final Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void unregister(final Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (final Observer observer : observers) {
            Log.v("KKKKKKKKK", "" + listenerValue);
            observer.update(listenerValue, acontext);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CardView mCardView;
        public ImageView imageView;
        public LinearLayout btn_download, btn_share;
        public ImageButton img_btn_download, img_btn_share;


        public MyViewHolder(View v) {
            super(v);
            mCardView = v.findViewById(R.id.card_view_order_cancel);

            imageView = v.findViewById(R.id.imageView);

            btn_download = v.findViewById(R.id.btn_download);
            btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModelStatuses modelStatus = arrayList.get(getAdapterPosition());
                    String path = "";
                   /* type == 0 (WhatsApp Normal)
                    type == 1 (WhatsApp GB)
                    type == 2 (WhatsApp Business)*/
                    if (modelStatus.getType() == 0) {
                        path = Config.WhatsAppSaveStatus;

                    } else if (modelStatus.getType() == 1) {
                        path = Config.GBWhatsAppSaveStatus;

                    } else if (modelStatus.getType() == 2) {
                        path = Config.BusinessWhatsAppSaveStatus;

                    }
                    copyFileOrDirectory(modelStatus.getPath(), path);

                    sharePerAds();
                }
            });

            btn_share = v.findViewById(R.id.btn_share);
            btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModelStatuses modelStatus = arrayList.get(getAdapterPosition());

                    if (modelStatus.getFull_path().endsWith(".jpg")) {
                        shareVia("image/jpg", modelStatus.getPath());
                    } else if (modelStatus.getFull_path().endsWith(".mp4")) {
                        shareVia("video/mp4", modelStatus.getPath());
                    }


                    sharePerAds();

                }
            });
            img_btn_share = v.findViewById(R.id.img_btn_share);
            img_btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModelStatuses modelStatus = arrayList.get(getAdapterPosition());

                    if (modelStatus.getFull_path().endsWith(".jpg")) {
                        shareVia("image/jpg", modelStatus.getPath());
                    } else if (modelStatus.getFull_path().endsWith(".mp4")) {
                        shareVia("video/mp4", modelStatus.getPath());
                    }

                    sharePerAds();

                }
            });

            img_btn_download = v.findViewById(R.id.img_btn_download);
            img_btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModelStatuses modelStatus = arrayList.get(getAdapterPosition());
                    String path = "";
                   /* type == 0 (WhatsApp Normal)
                    type == 1 (WhatsApp GB)
                    type == 2 (WhatsApp Business)*/
                    if (modelStatus.getType() == 0) {
                        path = Config.WhatsAppSaveStatus;

                    } else if (modelStatus.getType() == 1) {
                        path = Config.GBWhatsAppSaveStatus;

                    } else if (modelStatus.getType() == 2) {
                        path = Config.BusinessWhatsAppSaveStatus;

                    }

                    if (modelStatus.getFull_path().endsWith(".jpg")) {
                        copyFileOrDirectory(modelStatus.getPath(), path);
                        String hello = path + modelStatus.getPath() + ".jpg";
                        File hello2 = new File(hello);

                        acontext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(hello2)));
                        //   Toast.makeText(acontext,hello,Toast.LENGTH_SHORT).show();

                    } else if (modelStatus.getFull_path().endsWith(".mp4")) {
                        copyFileOrDirectory(modelStatus.getFull_path(), path);
                        String hello = path + modelStatus.getPath() + ".mp4";
                        File hello2 = new File(hello);
                        acontext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(hello2)));
                        // Toast.makeText(acontext,hello,Toast.LENGTH_SHORT).show();
                    }

                    sharePerAds();
                }

                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ModelStatuses modelStatuses = arrayList.get(getAdapterPosition());

                        Intent intent = new Intent(acontext, VideoViewerActivity.class);
                        Log.d(TAG, "onClick: "+"data send success: "+ modelStatuses.getFull_path());
                        intent.putExtra("video", modelStatuses.getFull_path());
                        intent.putExtra("type", "" + modelStatuses.getType());
                        intent.putExtra("atype", "1");
                        acontext.startActivity(intent);
                    }
                });
            }

        }
    }