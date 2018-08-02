package com.bsettle.tis100clone;


import com.bsettle.tis100clone.impl.CommandNode;
import com.bsettle.tis100clone.impl.Mode;
import com.bsettle.tis100clone.impl.Node;
import com.bsettle.tis100clone.impl.PortToken;
import com.bsettle.tis100clone.state.CommandNodeState;
import com.bsettle.tis100clone.state.NodeState;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CommandDiffTest {
    private CommandNode node;
    @Before
    public void init(){
        node = new CommandNode();
    }

    @Test
    public void NOP_TEST() {
        node.setCommand(0, "NOP");
        node.setCommand(1, "NOP");
        node.activate();

        HashMap<String, Object> diff = node.getDiff();
        assertEquals(diff.size(), 2);
        assertEquals(diff.get(CommandNodeState.INCREMENT_COMMAND), true);
        assertEquals(diff.get(CommandNodeState.MODE), Mode.RUN);
        node.push();
        assertEquals(node.getState().getCommandIndex(), 1);

        diff = node.getDiff();
        assertEquals(diff.size(), 1);
        assertEquals(diff.get(CommandNodeState.INCREMENT_COMMAND), true);
        node.push();
        assertEquals(node.getState().getCommandIndex(), 0);

        diff = node.getDiff();
        assertEquals(diff.size(), 1);
        assertEquals(diff.get(CommandNodeState.INCREMENT_COMMAND), true);
        node.push();
        assertEquals(node.getState().getCommandIndex(), 1);
    }
    @Test
    public void NEG_TEST() {
        node.commit(CommandNodeState.ACCUMULATOR, 3);
        node.push();
        node.setCommand(0, "NEG");
        node.activate();

        HashMap<String, Object> diff = node.getDiff();
        assertEquals(diff.size(), 3);
        assertEquals(diff.get(CommandNodeState.MODE), Mode.RUN);
        assertEquals(diff.get(CommandNodeState.INCREMENT_COMMAND), true);
        assertEquals(diff.get(CommandNodeState.ACCUMULATOR), -3);
        node.push();

        diff = node.getDiff();
        assertEquals(diff.size(), 2);
        assertEquals(diff.get(CommandNodeState.INCREMENT_COMMAND), true);
        assertEquals(diff.get(CommandNodeState.ACCUMULATOR), 3);
    }
    @Test
    public void SAV_TEST() {
        node.commit(CommandNodeState.ACCUMULATOR, 3);
        node.push();
        node.setCommand(0, "SAV");

        node.activate();
        HashMap<String, Object> diff = node.getDiff();
        assertEquals(diff.size(), 3);
        assertEquals(diff.get(CommandNodeState.MODE), Mode.RUN);
        assertEquals(diff.get(CommandNodeState.INCREMENT_COMMAND), true);
        assertEquals(diff.get(CommandNodeState.BACKUP), 3);
    }
    @Test
    public void SWP_TEST() {
        node.commit(CommandNodeState.ACCUMULATOR, 3);
        node.commit(CommandNodeState.BACKUP, 1);
        node.push();
        node.setCommand(0, "SWP");

        node.activate();
        HashMap<String, Object> diff = node.getDiff();
        assertEquals(diff.size(), 4);
        assertEquals(diff.get(CommandNodeState.MODE), Mode.RUN);
        assertEquals(diff.get(CommandNodeState.INCREMENT_COMMAND), true);
        assertEquals(diff.get(CommandNodeState.ACCUMULATOR), 1);
        assertEquals(diff.get(CommandNodeState.BACKUP), 3);
        node.push();

        diff = node.getDiff();
        assertEquals(diff.size(), 3);
        assertEquals(diff.get(CommandNodeState.INCREMENT_COMMAND), true);
        assertEquals(diff.get(CommandNodeState.ACCUMULATOR), 3);
        assertEquals(diff.get(CommandNodeState.BACKUP), 1);

    }
    @Test
    public void ADD_NUMBER_TEST() {
        node.setCommand(0, "ADD 2");

        node.activate();
        HashMap<String, Object> diff = node.getDiff();
        assertEquals(diff.size(), 3);
        assertEquals(diff.get(CommandNodeState.MODE), Mode.RUN);
        assertEquals(diff.get(CommandNodeState.INCREMENT_COMMAND), true);
        assertEquals(diff.get(CommandNodeState.ACCUMULATOR), 2);
        node.push();

        diff = node.getDiff();
        assertEquals(diff.size(), 2);
        assertEquals(diff.get(CommandNodeState.INCREMENT_COMMAND), true);
        assertEquals(diff.get(CommandNodeState.ACCUMULATOR), 4);
        node.push();
    }
    @Test
    public void ADD_PORT_TEST() {
        node.setCommand(0, "ADD LEFT");
        node.activate();
        HashMap<String, Object> diff = node.getDiff();
        assertEquals(diff.size(), 2);
        assertEquals(diff.get(CommandNodeState.MODE), Mode.READ);
        assertEquals(diff.get(CommandNodeState.READING_PORT), PortToken.LEFT);
        node.push();

        diff = node.getDiff();
        assertEquals(diff.size(), 0);
        node.push();
    }

    @Test
    public void ADD_ANY_TEST() {
        node.setCommand(0, "ADD ANY");
        node.activate();
        HashMap<String, Object> diff = node.getDiff();
        assertEquals(diff.size(), 2);
        assertEquals(diff.get(CommandNodeState.MODE), Mode.READ);
        assertEquals(diff.get(CommandNodeState.READING_PORT), PortToken.ANY);
        node.push();

        diff = node.getDiff();
        assertEquals(diff.size(), 0);
        node.push();
    }

    @Test
    public void JMP_TEST() {
        node.setCommand(0, "JMP B");
        node.setCommand(1, "A: JMP C");
        node.setCommand(2, "B: JMP A");
        node.setCommand(3, "C: NOP");

        node.activate();
        HashMap<String, Object> diff = node.getDiff();
        assertEquals(diff.size(), 2);
        assertEquals(diff.get(CommandNodeState.MODE), Mode.RUN);
        assertEquals(diff.get(CommandNodeState.COMMAND_INDEX), 2);
        node.push();

        diff = node.getDiff();
        assertEquals(diff.size(), 1);
        assertEquals(diff.get(CommandNodeState.COMMAND_INDEX), 1);
        node.push();

        diff = node.getDiff();
        assertEquals(diff.size(), 1);
        assertEquals(diff.get(CommandNodeState.COMMAND_INDEX), 3);
        node.push();
    }

    @Test
    public void JMP_CONDITIONAL_TEST() {
        node.setCommand(0, "A: JNZ A");
        node.setCommand(1, "ADD 1");
        node.setCommand(2, "JNZ A");
        node.setCommand(3, "NOP");

        node.activate();
        HashMap<String, Object> diff = node.getDiff();
        assertEquals(diff.size(), 2);
        assertEquals(diff.get(CommandNodeState.MODE), Mode.RUN);
        assertEquals(diff.get(CommandNodeState.INCREMENT_COMMAND), true);
        node.push();

        assertEquals(node.getState().getCommandIndex(), 1);

        diff = node.getDiff();
        assertEquals(diff.size(), 2);
        assertEquals(diff.get(CommandNodeState.INCREMENT_COMMAND), true);
        assertEquals(diff.get(CommandNodeState.ACCUMULATOR), 1);
        node.push();

        diff = node.getDiff();
        assertEquals(diff.size(), 1);
        assertEquals(diff.get(CommandNodeState.COMMAND_INDEX), 0);
    }

    @Test
    public void JRO_NUMBER_TEST() {
        node.setCommand(0, "JRO 2");
        node.setCommand(1, "NOP");
        node.setCommand(2, "JRO 10");
        node.setCommand(3, "NOP");
        node.setCommand(4, "NOP");

        node.activate();
        HashMap<String, Object> diff = node.getDiff();
        assertEquals(diff.size(), 2);
        assertEquals(diff.get(CommandNodeState.COMMAND_INDEX), 2);
        assertEquals(diff.get(CommandNodeState.MODE), Mode.RUN);
        node.push();

        diff = node.getDiff();
        assertEquals(diff.size(), 1);
        assertEquals(diff.get(CommandNodeState.COMMAND_INDEX), 12);
        node.push();
    }

    @Test
    public void MOV_READ(){
        node.setCommand(0, "MOV LEFT RIGHT");

        node.activate();
        HashMap<String, Object> diff = node.getDiff();
        assertEquals(diff.size(), 2);
        assertEquals(diff.get(CommandNodeState.MODE), Mode.READ);
        assertEquals(diff.get(CommandNodeState.READING_PORT), PortToken.LEFT);
        node.push();

        diff = node.getDiff();
        assertEquals(diff.size(), 0);
    }

    @Test
    public void MOV_WRITE(){
        node.setCommand(0, "MOV 1 RIGHT");

        node.activate();
        HashMap<String, Object> diff = node.getDiff();
        assertEquals(diff.size(), 3);
        assertEquals(diff.get(CommandNodeState.MODE), Mode.WRITE);
        assertEquals(diff.get(CommandNodeState.WRITING_PORT), PortToken.RIGHT);
        assertEquals(diff.get(CommandNodeState.WRITING_VALUE), 1);
        node.push();

        diff = node.getDiff();
        assertEquals(diff.size(), 0);
    }
}