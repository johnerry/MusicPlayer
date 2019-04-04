package com.example.johnfash.musicplayer;

import android.media.MediaPlayer;
import java.io.IOException;

public class AudioPlay {
    public static MediaPlayer player = new MediaPlayer();
    public static MediaPlayer getPlayer() {
        return player;
    }
    public static void stopAudio() {
        player.stop();
        player.reset();
    }

    public static void releaseAudio() {
        player.release();
    }

    public static boolean isPlaying() {
        return player.isPlaying();
    }

    public static void pauseAudio() {
        player.pause();
    }

    public static void startAudio() {
        player.start();
    }

    public static void setDataSource(String string) {
        try {
            player.setDataSource(string);
            player.prepare();
            player.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getMediaDuration() {
        return player.getDuration();
    }

    public static int getMediaCurrentPosition() {
        return player.getCurrentPosition();
    }

    public static void seekTo(int u) {
        player.seekTo(u);
    }
}
