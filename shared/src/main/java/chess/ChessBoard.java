package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessBoard() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(spots, that.spots);
    }

    public ChessBoard copy() {
        var chessBoard = new ChessBoard();
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                chessBoard.spots[row][col] = spots[row][col];
            }
        }
        return chessBoard;
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "spots=" + Arrays.deepToString(spots) +
                '}';
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(spots);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    private ChessPiece[][] spots = new ChessPiece[8][8];

    public void addPiece(ChessPosition position, ChessPiece piece) {
        spots[position.getRow() - 1][position.getColumn() - 1] = piece;


    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return spots[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        int x = 1;
        spots[x][x] = null;
        // White pawns
        addPiece(new ChessPosition(2, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(2, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(2, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(2, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(2, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(2, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(2, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(2, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        // Black pawns
        addPiece(new ChessPosition(7, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(7, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(7, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(7, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(7, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(7, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(7, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(7, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        // White rooks
        addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        // Black Rooks
        addPiece(new ChessPosition(8, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        // White Knights
        addPiece(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        // Black Knight
        addPiece(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        //White Bishop
        addPiece(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        // Black Bishop
        addPiece(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        // White Queen
        addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        // Black Queen
        addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        // White King
        addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        // Black King
        addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));


    }
    /*
    returns all of the chess positions for the give type and color
    */

    public ArrayList<ChessPosition> getPiecePositions(ChessPiece.PieceType pieceType, ChessGame.TeamColor teamColor) {
        ArrayList<ChessPosition> positions = new ArrayList<>();

        for (int col = 1; col <= 8; col++) {
            for (int row = 1; row <= 8; row++) {
                var chessPosition = new ChessPosition(row, col);
                var piece = getPiece(chessPosition);
                if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == pieceType) {
                    positions.add(chessPosition);
                }
            }
        }
        return positions;
    }

    public boolean chessMove(ChessMove move) {
        var startPosition = move.getStartPosition();
        var endPosition = move.getEndPosition();
        var piece = getPiece(startPosition);

        if (piece == null) {
            return false;
        }
        spots[startPosition.getRow() - 1][startPosition.getColumn() - 1] = null;
        spots[endPosition.getRow() - 1][endPosition.getColumn() - 1] = piece;
        return true;
    }
}
