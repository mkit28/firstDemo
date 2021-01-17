package com.mktech28.wsdownloader.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Config {
    public static final String WhatsAppDirectoryPath = "/storage/emulated/0/WhatsApp/Media/.Statuses/";
    public static final String WhatsAppSaveStatus = "/storage/emulated/0/WhatsAppStatusesDir/Media/WhatsApp/";

    public static final String GBWhatsAppDirectoryPath = "/storage/emulated/0/GWAActivity/Media/.Statuses/";
    public static final String GBWhatsAppSaveStatus = "/storage/emulated/0/WhatsAppStatusesDir/Media/GBWhatsApp/";

    public static final String BusinessWhatsAppDirectoryPath = "/storage/emulated/0/WhatsApp Business/Media/.Statuses/";
    public static final String BusinessWhatsAppSaveStatus = "/storage/emulated/0/WhatsAppStatusesDir/Media/WhatsAppBusiness/";

    public static final int count = 6;

    public static String getAllState(Context context){
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE",Context.MODE_PRIVATE);
        if(prefs.getString("ALL","").length() > 0){
            return prefs.getString("ALL","");
        }else{
            return "";
        }
    }


}
