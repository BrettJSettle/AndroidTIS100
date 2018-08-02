package com.bsettle.tis100clone.event;

public interface ControlHandler {
    public static enum ControlButton {
        PLAY, PAUSE, STEP, STOP, RESET
    };
    void controlButtonClicked(ControlButton button);
}
