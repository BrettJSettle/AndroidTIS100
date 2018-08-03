package com.bsettle.tis100clone.state;

import android.annotation.SuppressLint;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.bsettle.tis100clone.impl.CommandNode;
import com.bsettle.tis100clone.impl.IOColumnInfo;
import com.bsettle.tis100clone.impl.InputNode;
import com.bsettle.tis100clone.impl.NodeCollection;
import com.bsettle.tis100clone.impl.NodeGrid;
import com.bsettle.tis100clone.impl.PortToken;
import com.bsettle.tis100clone.level.LevelInfo;
import com.bsettle.tis100clone.level.LevelInfo.*;
import com.bsettle.tis100clone.impl.Node;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

public class GameState {
    private Logger logger = Logger.getLogger("GameState");

    private boolean running = false;
    private LevelInfo info;

    private NodeGrid nodeGrid; // stored as [row][col]
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, InputNode> inputNodes = new HashMap<>();
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, Node> outputNodes = new HashMap<>();

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
                Node n = getNodeFromInfo(i);
                nodeGrid.setNode(r, c, n);
            }
        }

        for(IOColumnInfo inputInfo : info.getInputColumns()){
            InputNode inputNode = new InputNode(inputInfo.getValues());
            int n = inputInfo.getColumn();
            Node.connectNodes(inputNode, PortToken.DOWN, nodeGrid.getNode(0, n));
            inputNodes.put(n, inputNode);
        }

        int rowCount = info.getRows();
        for (IOColumnInfo outputInfo : info.getOutputColumns()){
            int n = outputInfo.getColumn();
            Node outputNode = nodeGrid.getNode(rowCount-1, n);
            outputNodes.put(n, outputNode);
        }

    }

    private HashMap<Node, Integer> ioDiff(){
        HashMap<Node, Integer> outputs = new HashMap<>();
        for (Node outputNode : outputNodes.values()){
            PortToken outPort = outputNode.getState().getWritingPort();
            if (outPort != null && (outPort.equals(PortToken.ANY) || outPort.equals(PortToken.DOWN))){
                outputs.put(outputNode, outputNode.getState().getWritingValue());
                outputNode.writeFinished(outPort);
            }
        }

        for (InputNode inputNode : inputNodes.values()){
            inputNode.getDiff();
        }
        return outputs;
    }

    public HashMap<Node, Integer> step() {

        if (!running) {
            return new HashMap<>();
        }
        HashMap<Node, Integer> outputs = ioDiff();

        for (Node node : nodeGrid.nodeIterator()) {
            node.getDiff();
        }

        for (InputNode inputNode : inputNodes.values()){
            inputNode.push();
        }

        for (Node node : nodeGrid.nodeIterator()) {
            node.push();
        }

        return outputs;
    }

    public void reset() {
        for (Node node : nodeGrid.nodeIterator()) {
            node.reset();
        }
        for (InputNode inputNode : inputNodes.values()){
            inputNode.reset();
        }

        running = false;
    }

    public void clear(){
        for (Node node : nodeGrid.nodeIterator()) {
            node.clear();
        }

        for (InputNode inputNode : inputNodes.values()){
            inputNode.reset();
        }

        running = false;
    }

    public void activate() {
        for (Node node : nodeGrid.nodeIterator()) {
            node.activate();
        }

        for (InputNode inputNode : inputNodes.values()){
            inputNode.activate();
        }

        running = true;
    }

    public boolean isRunning(){
        return running;
    }

    private Node getNodeFromInfo(NodeInfo info){
        return new CommandNode();
    }

    public Node getNode(int row, int col) {
        return nodeGrid.getNode(row, col);
    }

    public final HashMap<Integer, InputNode> getInputNodes(){
        return inputNodes;
    }

    public final HashMap<Integer, Node> getOutputNodes(){
        return outputNodes;
    }

    public LevelInfo getLevelInfo() {
        return info;
    }

}
