package com.github.alstinson.sportcronoapp.manager;

import static java.lang.String.format;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.alstinson.sportcronoapp.MainActivity;
import com.github.alstinson.sportcronoapp.R;
import com.github.alstinson.sportcronoapp.state.OnStateChange;
import com.github.alstinson.sportcronoapp.state.STATE;
import com.github.alstinson.sportcronoapp.update.OnUpdate;

import java.util.Locale;
import java.util.Timer;

public class CurrentValuesManager
        extends ActivityManager<MainActivity>
        implements OnUpdate, OnStateChange {

    public static final String SECONDS_LEFT_FORMAT = "%.2f s.";
    public static final String STATS_FORMAT = "";

    public LinearLayout currentValuesGroup;
    public ProgressBar progressBar;
    public TextView secondsLeft;
    public TextView stats;

    private double time;
    private int repetition;
    private int set;


    public CurrentValuesManager(MainActivity activity) {
        this.activity = activity;

        this.currentValuesGroup = findViewById(R.id.current_values_group);
        this.progressBar = findViewById(R.id.progressBar);
        this.secondsLeft = findViewById(R.id.seconds_left);
        this.stats = findViewById(R.id.stats);

    }

    @Override
    public void update(double delta) {
        if (activity.stateManager.state != STATE.RUNNING) return;
        time += delta;
        int intValue = (int) time;
        if (intValue >= progressBar.getMax()) {
            time -= progressBar.getMax();
            activity.mediaManager.play();
            repetition++;
            if (repetition > activity.inputManager.getRepetitions()) {
                activity.mediaManager.play(500);
                repetition = 1;
                set++;
                if (set > activity.inputManager.getSets() && !activity.inputManager.getUnlimitedSets()) {
                    activity.mediaManager.play(1000);
                    activity.stateManager.setInit();
                }
            }
            initializeProgressBar();
            stats.setText(getStatsText());
        }
        secondsLeft.setText(format(Locale.getDefault(), SECONDS_LEFT_FORMAT, time));
        progressBar.setProgress(intValue);
    }

    private String getStatsText() {
        return new StringBuilder()
                .append("Set: ").append(set).append("\n")
                .append("Repetition: ").append(repetition).append("\n")
                .append("Time: ").append(getRepetitionTime()).append(" seconds")
                .toString();
    }

    private int getRepetitionTime() {
        return 10 * repetition;
    }

    private void initializeProgressBar() {
        progressBar.setMax(getRepetitionTime());
        progressBar.setProgress(0);
    }

    @Override
    public void onInit() {
        time = 0;
        repetition = 1;
        set = 1;
        currentValuesGroup.setVisibility(View.INVISIBLE);
        initializeProgressBar();
    }

    @Override
    public void onRunning() {
        currentValuesGroup.setVisibility(View.VISIBLE);
        secondsLeft.setText(format(Locale.getDefault(), SECONDS_LEFT_FORMAT, time));
        stats.setText(getStatsText());
        progressBar.setProgress((int) time);
    }
}
