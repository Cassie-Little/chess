package chess;

import java.util.Collection;
import java.util.HashSet;

public class RookMoves {
    public RookMoves() {

    }
    public Collection<ChessMove> rookPieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece) {
        HashSet<ChessMove> listOfMoves = new HashSet<>();
        MoveTypes.moveUp(board, myPosition, myPiece, listOfMoves);
        MoveTypes.moveDown(board, myPosition, myPiece, listOfMoves);
        MoveTypes.moveRight(board, myPosition, myPiece, listOfMoves);
        MoveTypes.moveLeft(board, myPosition, myPiece, listOfMoves);
        return listOfMoves;
    }

}


