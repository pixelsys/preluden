package com.capula.tetris.model;

import com.capula.tetris.model.piece.Sprite;
import java.util.Arrays;

/**
 * Key model with operators for the Tetris game. It is a two dimensional array, each cell representing one
 * element in an array. More importantly it has operators for working with sprites. Taking transparent
 * background into account it can tell whether a sprite can be placed on a board and of course simple
 * operations like putting and clearing a sprite. The most complex operation is to sink filled rows,
 * which is key part of the Tetris game.
 */
public class Board {

    public static class BoardMetrics {

        private final Board board;

        public BoardMetrics(Board board) {
            this.board = board;
        }

        static boolean isNotEmpty(int[] row) {
            for(var cell : row) {
                if(1 == cell) {
                    return true;
                }
            }
            return false;
        }

        public int getNonEmptyRows() {
            var nonEmpty = 0;
            for(int j = 0; j < board.dimensionY; j++) {
                if(isNotEmpty(board.getRow(j))) {
                    nonEmpty++;
                }
            }
            return nonEmpty;
        }

    }

    private final int dimensionX;
    private final int dimensionY;
    private int[][] board;

    public Board(int dimensionX, int dimensionY) {
        this.dimensionX = dimensionX;
        this.dimensionY = dimensionY;
        this.board = new int[dimensionY][dimensionX];
    }

    public int getWidth() {
        return dimensionX;
    }

    public int getHeight() {
        return dimensionY;
    }

    public int[] getRow(int y) {
        return board[y];
    }

    int[] createOffsetRow(int xOffset, int rowIndex, Sprite piece) {
        var offsetRow = new int[dimensionX];
        System.arraycopy(piece.getRow(rowIndex), 0, offsetRow, xOffset, piece.getWidth());
        return offsetRow;
    }

    static boolean checkOverlap(int[] a, int[] b) {
        if(a.length != b.length) {
            throw new IllegalArgumentException("The two arrays must have the same length!");
        }
        for(int i = 0; i < a.length; i++) {
            if((a[i] == 1) && (b[i] == 1)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFree(int x, int y, Sprite piece) {
        for(int j = 0; j < piece.getHeight(); j++) {
            var boardRow = board[y + j];
            var maskedRow = createOffsetRow(x, j, piece);
            if(checkOverlap(boardRow, maskedRow)) {
                return false;
            }
        }
        return true;
    }

    private void write(int x, int y, int[][] mask, int fillValue) {
        for(int i = 0; i < mask.length; i++) {
            for(int j = 0; j < mask[i].length; j++) {
                if(1 == mask[i][j]) {
                    board[y + i][x + j] = fillValue;
                }
            }
        }
    }

    public void clear(int x, int y, Sprite piece) {
        write(x, y, piece.getMask(), 0);
    }

    public void put(int x, int y, Sprite piece) {
        write(x, y, piece.getMask(), 1);
    }

    static boolean isFilledRow(int[] row) {
        for(var i : row) {
            if(i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a new board without filled rows. Also simulating gravity, after clearing filled rows, elements
     * from the above are 'pulled down by gravity'.
     */
    public void sinkFilledRows() {
        var newBoard = new int[dimensionY][dimensionX];
        var newBoardYTracker = dimensionY - 1;
        for(int j = dimensionY - 1; j >= 0; j--) {
            if(!isFilledRow(board[j])) {
                newBoard[newBoardYTracker] = board[j];
                newBoardYTracker--;
            }
        }
        board = newBoard;
    }

    public BoardMetrics getStatus() {
        return new BoardMetrics(this);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        Arrays.stream(board).forEach(r ->
                sb.append(Arrays.toString(r)
                        .replaceAll(",", "")
                        .replaceAll(" ", "")
                        .replaceAll("0", " ")
                        .replaceAll("1", "X"))
                .append("\n"));
        return sb.toString();
    }

}
