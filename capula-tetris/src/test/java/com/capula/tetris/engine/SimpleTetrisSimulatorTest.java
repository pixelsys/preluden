package com.capula.tetris.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SimpleTetrisSimulatorTest {

    private static final int BOARD_SIZE_X = 10;
    private static final int BOARD_SIZE_Y = 10;

    private SimpleTetrisSimulator simulator;

    @BeforeEach
    public void setUp() {
        simulator = new SimpleTetrisSimulator(BOARD_SIZE_X, BOARD_SIZE_Y);
    }

    @Test
    public void testSimulatorFinalResult_example1() {
        var input = "I0,I4,Q8";
        final int expected = 1;
        var actual = simulator.simulateSequence(input);
        assertEquals(expected, actual.getNonEmptyRows());
    }

    @Test
    public void testSimulatorFinalResult_example2() {
        var input = "T1,Z3,I4";
        final int expected = 4;
        var actual = simulator.simulateSequence(input);
        assertEquals(expected, actual.getNonEmptyRows());
    }

    @Test
    public void testSimulatorFinalResult_example3() {
        var input = "Q0,I2,I6,I0,I6,I6,Q2,Q4";
        final int expected = 3;
        var actual = simulator.simulateSequence(input);
        assertEquals(expected, actual.getNonEmptyRows());
    }

}
