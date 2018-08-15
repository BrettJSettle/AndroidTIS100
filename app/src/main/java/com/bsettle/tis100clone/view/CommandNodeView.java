package com.bsettle.tis100clone.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.bsettle.tis100clone.R;
import com.bsettle.tis100clone.command.Command;
import com.bsettle.tis100clone.impl.CommandNode;
import com.bsettle.tis100clone.impl.Mode;
import com.bsettle.tis100clone.impl.Node;
import com.bsettle.tis100clone.impl.PortToken;
import com.bsettle.tis100clone.parse.ParserException;
import com.bsettle.tis100clone.state.CommandNodeState;

import java.util.HashMap;

public class CommandNodeView extends NodeView implements TextWatcher{
    private CommandEditorView commandEditor;
    private TextView accText, bakText, modeText, lastText, idleText;

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

    public CommandEditorView getCommandEditor(){
        return commandEditor;
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
    public void update(){
        CommandNodeState ns =  getNode().getState();

        int line = ns.getCommandIndex();
        commandEditor.highlightLine(line);

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

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = commandEditor.getText().toString();
        String[] lines = text.split("\n", -1);
        HashMap<Integer, ParserException> errorMap = new HashMap<>();
        for (int i = 0; i < lines.length; i++){
            Command c = getNode().setCommand(i, lines[i]);
            System.out.println(c);
            if (c != null && c.getError() != null) {
                errorMap.put(i, c.getError());
            }
        }
        commandEditor.setErrorSpans(errorMap);
    }

    @Override
    public void setActive(boolean active) {
        commandEditor.setEnabled(!active);
    }
}
