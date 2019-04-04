package com.example.johnfash.musicplayer;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;

import static android.widget.SeekBar.*;

public class MusicScreen extends AppCompatActivity {
    private static final int UPDATE_FREQUENCY = 500;
    private static final int STEP_VALUE = 10000;
    private TextView selectedFile = null;
    private SeekBar seekBar = null;
    public MediaPlayer player = null;
    public String musicName;
    public int onOffPlayButton;
    private ImageButton playButton = null;
    private ImageButton prevButton = null;
    private ImageButton nextButton = null;
    public boolean isStarted = true;
    private String currentFile;
    public boolean isMovingSeekBar = false;
    public final Handler handler = new Handler();
    public final Runnable updatePositionRunnable = new Runnable() {
        public void run() {
            updatePosition();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        musicName = getIntent().getStringExtra("MusicName");
        musicName = musicName.substring(0,1).toUpperCase()+musicName.substring(1);
        isStarted = getIntent().getBooleanExtra("isStarted", true);
        if (musicName.length() > 50) {
            musicName = musicName.substring(0, 50) + "...";
        }
        currentFile = getIntent().getStringExtra("CurrentFile");
        onOffPlayButton = getIntent().getIntExtra("onOffPlayButton", 0);
        setContentView(R.layout.activity_music_screen);
        selectedFile = findViewById(R.id.selectedfile);
        seekBar = findViewById(R.id.seekbar);
        playButton = findViewById(R.id.play);
        prevButton = findViewById(R.id.prev);
        nextButton = findViewById(R.id.next);

        if (onOffPlayButton == 1) {
            playButton.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            playButton.setImageResource(android.R.drawable.ic_media_play);
        }

        player = AudioPlay.getPlayer();
        player.setOnCompletionListener(onCompletion);
        player.setOnErrorListener(onError);
        seekBar.setOnSeekBarChangeListener(seekBarChange);
        playButton.setOnClickListener(onButtonClick);
        nextButton.setOnClickListener(onButtonClick);
        prevButton.setOnClickListener(onButtonClick);
        selectedFile.setText(musicName);
        seekBar.setMax(AudioPlay.getMediaDuration());
        updatePosition();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updatePositionRunnable);
        AudioPlay.stopAudio();
        AudioPlay.releaseAudio();
        player = null;
    }

    private void startPlay(String file) {
        seekBar.setProgress(0);
        AudioPlay.stopAudio();
        AudioPlay.setDataSource(file);
        seekBar.setMax(AudioPlay.getMediaDuration());
        onOffPlayButton = 1;
        playButton.setImageResource(android.R.drawable.ic_media_pause);
        updatePosition();
        isStarted = true;
    }

    private void stopPlay() {
        AudioPlay.stopAudio();
        onOffPlayButton = 0;
        playButton.setImageResource(android.R.drawable.ic_media_play);
        handler.removeCallbacks(updatePositionRunnable);
        seekBar.setProgress(0);
        isStarted = false;
    }

    public void updatePosition() {
        handler.removeCallbacks(updatePositionRunnable);
        seekBar.setProgress(AudioPlay.getMediaCurrentPosition());
        handler.postDelayed(updatePositionRunnable, UPDATE_FREQUENCY);
    }

    private View.OnClickListener onButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.play: {
                    if (AudioPlay.isPlaying()) {
                        handler.removeCallbacks(updatePositionRunnable);
                        AudioPlay.pauseAudio();
                        onOffPlayButton = 0;
                        playButton.setImageResource(android.R.drawable.ic_media_play);
                    } else {
                        if (isStarted) {
                            AudioPlay.startAudio();
                            onOffPlayButton = 1;
                            playButton.setImageResource(android.R.drawable.ic_media_pause);
                            updatePosition();
                        } else {
                            startPlay(currentFile);
                        }
                    }
                    break;
                }
                case R.id.next: {
                    int seekto = AudioPlay.getMediaCurrentPosition() + STEP_VALUE;
                    if (seekto > AudioPlay.getMediaDuration())
                        seekto = AudioPlay.getMediaDuration();
                    AudioPlay.pauseAudio();
                    AudioPlay.seekTo(seekto);
                    playButton.setImageResource(android.R.drawable.ic_media_pause);
                    AudioPlay.startAudio();
                    onOffPlayButton = 1;
                    break;
                }
                case R.id.prev: {
                    int seekto = AudioPlay.getMediaCurrentPosition() - STEP_VALUE;
                    if (seekto < 0)
                        seekto = 0;
                    AudioPlay.pauseAudio();
                    AudioPlay.seekTo(seekto);
                    playButton.setImageResource(android.R.drawable.ic_media_pause);
                    AudioPlay.startAudio();
                    onOffPlayButton = 1;
                    break;
                }
            }
        }
    };

    public MediaPlayer.OnCompletionListener onCompletion = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            stopPlay();
        }
    };

    public MediaPlayer.OnErrorListener onError = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }
    };

    public SeekBar.OnSeekBarChangeListener seekBarChange = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (isMovingSeekBar) {
                AudioPlay.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isMovingSeekBar = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            isMovingSeekBar = false;
        }
    };

    @Override
    public void onBackPressed() {
        Intent change = new Intent(MusicScreen.this, Music.class);
        change.putExtra("MusicName", musicName);
        change.putExtra("onOffPlayButton", onOffPlayButton);
        change.putExtra("isStarted", isStarted);
        change.putExtra("CurrentFile", currentFile);
        startActivity(change);
    }
}

