package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class BishopMoves {

    public BishopMoves() {
    }

    public Collection<ChessMove> bishopPieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece) {
        HashSet<ChessMove> listOfMoves = new HashSet<>();
        MoveTypes.diagUpRight(board, myPosition, myPiece, listOfMoves);
        MoveTypes.diagDownLeft(board, myPosition, myPiece, listOfMoves);
        MoveTypes.diagDownRight(board, myPosition, myPiece, listOfMoves);
        MoveTypes.diagUpLeft(board, myPosition, myPiece, listOfMoves);
        return listOfMoves;
    }
}

