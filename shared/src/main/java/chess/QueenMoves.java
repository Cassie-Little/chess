package chess;

import java.util.Collection;
import java.util.HashSet;

public class QueenMoves {
    public QueenMoves() {
    }
    public Collection<ChessMove> queenPieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece) {
        HashSet<ChessMove> listOfMoves = new HashSet<>();
        MoveTypes.moveUp(board, myPosition, myPiece, listOfMoves);
        MoveTypes.moveDown(board, myPosition, myPiece, listOfMoves);
        MoveTypes.moveRight(board, myPosition, myPiece, listOfMoves);
        MoveTypes.moveLeft(board, myPosition, myPiece, listOfMoves);
        MoveTypes.diagUpLeft(board, myPosition, myPiece, listOfMoves);
        MoveTypes.diagUpRight(board, myPosition, myPiece, listOfMoves);
        MoveTypes.diagDownRight(board, myPosition, myPiece, listOfMoves);
        MoveTypes.diagDownLeft(board, myPosition, myPiece, listOfMoves);
        return listOfMoves;
    }



}
