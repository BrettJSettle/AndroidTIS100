package com.bsettle.tis100clone.state;

import android.annotation.SuppressLint;

import com.bsettle.tis100clone.impl.CommandNode;
import com.bsettle.tis100clone.impl.IOColumnInfo;
import com.bsettle.tis100clone.impl.InputNode;
import com.bsettle.tis100clone.impl.NodeGrid;
import com.bsettle.tis100clone.impl.OutputNode;
import com.bsettle.tis100clone.impl.PortToken;
import com.bsettle.tis100clone.impl.StackNode;
import com.bsettle.tis100clone.level.LevelInfo;
import com.bsettle.tis100clone.level.LevelInfo.*;
import com.bsettle.tis100clone.impl.Node;
import com.bsettle.tis100clone.level.NodeInfo;

import java.util.HashMap;

public class GameState {
    private int step = -1;

    private LevelInfo info;

    private NodeGrid nodeGrid; // stored as [row][col]
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, InputNode> inputNodes = new HashMap<>();
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, OutputNode> outputNodes = new HashMap<>();


    public GameState(LevelInfo info){
        this.info = info;
        init();
    }

    private void init(){
        int numCols = info.getColumns(), numRows = info.getRows();
        nodeGrid = new NodeGrid(numRows, numCols);
        for (int r = 0; r < numRows; r++){
            for (int c = 0; c < numCols; c++){
                NodeInfo i = info.getNodeInfo(r, c);
                Node n = makeNode(i);
                nodeGrid.setNode(r, c, n);
            }
        }

        for(IOColumnInfo inputInfo : info.getInputColumns()){
            InputNode inputNode = new InputNode(inputInfo.getValues());
            int n = inputInfo.getColumn();
            Node.connectNodes(inputNode, PortToken.DOWN, nodeGrid.getNode(0, n));
            inputNodes.put(n, inputNode);
        }

        for (IOColumnInfo outputInfo : info.getOutputColumns()){
            int n = outputInfo.getColumn();
            OutputNode outputNode = new OutputNode(outputInfo.getValues());
            Node.connectNodes(nodeGrid.getNode(numRows-1, n), PortToken.DOWN, outputNode);
            outputNodes.put(n, outputNode);
        }

    }

    private void getDiff(){

        for (OutputNode outputNode : outputNodes.values()){
            outputNode.getDiff();
        }

        for (InputNode inputNode : inputNodes.values()){
            inputNode.getDiff();
        }

        for (Node node : nodeGrid.nodeIterator()) {
            node.getDiff();
        }
    }

    private void push(){
        for (InputNode inputNode : inputNodes.values()){
            inputNode.push();
        }

        for (OutputNode outputNode : outputNodes.values()){
            outputNode.push();
        }

        for (Node node : nodeGrid.nodeIterator()) {
            node.push();
        }
    }

    public void step() {

        if (!isRunning()) {
            activate();
            return;
        }

        getDiff();
        push();
    }

    public void deactivate() {
        if (!isRunning()){
            return;
        }

        step = -1;

        for (Node node : nodeGrid.nodeIterator()) {
            node.deactivate();
        }
        for (InputNode inputNode : inputNodes.values()){
            inputNode.deactivate();
        }
        for (OutputNode outputNode : outputNodes.values()){
            outputNode.deactivate();
        }

    }

    public void erase(){
        if (step == -1){
            return;
        }

        for (Node node : nodeGrid.nodeIterator()) {
            node.reset();
        }

        for (InputNode inputNode : inputNodes.values()){
            inputNode.reset();
        }
        for (OutputNode outputNode : outputNodes.values()){
            outputNode.reset();
        }

    }

    private void activate() {
        if (step >= 0){
            return;
        }
        step = 0;
        for (Node node : nodeGrid.nodeIterator()) {
            node.activate();
        }

        for (InputNode inputNode : inputNodes.values()){
            inputNode.activate();
        }

        for (OutputNode outputNode : outputNodes.values()){
            outputNode.activate();
        }

    }

    public boolean isRunning(){
        return step >= 0;
    }

    private Node makeNode(NodeInfo info){
        if (info == null){
            return new CommandNode();
        }

        switch (info.getNodeType()){
            case STACK:
                return new StackNode();
            case DISABLED:
                return null;
            case COMMAND:
            default:
                return new CommandNode();
        }
    }

    public Node getNode(int row, int col) {
        return nodeGrid.getNode(row, col);
    }

    public final InputNode getInputNode(int column){
        return inputNodes.get(column);
    }

    public final OutputNode getOutputNode(int column){
        return outputNodes.get(column);
    }

    public LevelInfo getLevelInfo() {
        return info;
    }

}
