package com.bsettle.tis100clone.impl;

import com.bsettle.tis100clone.state.NodeState;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class OutputNode extends Node {
    private int outputLine = 0;
    private Vector<Integer> expected;
    private Integer outputValue = null;

    public OutputNode(Vector<Integer> expected){
        super(new NodeState());
        this.expected = expected;
    }
    @Override
    public HashMap<String, Object> getDiff() {
        if (!isRunning()){
            return null;
        }
        diff = new HashMap<>();
        outputValue = null;
        Integer n = tryRead(PortToken.UP);
        if (n != null){
            outputLine++;
            outputValue = n;
        }
        return diff;
    }

    @Override
    public void reset() {
        super.reset();
        outputLine = 0;
        commit(NodeState.WRITING_PORT, null);
        push();
    }

    public int getOutputLine(){
        return outputLine;
    }

    public int getExpectedValue(){
        return expected.get(outputLine);
    }
}
