package com.bsettle.tis100clone.impl;

public abstract class NodeCollection {



	public abstract Iterable<Node> nodeIterator();

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Node n : nodeIterator()) {
			builder.append(n.toString());
			builder.append("\n");
		}
		return builder.toString();

	}

}
