package com.bsettle.tis100clone.event;

public interface ControlHandler {
    enum ControlButton {
        PLAY, PAUSE, STEP, STOP, RESET
    };
    void controlButtonClicked(ControlButton button);
}
