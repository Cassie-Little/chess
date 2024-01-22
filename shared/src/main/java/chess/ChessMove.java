package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private final ChessPosition start;
    private final ChessPosition end;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.start = startPosition;
        this.end = endPosition;


    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return start;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return end;
    }

    @Override
    public String toString() {
        return "ChessMove{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(start, chessMove.start) && Objects.equals(end, chessMove.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return null;
        //throw new RuntimeException("Not implemented");
    }

    private void canBePromoted(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece) {
        if (myPiece.getPieceType() == ChessPiece.PieceType.PAWN
                && myPiece.getTeamColor() == ChessGame.TeamColor.WHITE
                && myPosition.getRow() == 8) {
            ChessPiece.QueenMoves(board, myPosition, myPiece);
        }


        throw new RuntimeException("not implemented");
    }
}
