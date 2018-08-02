package com.bsettle.tis100clone.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import com.bsettle.tis100clone.R;
import com.bsettle.tis100clone.command.Command;
import com.bsettle.tis100clone.impl.CommandNode;
import com.bsettle.tis100clone.impl.Mode;
import com.bsettle.tis100clone.impl.PortToken;
import com.bsettle.tis100clone.parse.ParserException;
import com.bsettle.tis100clone.state.CommandNodeState;

import java.util.HashMap;
import java.util.logging.Logger;

public class CommandNodeView extends ConstraintLayout implements TextWatcher {
    private CommandNode node;
    private CommandEditorView commandEditor;
    private TextView accText, bakText, modeText, lastText, idleText;
    private Logger logger = Logger.getLogger("NodeView");

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
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.command_node_view, this);

        accText = findViewById(R.id.accText);
        bakText = findViewById(R.id.bakText);
        modeText = findViewById(R.id.modeText);
        lastText = findViewById(R.id.lastText);
        idleText = findViewById(R.id.idleText);
        commandEditor = findViewById(R.id.commandEditor);

        commandEditor.addTextChangedListener(this);
    }

    public void setNode(CommandNode node){
        this.node = node;
        updateAll();
    }


    public CommandNode getNode(){
        return node;
    }

    public void clear(){
        commandEditor.setText("");
        updateAll();
    }

    public void updateAll(){
        CommandNodeState ns =  node.getState();

        int line = node.isRunning() ? ns.getCommandIndex() : -1;

        commandEditor.highlightLine(line);
        commandEditor.setEnabled(!node.isRunning());

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
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        commandEditor.clearErrors();
    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = commandEditor.getText().toString();
        String[] lines = text.split("\n", -1);
        HashMap<Integer, ParserException> errorMap = new HashMap<Integer, ParserException>();
        for (int i = 0; i < lines.length; i++){
            Command c = node.setCommand(i, lines[i]);
            if (c != null && c.getError() != null) {
                errorMap.put(i, c.getError());
            }
        }
        commandEditor.addErrors(errorMap);
    }
}
