package com.bsettle.tis100clone.command;


import android.util.SparseArray;

import com.bsettle.tis100clone.impl.CommandNode;
import com.bsettle.tis100clone.impl.Mode;
import com.bsettle.tis100clone.parse.Token;
import com.bsettle.tis100clone.state.CommandNodeState;

import java.util.HashMap;

public class JumpRelativeInstruction extends InstructionExpression {

	private SourceExpression source;
	
	public JumpRelativeInstruction(Token lookahead, SourceExpression source) {
		super(lookahead);
		this.source = source;
	}

	@Override
	public String toString() {
		return "Jump relative " + source.toString();
	}

	@Override
	public void getDiff(CommandNode node) {
		CommandNodeState s = node.getState();

		Integer ro = resolveSource(node, source);
		if (ro == null){
		    return;
        }
		int n = s.getCommandIndex() + ro;
        node.commit(CommandNodeState.COMMAND_INDEX, n);
		node.commit(CommandNodeState.MODE, Mode.RUN);
	}

}
