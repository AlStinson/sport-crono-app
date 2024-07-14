package com.github.alstinson.sportcronoapp.manager;

import com.github.alstinson.sportcronoapp.MainActivity;
import com.github.alstinson.sportcronoapp.state.OnStateChange;
import com.github.alstinson.sportcronoapp.state.STATE;
import com.github.alstinson.sportcronoapp.update.OnUpdate;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Consumer;

public class StateManager
        extends ActivityManager<MainActivity>
        implements OnUpdate {

    public STATE state;

    public Queue<Runnable> nextStateChanges = new LinkedList<>();

    public StateManager(MainActivity activity) {
        this.activity = activity;
    }

    public void setInit() {
        nextStateChanges.add(() -> applyStateChange(STATE.INIT, OnStateChange::onInit));
    }

    public void setReady() {
        nextStateChanges.add(() -> applyStateChange(STATE.READY, OnStateChange::onReady));
    }

    public void setRunning() {
        nextStateChanges.add(() -> applyStateChange(STATE.RUNNING, OnStateChange::onRunning));
    }

    public void setPaused() {
        nextStateChanges.add(() -> applyStateChange(STATE.PAUSED, OnStateChange::onPaused));
    }

    public boolean isState(STATE state) {
        return this.state == state;
    }

    private void applyStateChange(STATE state, Consumer<OnStateChange> notify) {
        System.out.printf("STATE changed from {%s} to {%s}\n", this.state, state);
        this.state = state;
        activity.getFieldsValues(OnStateChange.class).forEach(notify);
    }

    @Override
    public void update(double delta) {
        while (!nextStateChanges.isEmpty()) {
            Optional.ofNullable(nextStateChanges.poll()).ifPresent(Runnable::run);
        }
    }

}
