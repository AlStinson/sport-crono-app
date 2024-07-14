package com.github.alstinson.sportcronoapp.state;

public interface OnStateChange {
    default void onInit() {
    }

    default void onReady() {
    }

    default void onRunning() {
    }

    default void onPaused() {
    }
}
