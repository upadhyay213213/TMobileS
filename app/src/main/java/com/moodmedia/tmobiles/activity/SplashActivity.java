package com.moodmedia.tmobiles.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;

import com.moodmedia.tmobiles.R;
import com.moodmedia.tmobiles.database.SongsUtil;
import com.moodmedia.tmobiles.database.SqliteHelper;
import com.moodmedia.tmobiles.webclient.SongsListResponse;

import java.util.ArrayList;

public class SplashActivity extends Activity {
    private static final String SHARED_PREFS_FILE = "pref";
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ms_splash);
        verifyStoragePermissions(SplashActivity.this);
        if(!getRawStorageStatus()) {
            saveDataToDb();
        }

            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashActivity.this, PlaySongActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }


    private void saveDataToDb(){
        String RES_PREFIX = "android.resource://com.moodmedia.tmobiles/";
        Uri test = Uri.parse(RES_PREFIX + R.raw.aaj_jane);
        Uri test1 = Uri.parse(RES_PREFIX + R.raw.ae_dil);
        Uri test2 = Uri.parse(RES_PREFIX + R.raw.an_evening);
        Uri test3 = Uri.parse(RES_PREFIX + R.raw.bulleya);
        Uri test4 = Uri.parse(RES_PREFIX + R.raw.channa);

        String []rawsongname={test.toString(),test1.toString(),test2.toString(),test3.toString(),test4.toString()};

        ArrayList<SongsListResponse> songList=new ArrayList<>();
        for(int i=0;i < rawsongname.length;i++) {
            SongsListResponse songsListResponse = new SongsListResponse();
            songsListResponse.setSdCardPath(rawsongname[i]);
            songsListResponse.setName("Test_"+String.valueOf(i+1));
            songsListResponse.setAlbum("Test");
            songsListResponse.setSingers("Test");
            songsListResponse.setModId("Test");
            songsListResponse.setMusicCompany("Test");
            songsListResponse.setId("temp_"+String.valueOf(i));
            songsListResponse.setTrackId("Test");

            songList.add(songsListResponse);
        }
        SqliteHelper.init(SplashActivity.this);
        SongsUtil.addRawSongsToDb(songList);
        storeFlagInpreferance();

    }

    private void storeFlagInpreferance(){
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("IsRawDbStore", true);
        editor.commit();
    }

    private boolean getRawStorageStatus(){
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        return prefs.getBoolean("IsRawDbStore", false);
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
