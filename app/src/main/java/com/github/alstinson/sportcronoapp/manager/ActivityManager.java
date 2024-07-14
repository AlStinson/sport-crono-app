package com.github.alstinson.sportcronoapp.manager;

import android.app.Activity;
import android.view.View;

import androidx.annotation.IdRes;

public abstract class ActivityManager<A extends Activity> {
    protected A activity;

    public <T extends View> T findViewById(@IdRes int id) {
        return activity.findViewById(id);
    }
}
