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
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (myPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            addIfValidMove(board, myPosition, new ChessPosition(row + 1, col), myPiece, false, possibleMoves);
            addIfValidMove(board, myPosition, new ChessPosition(row + 1, col + 1), myPiece, true, possibleMoves);
            addIfValidMove(board, myPosition, new ChessPosition(row + 1, col - 1), myPiece, true, possibleMoves);
            if (myPosition.getRow() == 2 && board.getPiece(new ChessPosition(row + 1, col)) == null) {
                addIfValidMove(board, myPosition, new ChessPosition(row + 2, col), myPiece, false, possibleMoves);
            }
        }
    }

    private void forwardBlack(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (myPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            addIfValidMove(board, myPosition, new ChessPosition(row - 1, col), myPiece, false, possibleMoves);
            addIfValidMove(board, myPosition, new ChessPosition(row - 1, col + 1), myPiece, true, possibleMoves);
            addIfValidMove(board, myPosition, new ChessPosition(row - 1, col - 1), myPiece, true, possibleMoves);
            if (myPosition.getRow() == 7 && board.getPiece(new ChessPosition(row - 1, col)) == null) {
                addIfValidMove(board, myPosition, new ChessPosition(row - 2, col), myPiece, false, possibleMoves);
            }
        }
    }

    private void addIfValidMove(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition,
                                ChessPiece myPiece, boolean isDiag, Collection<ChessMove> possibleMoves) {
        if (ChessPiece.isInbounds(endPosition.getRow(), endPosition.getColumn())) {
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null && !isDiag) {
                if (myPiece.getTeamColor() == ChessGame.TeamColor.WHITE && endPosition.getRow() == 8) {
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.KNIGHT));
                }
                else if (myPiece.getTeamColor() == ChessGame.TeamColor.BLACK && endPosition.getRow() == 1) {
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.KNIGHT));

                } else {
                    possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                }
            } else if (piece != null && isDiag && piece.getTeamColor() != myPiece.getTeamColor()) {
                if (myPiece.getTeamColor() == ChessGame.TeamColor.WHITE && endPosition.getRow() == 8) {
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.KNIGHT));
                }
                else if (myPiece.getTeamColor() == ChessGame.TeamColor.BLACK && endPosition.getRow() == 1) {
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.KNIGHT));

                } else {
                    possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }

    }
}

