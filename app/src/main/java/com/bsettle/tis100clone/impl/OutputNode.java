package com.bsettle.tis100clone.impl;

import com.bsettle.tis100clone.state.CommandNodeState;
import com.bsettle.tis100clone.state.NodeState;
import java.util.Iterator;
import java.util.Vector;

public class OutputNode extends CommandNode {
    private int outputLine = -1;
    private Vector<Integer> expected;
    private Vector<Integer> output;

    public OutputNode(Vector<Integer> expected){
        super();
        this.expected = expected;
        output = new Vector<>();
        setCommand(0, "MOV UP NIL");
    }

    @Override
    public void push() {
        if (diff.containsKey(CommandNodeState.MODE) && diff.get(CommandNodeState.MODE).equals(Mode.RUN)){
            Node inputNode = neighbors.get(PortToken.UP);
            Integer val = inputNode.getState().getWritingValue();
            output.add(val);
        }
        super.push();
    }

    @Override
    public void activate() {
        super.activate();
        outputLine = 0;
    }

    @Override
    public void deactivate() {
        outputLine = -1;
        output = new Vector<>();
        commit(NodeState.READING_PORT, null);
        push();
    }

    public Integer getOutputValue() {
        return outputLine >= 0 && outputLine < output.size() ? output.get(outputLine) : null;
    }

    public Integer getOutputLine(){
        return outputLine;
    }

    public int nextLine() {
        return ++outputLine;
    }

    public Iterator<Integer> iter() {
        return expected.iterator();
    }

    public Integer getExpectedValue(){
        return outputLine >= 0 && outputLine < expected.size() ? expected.get(outputLine) : null;
    }

    public Boolean checkOutput(){
        if (outputLine < 0 || outputLine >= output.size() || outputLine >= expected.size()){
            return null;
        }
        return output.get(outputLine).equals(expected.get(outputLine));
    }
}
