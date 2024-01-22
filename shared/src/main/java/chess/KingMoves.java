package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMoves {
    public KingMoves() {
    }

    public Collection<ChessMove> kingPieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece) {
        HashSet<ChessMove> listOfMoves = new HashSet<>();
        up(board, myPosition, myPiece, listOfMoves);
        diagUpRight(board, myPosition, myPiece, listOfMoves);
        right(board, myPosition, myPiece, listOfMoves);
        diagDownRight(board, myPosition, myPiece, listOfMoves);
        down(board, myPosition, myPiece, listOfMoves);
        diagDownLeft(board, myPosition, myPiece, listOfMoves);
        left(board, myPosition, myPiece, listOfMoves);
        diagUpLeft(board, myPosition, myPiece, listOfMoves);
        return listOfMoves;
    }

    private void up(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        int tempRow = myPosition.getRow();
        int tempCol = myPosition.getColumn();
        tempRow += 1;
        ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
        addIfValidMove(board, myPosition, endPosition, myPiece, possibleMoves);
    }


    private void diagUpRight(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        int tempRow = myPosition.getRow();
        int tempCol = myPosition.getColumn();
        tempRow += 1;
        tempCol += 1;
        ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
        addIfValidMove(board, myPosition, endPosition, myPiece, possibleMoves);
    }

    private void right(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        int tempRow = myPosition.getRow();
        int tempCol = myPosition.getColumn();
        tempCol += 1;
        ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
        addIfValidMove(board, myPosition, endPosition, myPiece, possibleMoves);
    }

    private void diagDownRight(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        int tempRow = myPosition.getRow();
        int tempCol = myPosition.getColumn();
        int i = 1;
        while (i == 1) {
            tempRow -= 1;
            tempCol += 1;
            ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
            if (!addIfValidMove(board, myPosition, endPosition, myPiece, possibleMoves)) {
                break;
            }
            i++;
        }
    }

    private void down(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        int tempRow = myPosition.getRow();
        int tempCol = myPosition.getColumn();
        int i = 1;
        while (i == 1) {
            tempRow -= 1;
            ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
            if (!addIfValidMove(board, myPosition, endPosition, myPiece, possibleMoves)) {
                break;
            }
            i++;
        }
    }

    private void diagDownLeft(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        int tempRow = myPosition.getRow();
        int tempCol = myPosition.getColumn();
        int i = 1;
        while (i == 1) {
            tempRow -= 1;
            tempCol -= 1;
            ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
            if (!addIfValidMove(board, myPosition, endPosition, myPiece, possibleMoves)) {
                break;
            }
            i++;
        }
    }

    private void left(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        int tempRow = myPosition.getRow();
        int tempCol = myPosition.getColumn();
        int i = 1;
        while (i == 1) {
            tempCol -= 1;
            ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
            if (!addIfValidMove(board, myPosition, endPosition, myPiece, possibleMoves)) {
                break;
            }
            i++;
        }
    }

    private void diagUpLeft(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        int tempRow = myPosition.getRow();
        int tempCol = myPosition.getColumn();
        int i = 1;
        while (i == 1) {
            tempRow += 1;
            tempCol -= 1;
            ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
            if (!addIfValidMove(board, myPosition, endPosition, myPiece, possibleMoves)) {
                break;
            }
            i++;
        }
    }

    private boolean addIfValidMove(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        if (ChessPiece.isInbounds(endPosition.getRow(), endPosition.getColumn())) {
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                return true;
            } else {
                if (piece.getTeamColor() != myPiece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                }
                return false;
            }
        }
        return false;
    }
}
