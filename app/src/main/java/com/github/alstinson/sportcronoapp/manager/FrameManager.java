package com.github.alstinson.sportcronoapp.manager;

import android.view.Choreographer;

import com.github.alstinson.sportcronoapp.MainActivity;
import com.github.alstinson.sportcronoapp.update.OnUpdate;

import java.util.Calendar;

public class FrameManager extends ActivityManager<MainActivity> {

    private static final double MILIS_TO_SECONDS_CONVERSION = 1_000;

    public Choreographer choreographer = Choreographer.getInstance();
    public long lastFrameTimeMillis = now();


    public FrameManager(MainActivity activity) {
        this.activity = activity;
        choreographer.postFrameCallback(this::update);
    }

    public void update(Long __) {
        long currentFrameTimeMillis = now();
        double delta = (currentFrameTimeMillis - lastFrameTimeMillis) / MILIS_TO_SECONDS_CONVERSION;
        activity.getFieldsValues(OnUpdate.class).forEach(manager -> manager.update(delta));
        choreographer.postFrameCallback(this::update);
        lastFrameTimeMillis = currentFrameTimeMillis;
    }

    private long now() {
        return Calendar.getInstance().getTime().getTime();
    }

}
