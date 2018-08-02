package com.bsettle.tis100clone.command;


import android.util.SparseArray;

import com.bsettle.tis100clone.impl.CommandNode;
import com.bsettle.tis100clone.impl.Mode;
import com.bsettle.tis100clone.impl.PortToken;
import com.bsettle.tis100clone.impl.RegisterToken;
import com.bsettle.tis100clone.parse.Token;
import com.bsettle.tis100clone.state.CommandNodeState;

import java.util.HashMap;

public class MoveInstruction extends InstructionExpression {

	private SourceExpression source;
	private DestinationExpression destination;

	public MoveInstruction(Token instruction, SourceExpression src, DestinationExpression dst) {
		super(instruction);
		this.source = src;
		this.destination = dst;
	}

	@Override
	public String toString() {
		return "Move " + source.toString() + " to " + destination.toString();
	}

	@Override
	public void getDiff(CommandNode node) {
	    Integer num;

	    if (node.getState().getWritingPort() == null) {
            num = resolveSource(node, source);
            if (num == null) {
                return;
            }
        }else{
	        num = node.getState().getWritingValue();
        }

        resolveDestination(node, destination, num);
	}

	public SourceExpression getSource() {
		return source;
	}

	public DestinationExpression getDestination() {
		return destination;
	}
}
