package com.github.alstinson.sportcronoapp.manager;

import static com.github.alstinson.sportcronoapp.state.STATE.PAUSED;
import static com.github.alstinson.sportcronoapp.state.STATE.RUNNING;

import android.view.View;
import android.widget.Button;

import com.github.alstinson.sportcronoapp.MainActivity;
import com.github.alstinson.sportcronoapp.R;
import com.github.alstinson.sportcronoapp.state.OnStateChange;

public class ButtonsManager
        extends ActivityManager<MainActivity>
        implements OnStateChange {
    public Button buttonStart;
    public Button buttonPause;
    public Button buttonStop;

    public ButtonsManager(MainActivity activity) {
        this.activity = activity;

        buttonStart = findViewById(R.id.button_start);
        buttonPause = findViewById(R.id.button_pause);
        buttonStop = findViewById(R.id.button_stop);

        buttonStart.setOnClickListener(this::onStartClick);
        buttonPause.setOnClickListener(this::onPauseClick);
        buttonStop.setOnClickListener(this::onStopClick);
    }

    private void onStartClick(View view) {
        activity.stateManager.setRunning();
    }

    private void onPauseClick(View view) {
        if (activity.stateManager.isState(PAUSED)) activity.stateManager.setRunning();
        else if (activity.stateManager.isState(RUNNING)) activity.stateManager.setPaused();
    }

    private void onStopClick(View view) {
        activity.stateManager.setInit();
    }

    private void setButtonsEnabled(boolean start, boolean pause, boolean stop) {
        buttonStart.setEnabled(start);
        buttonPause.setEnabled(pause);
        buttonStop.setEnabled(stop);
    }

    @Override
    public void onInit() {
        setButtonsEnabled(false, false, false);
    }

    @Override
    public void onReady() {
        setButtonsEnabled(true, false, false);
    }

    @Override
    public void onRunning() {
        setButtonsEnabled(false, true, true);
    }

    @Override
    public void onPaused() {
        setButtonsEnabled(false, true, true);
    }
}
