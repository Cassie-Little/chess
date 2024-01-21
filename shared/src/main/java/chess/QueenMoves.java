package chess;

import java.util.Collection;
import java.util.HashSet;

public class QueenMoves {
    public QueenMoves() {
    }
    public Collection<ChessMove> queenPieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece) {
        HashSet<ChessMove> listOfMoves = new HashSet<>();
        moveUp(board, myPosition, myPiece, listOfMoves);
        moveDown(board, myPosition, myPiece, listOfMoves);
        moveRight(board, myPosition, myPiece, listOfMoves);
        moveLeft(board, myPosition, myPiece, listOfMoves);
        diagUpLeft(board, myPosition, myPiece, listOfMoves);
        diagUpRight(board, myPosition, myPiece, listOfMoves);
        diagDownRight(board, myPosition, myPiece, listOfMoves);
        diagDownLeft(board, myPosition, myPiece, listOfMoves);
        return listOfMoves;
    }
    private void moveUp(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        int tempRow = myPosition.getRow();
        int tempCol = myPosition.getColumn();
        while (true) {
            tempRow += 1;
            ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
            if (!addIfValidMove(board, myPosition,endPosition, myPiece, possibleMoves)){
                break;
            }
        }
    }
    private void moveDown(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        int tempRow = myPosition.getRow();
        int tempCol = myPosition.getColumn();
        while (true) {
            tempRow -= 1;
            ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
            if (!addIfValidMove(board, myPosition,endPosition, myPiece, possibleMoves)){
                break;
            }
        }
    }
    private void moveRight(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        int tempRow = myPosition.getRow();
        int tempCol = myPosition.getColumn();
        while (true) {
            tempCol += 1;
            ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
            if (!addIfValidMove(board, myPosition,endPosition, myPiece, possibleMoves)){
                break;
            }
        }
    }
    private void moveLeft(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        int tempRow = myPosition.getRow();
        int tempCol = myPosition.getColumn();
        while (true) {
            tempCol -= 1;
            ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
            if (!addIfValidMove(board, myPosition,endPosition, myPiece, possibleMoves)){
                break;
            }
        }
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
                possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.QUEEN ));
                return true;
            }
            else {
                if (piece.getTeamColor() != myPiece.getTeamColor()){
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.QUEEN ));
                }
                return false;
            }
        }
        return false;
    }
}
