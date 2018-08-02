package com.bsettle.tis100clone.impl;

import com.bsettle.tis100clone.state.NodeState;

import java.util.HashMap;
import java.util.Vector;

public class InputNode extends Node {
    private int inputLine = 0;
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
    public void reset() {
        super.reset();
        inputLine = 0;
        commit(NodeState.WRITING_PORT, null);
        push();
    }
}
