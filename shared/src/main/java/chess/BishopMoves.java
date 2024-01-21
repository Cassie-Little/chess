package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class BishopMoves {

    public BishopMoves(){
    }
    public Collection<ChessMove> bishopPieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece){
        HashSet<ChessMove> listOfMoves = new HashSet<>();
        diagUpRight(board, myPosition, myPiece, listOfMoves);
        diagDownLeft(board, myPosition, myPiece, listOfMoves);
        diagDownRight(board, myPosition, myPiece, listOfMoves);
        diagUpLeft(board, myPosition, myPiece, listOfMoves);
        return listOfMoves;
    }
    private void diagUpRight(ChessBoard board, ChessPosition myPosition,ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        int tempCol = myPosition.getColumn();
        int tempRow = myPosition.getRow();
        while (true) {
            tempCol += 1;
            tempRow += 1;
            ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
            if (!addIfValidMove(board, myPosition,endPosition, myPiece, possibleMoves)){
                break;
            }
        }
    }
    private void diagUpLeft(ChessBoard board, ChessPosition myPosition,ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        int tempCol = myPosition.getColumn();
        int tempRow = myPosition.getRow();
        while (true) {
            tempCol -= 1;
            tempRow += 1;
            ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
            if (!addIfValidMove(board, myPosition,endPosition, myPiece, possibleMoves)){
                break;
            }
        }
    }
    private void diagDownRight(ChessBoard board, ChessPosition myPosition,ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        int tempCol = myPosition.getColumn();
        int tempRow = myPosition.getRow();
        while (true) {
            tempCol += 1;
            tempRow -= 1;
            ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
            if (!addIfValidMove(board, myPosition,endPosition, myPiece, possibleMoves)){
                break;
            }
        }
    }
    private void diagDownLeft(ChessBoard board, ChessPosition myPosition,ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        int tempCol = myPosition.getColumn();
        int tempRow = myPosition.getRow();
        while (true) {
            tempCol -= 1;
            tempRow -= 1;
            ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
            if (!addIfValidMove(board, myPosition,endPosition, myPiece, possibleMoves)){
                break;
            }
        }
    }
    private boolean addIfValidMove(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        if (ChessPiece.isInbounds(endPosition.getRow(), endPosition.getColumn())) {
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.BISHOP ));
               return true;
            }
            else {
                if (piece.getTeamColor() != myPiece.getTeamColor()){
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.BISHOP ));
                }
                return false;
            }
        }
        return false;
    }
}

