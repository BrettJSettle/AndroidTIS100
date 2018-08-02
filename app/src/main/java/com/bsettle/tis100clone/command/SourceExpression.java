package com.bsettle.tis100clone.command;

import com.bsettle.tis100clone.impl.CommandNode;

public interface SourceExpression {
	String toString();
	Integer read(CommandNode n);
}
