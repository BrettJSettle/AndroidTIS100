package com.bsettle.tis100clone.command;

import com.bsettle.tis100clone.impl.CommandNode;

public interface DestinationExpression {
	boolean write(CommandNode node, int val);
	String toString();
}
