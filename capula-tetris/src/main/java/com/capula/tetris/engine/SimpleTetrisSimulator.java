package com.capula.tetris.engine;

import com.capula.tetris.model.Board;
import com.capula.tetris.model.Board.BoardMetrics;
import com.capula.tetris.model.PieceStep;

/**
 * The core engine for the Tetris game simulation. It uses a board to keep track of the game, on which a sequence
 * of pieces can be simulated.
 */
public class SimpleTetrisSimulator {

    private final Board board;

    public SimpleTetrisSimulator(int boardSizeX, int boardSizeY) {
        this.board = new Board(boardSizeX, boardSizeY);
    }

    public SimpleTetrisSimulator(Board board) {
        this.board = board;
    }

    public BoardMetrics simulateSequence(String piecesSeqString) {
        var piecesSequence = piecesSeqString.split(",");
        for(var pieceStepString : piecesSequence) {
            var pieceStep = PieceStep.fromString(pieceStepString);
            //System.out.println("Piece step: " + pieceStep.getSprite() + " offset: " + pieceStep.getxCoord());
            // simulate gravity, put the piece on the top and then let it sink
            int i = pieceStep.getxCoord(), j = -1, cycleCount = 0;
            var piece = pieceStep.getSprite();
            while((++j < board.getHeight() - piece.getHeight() + 1)) {
                if(j != 0) {
                    board.clear(i, j - 1, piece);
                }
                if(board.isFree(i, j, piece)) {
                    // sink with a row
                    board.put(i, j, piece);
                } else {
                    // if can't place it there then revert and break - resting position
                    board.put(i, j - 1, piece);
                    j = board.getHeight();
                }
                cycleCount++;
            }
            if(0 == cycleCount) {
                // couldn't place the piece, game over
                break;
            }
            //System.out.println("Resting position: " + board);
            board.sinkFilledRows();
            //System.out.println("After sinking: " + board);
            //System.out.println("----------------------------------------------");
        }
        return board.getStatus();
    }

}
