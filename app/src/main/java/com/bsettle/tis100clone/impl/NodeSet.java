package com.bsettle.tis100clone.impl;

import java.util.Vector;

public class NodeSet extends NodeCollection {
	private Vector<Node> nodes = new Vector<Node>();

	@Override
	public Iterable<Node> nodeIterator() {
		return nodes;
	}
	
	public NodeSet(Node...nodes) {
		for (Node n : nodes) {
			this.nodes.add(n);
		}
	}

}
