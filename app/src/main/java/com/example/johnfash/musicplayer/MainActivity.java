package com.example.johnfash.musicplayer;

import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {
    private static final int UPDATE_FREQUENCY = 500;
    private static final int STEP_VALUE = 4000;

//    private MediaCursorAdapter mediaAdapter = null;
    private TextView selectedFile = null;
    private SeekBar seekBar = null;
    private MediaPlayer player = null;
    private ImageButton playButton = null;
    private ImageButton prevButton = null;
    private ImageButton nextButton = null;
    private boolean isStarted = true;
    private String currentFile = "";
    private boolean isMovingSeekBar = false;
    private final Handler handler = new Handler();
//    private final Runnable updatePositionRunnable = new Runnable() {
//        public void run() {
//            updatePosition();
//        }
//    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
//                    Toast.makeText(MainActivity.this,"hello",Toast.LENGTH_SHORT).show();
                    Intent change = new Intent(MainActivity.this, Music.class);
//                change.putExtra("NAME", name);
//                change.putExtra("CATEGORY", category);
                    startActivity(change);
//                    method call goes here
//                    cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
                }
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        }else {
            Intent change = new Intent(MainActivity.this, Music.class);
//                change.putExtra("NAME", name);
//                change.putExtra("CATEGORY", category);
            startActivity(change);
        }

        setContentView(R.layout.activity_main);

//        selectedFile = findViewById(R.id.selectedfile);
//        seekBar = findViewById(R.id.seekbar);
//        playButton = findViewById(R.id.play);
//        prevButton = findViewById(R.id.prev);
//        nextButton = findViewById(R.id.next);
//        player = new MediaPlayer();
//
//        player.setOnCompletionListener(onCompletion);
//        player.setOnErrorListener(onError);
//        seekBar.setOnSeekBarChangeListener(seekBarChange);
//
//        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
//
//        if (null != cursor) {
//            cursor.moveToFirst();
//
//            mediaAdapter = new MediaCursorAdapter(this, R.layout.list_item, cursor);
//            setListAdapter(mediaAdapter);
//            playButton.setOnClickListener(onButtonClick);
//            nextButton.setOnClickListener(onButtonClick);
//            prevButton.setOnClickListener(onButtonClick);
//        }

    }

//    @Override
//    protected void onListItemClick(ListView list, View view, int position, long id) {
//        super.onListItemClick(list, view, position, id);
//        currentFile = (String) view.getTag();
//        startPlay(currentFile);
//    }
//

//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        handler.removeCallbacks(updatePositionRunnable);
//        player.stop();
//        player.reset();
//        player.release();
//        player = null;
//    }

//    private void startPlay(String file) {
//
//        Log.i("Selected: ", file);
//        String fileName = file.substring(file.lastIndexOf("/") + 1);
//        selectedFile.setText(fileName);
//        seekBar.setProgress(0);
//
//        player.stop();
//        player.reset();
//
//        try {
//            player.setDataSource(file);
//            player.prepare();
//            player.start();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        seekBar.setMax(player.getDuration());
//        playButton.setImageResource(android.R.drawable.ic_media_pause);
//
//        updatePosition();
//        isStarted = true;
//
//    }

//    private void stopPlay() {
//        player.stop();
//        player.reset();
//        playButton.setImageResource(android.R.drawable.ic_media_play);
//        handler.removeCallbacks(updatePositionRunnable);
//        seekBar.setProgress(0);
//        isStarted = false;
//    }
//
//    private void updatePosition() {
//        handler.removeCallbacks(updatePositionRunnable);
//        seekBar.setProgress(player.getCurrentPosition());
//        handler.postDelayed(updatePositionRunnable, UPDATE_FREQUENCY);
//    }
//
//    private class MediaCursorAdapter extends SimpleCursorAdapter {
//        public MediaCursorAdapter(Context context, int layout, Cursor c) {
//            super(context, layout, c,
//                    new String[]{MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.TITLE, MediaStore.Audio.AudioColumns.DURATION},
//                    new int[]{R.id.displayname, R.id.title, R.id.duration}
//            );
//        }


//        @Override
//        public void bindView(View view, Context context, Cursor cursor) {
//            TextView title = view.findViewById(R.id.title);
//            TextView name = view.findViewById(R.id.displayname);
//            TextView duration = view.findViewById(R.id.duration);
//
//            String cutName = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));
//            if (cutName.length() > 30){
//                cutName = cutName.substring(0, 25) + "...";
//            }else {
//                cutName = cutName;
//            }
//            String cutTitle = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE));
//            if (cutTitle.length() > 30){
//                cutTitle = cutTitle.substring(0, 25) + "...";
//            }else {
//                cutTitle = cutTitle;
//            }
//            name.setText(cutName);
//            title.setText(cutTitle);
//            long durationInMs = Long.parseLong(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION)));
//            double durationInMin = ((double) durationInMs / 1000.0) / 60.0;
//            durationInMin = new BigDecimal(Double.toString(durationInMin)).setScale(2, BigDecimal.ROUND_UP).doubleValue();
//            duration.setText("" + durationInMin);
//
//            view.setTag(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)));
//        }

//        @Override
//        public View newView(Context context, Cursor cursor, ViewGroup parent) {
//            LayoutInflater inflater = LayoutInflater.from(context);
//            View v = inflater.inflate(R.layout.list_item, parent, false);
//            bindView(v, context, cursor);
//            return v;
//        }
//    }

//    private View.OnClickListener onButtonClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.play: {
//                    if (player.isPlaying()) {
//                        handler.removeCallbacks(updatePositionRunnable);
//                        player.pause();
//                        playButton.setImageResource(android.R.drawable.ic_media_play);
//                    } else {
//                        if (isStarted) {
//                            player.start();
//                            playButton.setImageResource(android.R.drawable.ic_media_pause);
//                            updatePosition();
//                        } else {
//                            startPlay(currentFile);
//                        }
//                    }
//
//                    break;
//                }
//                case R.id.next: {
//                    int seekto = player.getCurrentPosition() + STEP_VALUE;
//                    if (seekto > player.getDuration())
//                        seekto = player.getDuration();
//                    player.pause();
//                    player.seekTo(seekto);
//                    player.start();
//
//                    break;
//                }
//                case R.id.prev: {
//                    int seekto = player.getCurrentPosition() - STEP_VALUE;
//                    if (seekto < 0)
//                        seekto = 0;
//                    player.pause();
//                    player.seekTo(seekto);
//                    player.start();
//
//                    break;
//                }
//            }
//        }
//    };

//    private MediaPlayer.OnCompletionListener onCompletion = new MediaPlayer.OnCompletionListener() {
//        @Override
//        public void onCompletion(MediaPlayer mp) {
//            stopPlay();
//        }
//    };

//    private MediaPlayer.OnErrorListener onError = new MediaPlayer.OnErrorListener() {
//        @Override
//        public boolean onError(MediaPlayer mp, int what, int extra) {
//            return false;
//        }
//    };

//    private SeekBar.OnSeekBarChangeListener seekBarChange = new SeekBar.OnSeekBarChangeListener() {
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            if (isMovingSeekBar) {
//                player.seekTo(progress);
//            }
//        }
//
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar) {
//            isMovingSeekBar = true;
//        }
//
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar) {
//            isMovingSeekBar = false;
//        }
//    };
}

