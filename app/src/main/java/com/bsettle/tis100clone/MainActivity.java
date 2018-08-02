package com.bsettle.tis100clone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bsettle.tis100clone.level.LevelTileInfo;

public class MainActivity extends AppCompatActivity implements OnClickListener{
    private FloatingActionButton fab;
    private Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.equals(fab)){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }else if (v.equals(playButton)){
            Intent levelSelectIntent = new Intent(this, LevelSelectActivity.class);
            startActivity(levelSelectIntent);
        }
    }
}
