package com.bsettle.tis100clone.command;


import com.bsettle.tis100clone.impl.CommandNode;

public class NumberExpression extends ArgumentExpression implements SourceExpression {
	private int value;

	public NumberExpression(String value) {
		super(value);
		this.value = Integer.parseInt(value);
	}

	public Integer read(CommandNode node){
		return value;
	}

}
