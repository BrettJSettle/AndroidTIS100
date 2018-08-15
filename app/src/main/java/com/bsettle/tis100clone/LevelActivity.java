package com.bsettle.tis100clone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bsettle.tis100clone.event.ControlHandler;
import com.bsettle.tis100clone.impl.CommandNode;
import com.bsettle.tis100clone.impl.InputNode;
import com.bsettle.tis100clone.impl.Node;
import com.bsettle.tis100clone.impl.OutputNode;
import com.bsettle.tis100clone.impl.StackNode;
import com.bsettle.tis100clone.level.LevelInfo;
import com.bsettle.tis100clone.level.LevelTileInfo;
import com.bsettle.tis100clone.view.NodeView;
import com.bsettle.tis100clone.state.GameState;
import com.bsettle.tis100clone.view.BidirectionalPortView;
import com.bsettle.tis100clone.view.CommandNodeView;
import com.bsettle.tis100clone.view.ControlView;
import com.bsettle.tis100clone.view.IOPortView;
import com.bsettle.tis100clone.view.NodeFrame;
import com.bsettle.tis100clone.view.PortView;
import com.bsettle.tis100clone.view.StackNodeView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import java.util.logging.Logger;


public class LevelActivity extends AppCompatActivity implements ControlHandler{

    private static Logger logger = Logger.getLogger("GridActivity");
    private GridLayout gridLayout;
    private NodeView[][] nodeViewGrid;
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

        nodeViewGrid = new NodeView[rowNum][colNum];

        gridLayout.setRowCount(2 * rowNum + 1);
        gridLayout.setColumnCount(2 * colNum - 1);

        for (int row = 0; row < rowNum; row++){
            for (int col = 0; col < colNum; col++){
                NodeFrame nf = new NodeFrame(this);
                Node n = gameState.getNode(row, col);

                if (n instanceof CommandNode){
                    CommandNodeView nv = new CommandNodeView(this);
                    nodeViewGrid[row][col] = nv;
                    nv.setNode(n);
                    nf.addView(nv);
                }else if (n instanceof StackNode){
                    StackNodeView nv = new StackNodeView(this);
                    nodeViewGrid[row][col] = nv;
                    nv.setNode(n);
                    nf.addView(nv);
                }
                addView(nf, (row * 2) + 1, col * 2);
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

    private void addIOViews(){
        if (portViews == null){
            portViews = new Vector<>();
        }

        char in_name = 'A', out_name = 'A';
        int lastRow = gameState.getLevelInfo().getRows()-1;

        for (int col = 0; col < gameState.getLevelInfo().getColumns(); col++){
            InputNode in_node = gameState.getInputNode(col);
            if (in_node != null){
                IOPortView inputPortView = new IOPortView(this, String.valueOf(in_name), in_node, gameState.getNode(0, col));
                inputPortView.setData(in_node.iter());
                addView(inputPortView, 0, col * 2);
                portViews.add(inputPortView);
                in_name++;
            }

            OutputNode out_node = gameState.getOutputNode(col);
            if (out_node != null){
                IOPortView outputPortView = new IOPortView(this, String.valueOf(out_name), gameState.getNode(lastRow, col), out_node);
                outputPortView.setData(out_node.iter());
                addView(outputPortView, gridLayout.getRowCount()-1, col * 2);
                portViews.add(outputPortView);
                out_name++;
            }
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
    private void updateViews(){
        for (NodeView[] row : nodeViewGrid){
            for (NodeView nv : row) {
                nv.setActive(gameState.isRunning());
                nv.update();
            }
        }

        for (PortView pv : portViews){
            pv.update();
        }
    }


    private void step(){
        if (getCurrentFocus() != null) {
            getCurrentFocus().clearFocus();
        }

        gameState.step();
        updateViews();
    }

    private void play(){
        if (playing){
            return;
        }
        playing = true;


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!playing){
                        break;
                    }
                    LevelActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            step();
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }).start();
    }

    private void askErase(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                switch (choice) {
                    case DialogInterface.BUTTON_POSITIVE:
                        erase();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Erase the program?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void deactivate(){
        if (!gameState.isRunning()){
            askErase();
            return;
        }

        playing = false;
        gameState.deactivate();
        updateViews();

    }

    private void erase(){
        gameState.erase();
        updateViews();
    }

    @Override
    public void controlButtonClicked(ControlButton button) {
        switch(button){
            case STOP:
                deactivate();
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
