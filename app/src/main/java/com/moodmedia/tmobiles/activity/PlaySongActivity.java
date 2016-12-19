package com.moodmedia.tmobiles.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.moodmedia.tmobiles.R;
import com.moodmedia.tmobiles.common.Constant;
import com.moodmedia.tmobiles.database.SongsUtil;
import com.moodmedia.tmobiles.database.SqliteHelper;
import com.moodmedia.tmobiles.mediaplayer.Utilities;
import com.moodmedia.tmobiles.service.DownloadService;
import com.moodmedia.tmobiles.webclient.ServerCall;
import com.moodmedia.tmobiles.webclient.ServiceGenerator;
import com.moodmedia.tmobiles.model.SongsListResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaySongActivity extends Activity implements View.OnClickListener, MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    private ImageView btnPlay;
    private ImageView btnNext;
    private MediaPlayer mp;
    private Utilities utils;
    private SeekBar songProgressBar;
    private Handler mHandler = new Handler();
    private TextView songTotalDurationLabel;
    private TextView songCurrentDurationLabel;
    private EditText songTitleLabel;
    private int currentSongIndex = 0;
    private ArrayList<SongsListResponse> songsListForDb;
    private EditText songAlbumName;
    private EditText songSinger;
    private boolean firstClick=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ms_music_player_new);
        initViews();
        final ServerCall mServerCall = ServiceGenerator.getRestService(Constant.ApiForConfigurations.BASE_URL);
        final Call<List<SongsListResponse>> songs = mServerCall.getSongsList();
        songs.enqueue(new Callback<List<SongsListResponse>>() {

            @Override
            public void onResponse(Call<List<SongsListResponse>> call, Response<List<SongsListResponse>> response) {
                List<SongsListResponse> songsList = response.body();
                SqliteHelper.init(PlaySongActivity.this);
                SongsUtil.addSongsToDb((ArrayList<SongsListResponse>) songsList);
                DownloadService.startDownloadingSongs(PlaySongActivity.this);
            }

            @Override
            public void onFailure(Call<List<SongsListResponse>> call, Throwable t) {
                Toast.makeText(PlaySongActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        btnPlay = (ImageView) findViewById(R.id.ivPlay);
        btnNext = (ImageView) findViewById(R.id.ivNext);
        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        songTotalDurationLabel = (TextView) findViewById(R.id.totalTimeStatus);
        songCurrentDurationLabel = (TextView) findViewById(R.id.currentTimeStatus);
        songTitleLabel = (EditText) findViewById(R.id.songTitle);
        songAlbumName = (EditText) findViewById(R.id.albumTitle);
        songSinger = (EditText) findViewById(R.id.artistTitle);
        btnPlay.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        mp = new MediaPlayer();
        utils = new Utilities();
        songProgressBar.setOnSeekBarChangeListener(this); // Important
        mp.setOnCompletionListener(this); // Important
        // Getting all songs list
        SqliteHelper.init(PlaySongActivity.this);
        songsListForDb = SongsUtil.getrawSongsListFromDb();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivPlay:
                if(firstClick) {
                    // By default play first song
                    playSong(0);
                    firstClick=false;
                }else {
                    // check for already playing
                    if (mp.isPlaying()) {
                        if (mp != null) {
                            mp.pause();
                            // Changing button image to play button
                            btnPlay.setImageResource(R.drawable.ms_play_icon);
                        }
                    } else {
                        // Resume song
                        if (mp != null) {
                            mp.start();
                            // Changing button image to pause button
                            btnPlay.setImageResource(R.drawable.ms_pause_icon);
                        }
                    }
                }
                break;
            case R.id.ivNext:
                playNextSong();

                break;
        }
    }

    /**
     * Function to play a song
     *
     * @param songIndex - index of song
     */
    public void playSong(int songIndex) {
        // Play song
        SongsListResponse currentSongs;
        if (songsListForDb.size() > 0) {
            Log.v("songIndex:", String.valueOf(songIndex));
            currentSongs = songsListForDb.get(songIndex);

            try {
                mp.reset();
                if (songIndex < 5) {
                    mp.setDataSource(getApplicationContext(), Uri.parse(currentSongs.getSdCardPath()));

                } else {
                    if (currentSongs.getSdCardPath() != null) {
                        mp.setDataSource(currentSongs.getSdCardPath());
                    } else {
                        Toast.makeText(PlaySongActivity.this, "Please wait song download in progress", Toast.LENGTH_LONG).show();
                    }
                }
                mp.prepare();
                mp.start();
                // Displaying Song title
                String songTitle = currentSongs.getName();
                songTitleLabel.setText(songTitle);
                songAlbumName.setText(currentSongs.getAlbum());
                songSinger.setText(currentSongs.getSingers());

                // Changing Button Image to pause image
                btnPlay.setImageResource(R.drawable.ms_pause_icon);

                // set Progress bar values
                songProgressBar.setProgress(0);
                songProgressBar.setMax(100);

                // Updating progress bar
                updateProgressBar();

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * Update timer on seekbar
     */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mp.getDuration();
            long currentDuration = mp.getCurrentPosition();

            // Displaying Total Duration time
            songTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            songCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            songProgressBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    /**
     * On Song Playing completed
     * if repeat is ON play same song again
     * if shuffle is ON play random song
     */
    @Override
    public void onCompletion(MediaPlayer arg0) {

        // check for repeat is ON or OFF

        // shuffle is on - play a random song
        // no repeat or shuffle ON - play next song
        playNextSong();
    }

    private void playNextSong(){
        if (currentSongIndex < (songsListForDb.size() - 1)) {
            playSong(currentSongIndex + 1);
            currentSongIndex = currentSongIndex + 1;
        } else {
            // play first song
            if (currentSongIndex > 4) {
                playSong(0);
                currentSongIndex = 0;
            } else {
                if (currentSongIndex == 4) {
                    songsListForDb.addAll(SongsUtil.getSongsListFromDb());
                }
                playSong(currentSongIndex + 1);
                currentSongIndex = currentSongIndex + 1;
            }
        }
    }
}
