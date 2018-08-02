package com.bsettle.tis100clone.impl;

public enum PortToken {
	LEFT, DOWN, UP, RIGHT, ANY, LAST;

	public PortToken reverse(){
	    switch(this){
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            case DOWN:
                return UP;
            case UP:
                return DOWN;
            default:
                return null;
        }
    }
}
