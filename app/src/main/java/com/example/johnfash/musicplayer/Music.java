package com.example.johnfash.musicplayer;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.math.BigDecimal;

public class Music extends ListActivity {
    private static final int UPDATE_FREQUENCY = 500;
    private static final int STEP_VALUE = 10000;
    private MediaCursorAdapter mediaAdapter = null;
    private TextView selectedFile = null;
    public Cursor myCursor;
    public LinearLayout click;
    public MediaPlayer player = null;
    private ImageButton playButton = null;
    private ImageButton prevButton = null;
    private ImageButton nextButton = null;
    public boolean isStarted = true;
    public int onOffPlayButton;
    public String musicName;
    public String currentFile;
    public final Handler handler = new Handler();
    public final Runnable updatePositionRunnable = new Runnable() {
        public void run() {
            updatePosition();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        musicName = getIntent().getStringExtra("MusicName");
        onOffPlayButton = getIntent().getIntExtra("onOffPlayButton", 0);
        isStarted = getIntent().getBooleanExtra("isStarted", true);
        currentFile = getIntent().getStringExtra("CurrentFile");
        setContentView(R.layout.activity_music);
        selectedFile = findViewById(R.id.selectedfile);
        if (musicName == null) {
            selectedFile.setText("No file selected");
        } else {
            selectedFile.setText(musicName);
        }
        click = findViewById(R.id.click);
        playButton = findViewById(R.id.play);
        prevButton = findViewById(R.id.prev);
        nextButton = findViewById(R.id.next);
        if (onOffPlayButton == 1) {
            playButton.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            playButton.setImageResource(android.R.drawable.ic_media_play);
        }
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFile.getText().toString().equals("No file selected")) {
                } else {
                    Intent change = new Intent(Music.this, MusicScreen.class);
                    change.putExtra("MusicName", musicName);
                    change.putExtra("CurrentFile", currentFile);
                    change.putExtra("onOffPlayButton", onOffPlayButton);
                    change.putExtra("isStarted", isStarted);
                    startActivity(change);
                }
            }
        });
        player = AudioPlay.getPlayer();

        player.setOnCompletionListener(onCompletion);
        player.setOnErrorListener(onError);
        myCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

        if (null != myCursor) {
            myCursor.moveToFirst();

            mediaAdapter = new MediaCursorAdapter(this, R.layout.list_item, myCursor);
            setListAdapter(mediaAdapter);
            playButton.setOnClickListener(onButtonClick);
            nextButton.setOnClickListener(onButtonClick);
            prevButton.setOnClickListener(onButtonClick);
        }
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);
        currentFile = (String) view.getTag();
        startPlay(currentFile);
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
        musicName = file.substring(file.lastIndexOf("/") + 1);
        musicName = musicName.substring(0,1).toUpperCase()+musicName.substring(1);
        selectedFile.setText(musicName);
        AudioPlay.stopAudio();
        AudioPlay.setDataSource(file);
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
        isStarted = false;
    }

    public void updatePosition() {
        handler.removeCallbacks(updatePositionRunnable);
        handler.postDelayed(updatePositionRunnable, UPDATE_FREQUENCY);
    }

    private class MediaCursorAdapter extends SimpleCursorAdapter {
        public MediaCursorAdapter(Context context, int layout, Cursor c) {
            super(context, layout, c,
                    new String[]{MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.TITLE, MediaStore.Audio.AudioColumns.DURATION},
                    new int[]{R.id.displayname, R.id.title, R.id.duration}
            );
        }


        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView title = view.findViewById(R.id.title);
            TextView name = view.findViewById(R.id.displayname);
            TextView duration = view.findViewById(R.id.duration);
            String cutName = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));
            cutName = cutName.substring(0,1).toUpperCase()+cutName.substring(1);
            if (cutName.length() > 30) {
                cutName = cutName.substring(0, 25) + "...";
            }
            String cutTitle = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE));
            cutTitle = cutTitle.substring(0,1).toUpperCase()+cutTitle.substring(1);
            if (cutTitle.length() > 30) {
                cutTitle = cutTitle.substring(0, 25) + "...";
            }
            name.setText(cutName);
            title.setText(cutTitle);
            long durationInMs = Long.parseLong(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION)));
            double durationInMin = ((double) durationInMs / 1000.0) / 60.0;
            durationInMin = new BigDecimal(Double.toString(durationInMin)).setScale(2, BigDecimal.ROUND_UP).doubleValue();
            duration.setText("" + durationInMin);
            view.setTag(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)));
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.list_item, parent, false);
            bindView(v, context, cursor);
            return v;
        }
    }

    private View.OnClickListener onButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.play: {
                    if (selectedFile.getText().toString().equals("No file selected")) {

                    } else {
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
                    }
                    break;
                }
                case R.id.next: {
                    if (selectedFile.getText().toString().equals("No file selected")) {

                    } else {
                        int seekto = AudioPlay.getMediaCurrentPosition() + STEP_VALUE;
                        if (seekto > AudioPlay.getMediaDuration())
                            seekto = AudioPlay.getMediaDuration();
                        AudioPlay.pauseAudio();
                        AudioPlay.seekTo(seekto);
                        playButton.setImageResource(android.R.drawable.ic_media_pause);
                        AudioPlay.startAudio();
                        onOffPlayButton = 1;
                    }
                    break;
                }
                case R.id.prev: {
                    if (selectedFile.getText().toString().equals("No file selected")) {
                    } else {
                        int seekto = AudioPlay.getMediaCurrentPosition() - STEP_VALUE;
                        if (seekto < 0)
                            seekto = 0;
                        AudioPlay.pauseAudio();
                        AudioPlay.seekTo(seekto);
                        playButton.setImageResource(android.R.drawable.ic_media_pause);
                        AudioPlay.startAudio();
                        onOffPlayButton = 1;
                    }
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

    @Override
    public void onBackPressed() {
        Intent exitApp = new Intent(Intent.ACTION_MAIN);
        exitApp.addCategory(Intent.CATEGORY_HOME);
        exitApp.setFlags(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS);
        startActivity(exitApp);
    }

}

