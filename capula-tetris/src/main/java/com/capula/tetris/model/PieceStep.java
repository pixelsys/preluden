package com.capula.tetris.model;

import com.capula.tetris.model.piece.Sprite;

/**
 * Tuple to work nicely with input sequences.
 */
public class PieceStep {

    private final Sprite sprite;
    private final Integer xCoord;

    public PieceStep(Sprite sprite, Integer xCoord) {
        this.sprite = sprite;
        this.xCoord = xCoord;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Integer getxCoord() {
        return xCoord;
    }

    public static PieceStep fromString(String input) {
        if(input.length() != 2) {
            throw new IllegalArgumentException("Invalid piece step description: " + input);
            // FIXME add more checks here
        }
        var sprite = Sprite.valueOf(input.substring(0, 1));
        var xCoord = Integer.parseInt(input.substring(1));
        return new PieceStep(sprite, xCoord);
    }

}
