package com.capula.tetris.model;

import static com.capula.tetris.model.piece.Sprite.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class BoardTest {

    @Test
    public void test_createOffsetRow1() {
        var board = new Board(5, 5);
        var result = board.createOffsetRow(1, 0, I);
        assertTrue(Arrays.equals(new int[]{0, 1, 1, 1, 1}, result));
    }

    @Test
    public void test_createOffsetRow2() {
        var board = new Board(8, 8);
        var result = board.createOffsetRow(3, 1, S);
        assertTrue(Arrays.equals(new int[]{0, 0, 0, 1, 1, 0, 0, 0}, result));
    }

    @Test
    public void test_rowsDoOverlap() {
        int[] a = {0, 1, 1, 1, 0, 0};
        int[] b = {0, 1, 0, 0, 1, 0};
        assertTrue(Board.checkOverlap(a, b));
    }

    @Test
    public void test_rowsDontOverlap() {
        int[] a = {0, 1, 1, 1, 0, 0, 0};
        int[] b = {0, 0, 0, 0, 1, 1, 0};
        assertFalse(Board.checkOverlap(a, b));
    }

    @Test
    public void test_putPiece() {
        var board = new Board(10, 10);
        board.put(7, 8, T);
        assertTrue(Arrays.equals(new int[]{0, 0, 0, 0, 0, 0, 0, 1, 1, 1}, board.getRow(8)));
        assertTrue(Arrays.equals(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 0}, board.getRow(9)));
    }

    @Test
    public void test_operands() {
        var board = new Board(10, 10);
        board.put(7, 8, T);
        assertTrue(board.isFree(6, 7, I));
        assertFalse(board.isFree(6, 8, I));
        assertFalse(board.isFree(6, 9, I));
        board.clear(7, 8, T);
        assertTrue(board.isFree(6, 8, I));
    }

    @Test
    public void test_sinkFilledBottomRows() {
        var board = new Board(5, 5);
        board.put(0, 4, I);
        board.put(0, 3, I);
        board.put(3, 2, J);
        board.sinkFilledRows();
        for(var i = 0; i <= 3; i++) {
            assertTrue(Arrays.equals(new int[]{0, 0, 0, 0, 0}, board.getRow(i)));
        }
        assertTrue(Arrays.equals(new int[]{0, 0, 0, 0, 1}, board.getRow(4)));
    }

    @Test
    public void test_sinkFilledBottomRows2() {
        var board = new Board(4, 5);
        board.put(0, 4, I);
        board.put(0, 3, I);
        board.put(1, 1, T);
        board.sinkFilledRows();
        for(var i = 0; i <= 2; i++) {
            assertTrue(Arrays.equals(new int[]{0, 0, 0, 0}, board.getRow(i)));
        }
        assertTrue(Arrays.equals(new int[]{0, 1, 1, 1}, board.getRow(3)));
        assertTrue(Arrays.equals(new int[]{0, 0, 1, 0}, board.getRow(4)));
    }

}
