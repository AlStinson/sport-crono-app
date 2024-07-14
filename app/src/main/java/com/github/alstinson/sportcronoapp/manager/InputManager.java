package com.github.alstinson.sportcronoapp.manager;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.github.alstinson.sportcronoapp.MainActivity;
import com.github.alstinson.sportcronoapp.R;
import com.github.alstinson.sportcronoapp.state.OnStateChange;
import com.github.alstinson.sportcronoapp.state.STATE;

public class InputManager
        extends ActivityManager<MainActivity>
        implements OnStateChange {
    EditText repetitions;
    EditText sets;
    CheckBox unlimitedSets;

    public InputManager(MainActivity activity) {
        this.activity = activity;
        repetitions = findViewById(R.id.repetitions);
        sets = findViewById(R.id.sets);
        unlimitedSets = findViewById(R.id.unlimited_sets);

        repetitions.addTextChangedListener(new InputTextWatcher());
        sets.addTextChangedListener(new InputTextWatcher());
        unlimitedSets.setOnCheckedChangeListener(this::onCheckedChanged);
    }

    public int getRepetitions() {
        return getIntValue(repetitions.getText().toString());
    }

    public int getSets() {
        return getIntValue(sets.getText().toString());
    }

    public boolean getUnlimitedSets() {
        return unlimitedSets.isChecked();
    }

    private int getIntValue(String string) {
        try {
            int value = Integer.parseInt(string);
            return Math.max(0, value);
        } catch (Exception e) {
            return 0;
        }
    }

    private void tryChangeState() {
        if (activity.stateManager.state == STATE.INIT && isReady()) {
            activity.stateManager.setReady();
        } else if (activity.stateManager.state == STATE.READY && !isReady()) {
            activity.stateManager.setInit();
        }
    }

    private boolean isReady() {
        return getRepetitions() > 0 && (getSets() > 0 || getUnlimitedSets());
    }

    @Override
    public void onInit() {
        setEnabled(true);
        tryChangeState();
    }

    @Override
    public void onRunning() {
        setEnabled(false);
    }

    private void setEnabled(boolean value) {
        repetitions.setEnabled(value);
        unlimitedSets.setEnabled(value);
        if (!value || !getUnlimitedSets()) sets.setEnabled(value);
    }

    private void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            sets.setText("");
            sets.setEnabled(false);
        } else {
            sets.setEnabled(true);
        }
        tryChangeState();
    }

    class InputTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            tryChangeState();
        }
    }
}
