package com.bsettle.tis100clone.impl;

import com.bsettle.tis100clone.state.NodeState;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;


public class StackNode extends Node {
    private Stack<Integer> stack;

    public StackNode() {
        super(new NodeState());
        stack = new Stack<>();
    }

    @Override
    public void writeFinished(PortToken direction) {
        super.writeFinished(direction);
        stack.pop();
    }

    public final Iterator<Integer> iter(){
        return stack.iterator();
    }

    @Override
    public HashMap<String, Object> getDiff() {
        if (diff == null) {
            diff = new HashMap<>();
            if (stack.size() < Node.MAX_LINES) {
                for (PortToken p : new PortToken[]{PortToken.UP, PortToken.LEFT, PortToken.RIGHT, PortToken.DOWN}) {
                    Integer in = tryRead(p);
                    if (in != null) {
                        stack.push(in);
                    }
                }
            }

            if (stack.size() > 0){
                diff.put(NodeState.WRITING_PORT, PortToken.ANY);
                diff.put(NodeState.WRITING_VALUE, stack.peek());
            }
        }
        return diff;
    }

    @Override
    public void deactivate() {
        stack.clear();
        commit(NodeState.READING_PORT, null);
        commit(NodeState.WRITING_PORT, null);
        commit(NodeState.WRITING_VALUE, null);
    }

    @Override
    public void activate() {

    }
}
