package com.bsettle.tis100clone.impl;

import com.bsettle.tis100clone.state.NodeState;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class InputNode extends Node {
    private int inputLine = -1;
    private Vector<Integer> input;

    public InputNode(Vector<Integer> input){
        super(new NodeState());
        this.input = input;
    }

    @Override
    public HashMap<String, Object> getDiff() {
        if (!isRunning()){
            return null;
        }
        diff = new HashMap<>();
        if (inputLine < input.size()) {
            diff.put(NodeState.WRITING_PORT, PortToken.DOWN);
            diff.put(NodeState.WRITING_VALUE, input.get(inputLine));
        }
        return diff;
    }

    @Override
    public void writeFinished(PortToken direction) {
        super.writeFinished(direction);
        inputLine++;
    }

    @Override
    public void activate() {
        super.activate();
        inputLine = 0;
    }

    @Override
    public void reset() {
        super.reset();
        inputLine = -1;
        commit(NodeState.WRITING_PORT, null);
        push();
    }

    public int getInputLine(){
        return inputLine;
    }

    public Iterator<Integer> iter(){
        return input.iterator();
    }
}
