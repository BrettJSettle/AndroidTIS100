package com.bsettle.tis100clone.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.bsettle.tis100clone.R;
import com.bsettle.tis100clone.event.ControlButtonListener;

public class ControlView extends LinearLayout implements View.OnClickListener{
    private ImageButton playButton, stepPauseButton, stopButton;
    private boolean running = false;
    private ControlButtonListener handler;

    public ControlView(Context context) {
        super(context);
        initialize(context);
    }

    public ControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public ControlView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_view, this);

        playButton = findViewById(R.id.playButton);
        stepPauseButton = findViewById(R.id.stepButton);
        stopButton = findViewById(R.id.stopButton);

        playButton.setOnClickListener(this);
        stepPauseButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
    }

    private void play(){
        handler.controlButtonClicked(ControlButtonListener.ControlButton.PLAY);
        setRunning(true);
    }

    private void pause(){
        handler.controlButtonClicked(ControlButtonListener.ControlButton.PAUSE);
        setRunning(false);
    }

    private void stop(){
        handler.controlButtonClicked(ControlButtonListener.ControlButton.STOP);
        setRunning(false);
    }

    private void step(){
        handler.controlButtonClicked(ControlButtonListener.ControlButton.STEP);
    }

    private void setRunning(boolean running){
        this.running = running;

        Drawable step_pause = running ? getResources().getDrawable(R.drawable.pause_icon, getResources().newTheme()) :
                getResources().getDrawable(R.drawable.step_icon, getResources().newTheme());
        stepPauseButton.setImageDrawable(step_pause);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(playButton)){
            play();
        }else if (v.equals(stepPauseButton)){
            if (running){
                pause();
            }else {
                step();
            }
        }else if (v.equals(stopButton)){
            stop();
        }
    }

    public void setHandler(ControlButtonListener handler){
        this.handler = handler;
    }

}
