package com.capula.tetris.model.piece;

/**
 * Represents the pieces of the Tetris game. These shapes (sprites) are represented in a rectangle, though
 * those shapes can have transparent cells (where the value is zero). This way arbitrary shapes inside a rectangle
 * can be defined and used for collision detection.
 */
public enum Sprite {

    Q(new int[][]{{1, 1},
                  {1, 1}}),
    Z(new int[][]{{1, 1, 0},
                  {0, 1, 1}}),
    S(new int[][]{{0, 1, 1},
                  {1, 1, 0}}),
    T(new int[][]{{1, 1, 1},
                  {0, 1, 0}}),
    I(new int[][]{{1, 1, 1, 1}}),
    L(new int[][]{{1, 0},
                  {1, 0},
                  {1, 1}}),
    J(new int[][]{{0, 1},
                  {0, 1},
                  {1, 1}});

    private final int[][] mask;

    Sprite(int[][] mask) {
        this.mask = mask;
    }

    public int[][] getMask() {
        return mask;
    }

    public int getHeight() {
        return mask.length;
    }

    public int getWidth() {
        return mask[0].length;
    }

    public int[] getRow(int index) {
        return mask[index];
    }

}
