package com.bsettle.tis100clone.impl;

import com.bsettle.tis100clone.command.Command;
import com.bsettle.tis100clone.command.Expression;
import com.bsettle.tis100clone.state.CommandNodeState;
import com.bsettle.tis100clone.state.NodeState;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


public class StackNode extends Node {
    private Stack<Integer> stack;

    StackNode(NodeState state) {
        super(state);
        stack = new Stack<>();
    }

    @Override
    public void writeFinished(PortToken direction) {
        super.writeFinished(direction);
        stack.pop();
    }

    @Override
    public HashMap<String, Object> getDiff() {
        if (diff == null) {
            diff = new HashMap<>();
            for (PortToken p : new PortToken[]{PortToken.UP, PortToken.LEFT, PortToken.RIGHT, PortToken.DOWN}) {
                Integer in = tryRead(p);
                if (in != null) {
                    stack.push(in);
                }
            }

            if (stack.size() > 0){
                diff.put(NodeState.WRITING_PORT, PortToken.ANY);
                diff.put(NodeState.WRITING_VALUE, stack.peek());
            }
        }
        return diff;
    }
}