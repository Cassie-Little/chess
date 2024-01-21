package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMoves {
    public PawnMoves() {
    }
    public Collection<ChessMove> pawnPieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece) {
        HashSet<ChessMove> listOfMoves = new HashSet<>();
        forwardWhite(board, myPosition, myPiece, listOfMoves);
        forwardBlack(board, myPosition, myPiece, listOfMoves);
        return listOfMoves;
    }
    private void forwardWhite(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        int tempRow = myPosition.getRow();
        int tempCol = myPosition.getColumn();
        int i = 1;
        if (myPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
            while (i == 1) {
                ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
                if (!addIfValidMove(board, myPosition, endPosition, myPiece, possibleMoves)) {
                    break;
                }
                tempRow += 1;
                i++;
            }
        }
    }
    private void forwardBlack(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        int tempRow = myPosition.getRow();
        int tempCol = myPosition.getColumn();
        int i = 1;
        if (myPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
            while (i == 1) {
                tempRow -= 1;
                ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
                if (!addIfValidMove(board, myPosition, endPosition, myPiece, possibleMoves)) {
                    break;
                }
                i++;
            }
        }
    }
    private boolean addIfValidMove(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        if (ChessPiece.isInbounds(endPosition.getRow(), endPosition.getColumn())) {
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.PAWN ));
                return true;
            }
            else {
                if (piece.getTeamColor() != myPiece.getTeamColor()){
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.PAWN ));
                }
                return false;
            }
        }
        return false;
    }

}

