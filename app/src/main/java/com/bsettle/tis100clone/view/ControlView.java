package com.bsettle.tis100clone.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.bsettle.tis100clone.R;
import com.bsettle.tis100clone.event.ControlHandler;

public class ControlView extends LinearLayout implements View.OnClickListener{
    private ImageButton playPauseButton, stepButton, stopButton;
    private boolean running = false;
    private ControlHandler handler;

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

        playPauseButton = findViewById(R.id.playButton);
        stepButton = findViewById(R.id.stepButton);
        stopButton = findViewById(R.id.stopButton);

        playPauseButton.setOnClickListener(this);
        stepButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
    }

    private void play(){
        handler.controlButtonClicked(ControlHandler.ControlButton.PLAY);
        setRunning(true);
    }

    private void pause(){
        handler.controlButtonClicked(ControlHandler.ControlButton.PAUSE);
        setRunning(false);
    }

    private void stop(){
        handler.controlButtonClicked(ControlHandler.ControlButton.STOP);
        setRunning(false);
    }
    private void reset(){
        handler.controlButtonClicked(ControlHandler.ControlButton.RESET);
    }

    private void step(){
        handler.controlButtonClicked(ControlHandler.ControlButton.STEP);
        setRunning(true);
    }

    private void setRunning(boolean running){
        this.running = running;
        Drawable play_pause = running ? getResources().getDrawable(R.drawable.pause_icon, getResources().newTheme()) :
                getResources().getDrawable(R.drawable.play_icon, getResources().newTheme());
        playPauseButton.setImageDrawable(play_pause);

        Drawable stop_reset = running ? getResources().getDrawable(R.drawable.stop_icon, getResources().newTheme()) :
                getResources().getDrawable(R.drawable.reset_icon, getResources().newTheme());
        stopButton.setImageDrawable(stop_reset);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(playPauseButton)){
            if (running){
                pause();
            }else{
                play();
            }
        }else if (v.equals(stepButton)){
            step();
        }else if (v.equals(stopButton)){
            if (running){
                stop();
            }else{
                reset();
            }
        }
    }

    public void setHandler(ControlHandler handler){
        this.handler = handler;
    }

}
