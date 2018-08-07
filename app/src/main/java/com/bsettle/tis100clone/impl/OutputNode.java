package com.bsettle.tis100clone.impl;

import com.bsettle.tis100clone.state.CommandNodeState;
import com.bsettle.tis100clone.state.NodeState;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class OutputNode extends CommandNode {
    private int outputLine = -1;
    private Vector<Integer> expected;
    private Integer outputValue = null;

    public OutputNode(Vector<Integer> expected){
        super();
        this.expected = expected;
        setCommand(0, "MOV UP ACC");
    }
    @Override
    public HashMap<String, Object> getDiff() {
        if (!isRunning()){
            return null;
        }
        diff = super.getDiff();
        return diff;
    }

    @Override
    public void push() {
        if (diff.containsKey(CommandNodeState.MODE) && diff.get(CommandNodeState.MODE).equals(Mode.RUN)){
            outputLine++;
            outputValue = getState().getAccumulator();
        }
        super.push();
    }

    @Override
    public void activate() {
        super.activate();
        outputLine = 0;
    }

    @Override
    public void reset() {
        super.reset();
        outputLine = -1;
        commit(NodeState.WRITING_PORT, null);
        push();
    }

    public int getOutputLine(){
        return outputLine;
    }

    public Iterator<Integer> iter() {
        return expected.iterator();
    }

    public int getExpectedValue(){
        return expected.get(outputLine);
    }
}
