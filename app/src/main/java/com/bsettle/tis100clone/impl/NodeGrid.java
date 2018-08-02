package com.bsettle.tis100clone.impl;

import java.util.Collections;
import java.util.Scanner;
import java.util.Vector;

public class NodeGrid extends NodeCollection{
	private Node[][] grid;
	private int rows, columns;

	public NodeGrid(int rows, int columns) {
		grid = new Node[rows][columns];
        this.rows = rows;
        this.columns = columns;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				grid[i][j] = new CommandNode();
				if (i > 0){
                    Node.connectNodes(grid[i][j], PortToken.UP, grid[i-1][j]);
                }if (j > 0){
                    Node.connectNodes(grid[i][j], PortToken.LEFT, grid[i][j-1]);
                }
			}
		}
	}

	public Iterable<Node> nodeIterator(){
        Vector<Node> nodeList = new Vector<Node>();
        for (int i = 0; i < rows; i++){
            Collections.addAll(nodeList, grid[i]);
        }
        return nodeList;
    }

	public Node getNode(int row, int column) {
		return grid[row][column];
	}

	public int getRowCount() {
		return rows;
	}
	
	public int getColumnCount() {
		return columns;
	}

    public void setNode(int r, int c, Node n) {
    	grid[r][c] = n;
    	if (r > 0){
    		Node.connectNodes(grid[r-1][c], PortToken.DOWN, grid[r][c]);
		}
		if (c > 0){
    		Node.connectNodes(grid[r][c-1], PortToken.RIGHT, grid[r][c]);
		}
	}
}
