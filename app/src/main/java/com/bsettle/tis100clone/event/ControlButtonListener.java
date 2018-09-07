package com.bsettle.tis100clone.event;

public interface ControlButtonListener {
    enum ControlButton {
        PLAY, PAUSE, STEP, STOP, RESET
    };
    void controlButtonClicked(ControlButton button);
}
