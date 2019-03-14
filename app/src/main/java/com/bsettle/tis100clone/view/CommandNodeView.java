package com.bsettle.tis100clone.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsettle.tis100clone.R;
import com.bsettle.tis100clone.impl.CommandNode;
import com.bsettle.tis100clone.impl.Mode;
import com.bsettle.tis100clone.impl.Node;
import com.bsettle.tis100clone.impl.PortToken;
import com.bsettle.tis100clone.parse.ParserException;
import com.bsettle.tis100clone.state.CommandNodeState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandNodeView extends NodeView implements View.OnClickListener{

    private TextView accText, bakText, modeText, lastText, idleText;
    private LinearLayout commandLineLayout;
    private List<CommandLineView> lines;

    private static CommandLineView currentLine;

    public CommandNodeView(Context context) {
        super(context);
        initialize(context);
    }

    public CommandNodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public CommandNodeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        inflate(context, R.layout.command_node_view, this);

        lines = new ArrayList<>();
        commandLineLayout = findViewById(R.id.commandLineLayout);
        for (int i = 0; i < 20; i++){
            addLine();
        }
        accText = findViewById(R.id.accText);
        bakText = findViewById(R.id.bakText);
        modeText = findViewById(R.id.modeText);
        lastText = findViewById(R.id.lastText);
        idleText = findViewById(R.id.idleText);

    }

    private void addLine(){
        CommandLineView lineView = new CommandLineView(this.getContext());
        commandLineLayout.addView(lineView);
        lineView.setText("LINE " + lines.size());
        lineView.setOnClickListener(this);
        lines.add(lineView);

    }


    @Override
    public void setNode(Node node){
        this.node = node;
        update();

    }

    @Override
    public CommandNode getNode(){
        return (CommandNode) node;
    }

    @Override
    public void update() {
        CommandNodeState ns = getNode().getState();

        int line = ns.getCommandIndex();

        accText.setText(String.valueOf(ns.getAccumulator()));
        Integer backup = ns.getBackup();
        bakText.setText(String.format("(%s)", backup));
        Mode mode = ns.getMode();
        modeText.setText(mode.toString());

        PortToken pt = ns.getLastPort();
        String lastString = pt == null ? "NONE" : pt.toString();
        lastText.setText(lastString);
        idleText.setText("0%");
    }

    @Override
    public void setActive(boolean active) {

    }

    public void setHighlighted(boolean h){
        setBackgroundColor(h ? Color.WHITE : Color.BLACK);
    }

    @Override
    public void onClick(View v) {
        if (currentLine != null){
            currentLine.setBackgroundColor(Color.BLACK);
        }
        v.setBackgroundColor(Color.RED);
        currentLine = (CommandLineView) v;
    }
}
