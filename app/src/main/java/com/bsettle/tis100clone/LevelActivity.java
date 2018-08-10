package com.bsettle.tis100clone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bsettle.tis100clone.event.ControlHandler;
import com.bsettle.tis100clone.impl.CommandNode;
import com.bsettle.tis100clone.impl.InputNode;
import com.bsettle.tis100clone.impl.Node;
import com.bsettle.tis100clone.impl.OutputNode;
import com.bsettle.tis100clone.level.LevelInfo;
import com.bsettle.tis100clone.level.LevelTileInfo;
import com.bsettle.tis100clone.state.GameState;
import com.bsettle.tis100clone.view.BidirectionalPortView;
import com.bsettle.tis100clone.view.CommandEditorView;
import com.bsettle.tis100clone.view.ControlView;
import com.bsettle.tis100clone.view.CommandNodeView;
import com.bsettle.tis100clone.view.IOPortView;
import com.bsettle.tis100clone.view.PortView;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LevelActivity extends AppCompatActivity implements ControlHandler{

    private static Logger logger = Logger.getLogger("GridActivity");
    private GridLayout gridLayout;
    private CommandNodeView[][] nodeViewGrid;
    private Vector<PortView> portViews;
    private GameState gameState;

    private boolean playing = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        gridLayout = findViewById(R.id.gridLayout);
        ControlView controlView = findViewById(R.id.buttonView);
        controlView.setHandler(this);

        Intent intent = getIntent();
        LevelTileInfo level = (LevelTileInfo) intent.getSerializableExtra("level");
        loadLevel(level);
    }

    private void loadLevel(LevelTileInfo item){
        AssetManager manager = getAssets();
        try {
            InputStream is = manager.open(item.getName() + ".json");
            LevelInfo info = LevelInfo.fromFile(item, is);
            gameState = new GameState(info);
            addNodeViews();
            addPortViews();
            addIOViews();
        }catch(IOException e){
            logger.info("Failed to load level " + item.getName());
        }
    }

    private void addNodeViews(){
        LevelInfo info = gameState.getLevelInfo();
        int rowNum = info.getRows();
        int colNum = info.getColumns();

        nodeViewGrid = new CommandNodeView[rowNum][colNum];

        gridLayout.setRowCount(2 * rowNum + 1);
        gridLayout.setColumnCount(2 * colNum - 1);

        for (int row = 0; row < rowNum; row++){
            for (int col = 0; col < colNum; col++){
                CommandNodeView nv = new CommandNodeView(this);
                if (gameState.getNode(row, col) instanceof CommandNode){
                    nv.setNode((CommandNode) gameState.getNode(row, col));
                }
                nodeViewGrid[row][col] = nv;
                addView(nv, (row * 2) + 1, col * 2);
            }
        }
    }

    private void addView(View view, int row, int col){
        GridLayout.Spec rowSpan = GridLayout.spec(row, GridLayout.CENTER);
        GridLayout.Spec colspan = GridLayout.spec(col, GridLayout.CENTER);

        GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridLayout.addView(view, gridParam);
    }

    private void addPortViews() {
        portViews = new Vector<>();
        LevelInfo info = gameState.getLevelInfo();
        int rowNum = info.getRows();
        int colNum = info.getColumns();

        // add port views between nodes
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                if (col < colNum - 1) {
                    PortView vpv = new BidirectionalPortView(this, LinearLayout.VERTICAL, gameState.getNode(row, col), gameState.getNode(row, col + 1));
                    addView(vpv, 2 * row + 1, 2 * col + 1);
                    portViews.add(vpv);
                }
                if (row < rowNum - 1) {
                    PortView hpv = new BidirectionalPortView(this, LinearLayout.HORIZONTAL, gameState.getNode(row, col), gameState.getNode(row + 1, col));
                    addView(hpv, 2 * row + 2, 2 * col);
                    portViews.add(hpv);
                }
            }
        }
    }

    public void addIOViews(){
        if (portViews == null){
            portViews = new Vector<>();
        }

        char name = 'A';
        for (Map.Entry<Integer, InputNode> entry : gameState.getInputNodes().entrySet()){
            int column = entry.getKey();
            InputNode inputNode = entry.getValue();
            IOPortView inputPortView = new IOPortView(this, String.valueOf(name), inputNode, gameState.getNode(0, column));
            inputPortView.setData(inputNode.iter());
            addView(inputPortView, 0, column * 2);
            portViews.add(inputPortView);
            name++;
        }

        int lastRow = gameState.getLevelInfo().getRows()-1;
        name = 'A';
        for (Map.Entry<Integer, OutputNode> entry : gameState.getOutputNodes().entrySet()){
            int column = entry.getKey();
            OutputNode outputNode = entry.getValue();
            IOPortView outputPortView = new IOPortView(this, String.valueOf(name), gameState.getNode(lastRow, column), outputNode);
            outputPortView.setData(outputNode.iter());
            addView(outputPortView, gridLayout.getRowCount()-1, column * 2);
            portViews.add(outputPortView);
            name++;
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            gridLayout.getFocusedChild().clearFocus();
        }
    }


    private void step(){

        if (!gameState.isRunning()){
            gameState.activate();

            if (getCurrentFocus() != null) {
                getCurrentFocus().clearFocus();
            }
        }else {
            gameState.step();
        }

        for (CommandNodeView[] row : nodeViewGrid){
            for (CommandNodeView nodeView : row){
                nodeView.updateAll();
            }
        }


        for (PortView pv : portViews){
            pv.update();
        }
    }

    private void play(){
        if (playing){
            return;
        }
        playing = true;

        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!playing){
                        break;
                    }
                    step();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        };
        r.run();
    }

    private void askReset(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                switch (choice) {
                    case DialogInterface.BUTTON_POSITIVE:
                        reset();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Reset the program?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void stop(){
        if (!gameState.isRunning()){
            askReset();
            return;
        }

        playing = false;
        gameState.reset();

        for (CommandNodeView[] row : nodeViewGrid){
            for (CommandNodeView node : row){
                node.updateAll();
            }
        }
        for (PortView pv : portViews){
            pv.update();
        }

    }

    public void reset(){
        gameState.clear();

        for (CommandNodeView[] row : nodeViewGrid){
            for (CommandNodeView node : row){
                node.clear();
            }
        }

        for (PortView pv : portViews){
            pv.update();
        }
    }

    @Override
    public void controlButtonClicked(ControlButton button) {
        switch(button){
            case STOP:
                stop();
                break;
            case PAUSE:
                playing = false;
                break;
            case PLAY:
                play();
                break;
            case STEP:
                step();
                break;
        }
    }
}
