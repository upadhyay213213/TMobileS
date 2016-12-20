package com.moodmedia.tmobiles.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.moodmedia.tmobiles.activity.PlaySongActivity;
import com.moodmedia.tmobiles.common.Constant;
import com.moodmedia.tmobiles.database.SongsUtil;
import com.moodmedia.tmobiles.webclient.ServerCall;
import com.moodmedia.tmobiles.webclient.ServiceGenerator;
import com.moodmedia.tmobiles.model.SongDetails;
import com.moodmedia.tmobiles.model.SongsListResponse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class DownloadService extends Service {
    private static final String TAG = "DownloadService";
    private static final String EXTRA_TRACK_URL = "urls";
    public static final int UPDATE_PROGRESS = 0;

    public static void startDownloadingSongs(PlaySongActivity context) {
        Intent intent = new Intent(context, DownloadService.class);
        context.startService(intent);
    }

    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            ArrayList<SongsListResponse> trackIds = SongsUtil.getUnDownloadedSongsToDownLoad();
            if(trackIds==null){
                Toast.makeText(this,"All songs are downloaded",Toast.LENGTH_SHORT).show();
                stopSelf();
                Log.d("All Songs", "All Songs downlaoded to SD card");
            }else {
                for (int i = 0; i < trackIds.size(); i++) {
                    Log.d("Ids to downloadsongs",trackIds.toString());
                    new GetSongDetails().execute(trackIds.get(i).getId());
                }
            }
        }
        return START_STICKY;
    }

    /**
     * To Get songs details from according to song id
     */
    class GetSongDetails extends AsyncTask<String, Void, String> {

        private SongDetails songDetails;
        private ServerCall retrofitInterface;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            retrofitInterface = ServiceGenerator.getRestService(Constant.ApiForConfigurations.BASE_URL);
        }

        @Override
        protected String doInBackground(String... lists) {
            try {
                Call<SongDetails> songsUrl = retrofitInterface.getSongsDetailsWithURL(lists[0]);
                songDetails = songsUrl.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SongsUtil.updateDownloadUrlPath(songDetails.getDownloadUrl(), songDetails.getId());
            return songDetails.getDownloadUrl();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new DownloadSong(retrofitInterface,  songDetails.getId()).execute(s);
        }
    }


    /*
    To download from song at given url
     */
    private class DownloadSong extends AsyncTask<String, Void, Void> {

        private final ServerCall retrofitInterface;
        private int trackNumber;
        private String songId;

        public DownloadSong(ServerCall retrofitInterface, String songId) {
            this.retrofitInterface = retrofitInterface;
            this.songId = songId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... url) {
            Call<ResponseBody> request = retrofitInterface.downloadFile(url[0]);
            try {
                downloadFile(request.execute().body(),songId);
                Log.d("songId", songId);

            } catch (IOException e) {

                e.printStackTrace();
                Log.e("Download Error", e.getMessage());
                //Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    /**
     * To save downloaded song on SD card
     *
     * @param body
     * @throws IOException
     */
    private void downloadFile(ResponseBody body,String songId) throws IOException {
        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
        File outputFile = new File(getExternalFilesDir(null) + File.separator + "moodmedia_track_" + songId + ".mp3");
        OutputStream output = new FileOutputStream(outputFile);
        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;
        while ((count = bis.read(data)) != -1) {

            total += count;
            double current = Math.round(total / (Math.pow(1024, 2)));

            int progress = (int) ((total * 100) / fileSize);

            long currentTime = System.currentTimeMillis() - startTime;
            if (currentTime > 1000 * timeCount) {
                Log.d(TAG, "file download: " + progress + " of " + fileSize);
                timeCount++;
            }

            output.write(data, 0, count);
        }
        if (outputFile.length() == fileSize) {//song is downloaded
            SongsUtil.updateSdCardPathAndDownloadStatus(outputFile.getPath(), songId);
        }
        output.flush();
        output.close();
        bis.close();
        if(!SongsUtil.isUnDownLoadedSongSongPresent()){
            stopSelf();
        }

    }


}
