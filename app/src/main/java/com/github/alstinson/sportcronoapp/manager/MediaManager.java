package com.github.alstinson.sportcronoapp.manager;

import android.media.MediaPlayer;

import com.github.alstinson.sportcronoapp.MainActivity;
import com.github.alstinson.sportcronoapp.R;

import java.util.Timer;
import java.util.TimerTask;

public class MediaManager extends ActivityManager<MainActivity> {

    public MediaPlayer mediaPlayer;

    public MediaManager(MainActivity activity) {
        this.activity = activity;
        mediaPlayer = MediaPlayer.create(activity.getBaseContext(), R.raw.beep);
    }

    public void play() {
        mediaPlayer.start();
    }

    public void play(int millisDelay) {
        new Timer().schedule(new DelayPlay(), millisDelay);
    }

    private class DelayPlay extends TimerTask {

        @Override
        public void run() {
            mediaPlayer.start();
        }
    }


}
