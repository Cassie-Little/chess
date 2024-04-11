package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn = TeamColor.WHITE;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK,
        NONE
    }


    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        var piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        HashSet<ChessMove> possibleMoves = new HashSet<>();
        for (var move : piece.pieceMoves(board, startPosition)) {
            var testBoard = board.copy();
            if (testBoard.chessMove(move) && !isInCheck(testBoard, piece.getTeamColor())) {
                possibleMoves.add(move);

            }
        }
        return possibleMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        var piece = board.getPiece(move.getStartPosition());
        if (piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("Not your team");
        } else if (isInStalemate(teamTurn)) {
            throw new InvalidMoveException("Stale");
        } else if (isInCheckmate(teamTurn)) {
            throw new InvalidMoveException("in check mate");
        } else if (!validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException("not in valid moves");
        } else {
            board.chessMove(move);
            var promo = move.getPromotionPiece();
            if (promo != null) {
                piece.setPieceType(promo);
            }
            if (piece.getTeamColor() == TeamColor.WHITE) {
                teamTurn = TeamColor.BLACK;
            } else if (piece.getTeamColor() == TeamColor.BLACK) {
                teamTurn = TeamColor.WHITE;
            }


        }
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(board, teamColor);
    }

    private static boolean isInCheck(ChessBoard board, TeamColor teamColor) {
        var kingOnBoard = board.getPiecePositions(ChessPiece.PieceType.KING, teamColor);
        if (kingOnBoard.isEmpty()) {
            return false;
        } else {
            var kingPosition = kingOnBoard.getFirst();
            for (int col = 1; col <= 8; col++) {
                for (int row = 1; row <= 8; row++) {
                    var chessPosition = new ChessPosition(row, col);
                    var piece = board.getPiece(chessPosition);
                    if (piece != null && piece.getTeamColor() != teamColor) {
                        for (var move : piece.pieceMoves(board, chessPosition)) {
                            if (move.getEndPosition().equals(kingPosition)) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            var kingPosition = board.getPiecePositions(ChessPiece.PieceType.KING, teamColor).getFirst();
            var king = board.getPiece(kingPosition);
            for (var move : king.pieceMoves(board, kingPosition)) {
                var testBoard = board.copy();
                if (testBoard.chessMove(move) && !isInCheck(testBoard, king.getTeamColor())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean doesTeamHaveAMove = false;
        for (int col = 1; col <= 8; col++) {
            for (int row = 1; row <= 8; row++) {
                var chessPosition = new ChessPosition(row, col);
                var piece = board.getPiece(chessPosition);
                if (piece != null && piece.getTeamColor() == teamColor && !validMoves(chessPosition).isEmpty()) {
                    doesTeamHaveAMove = true;

                }
            }
        }
        if (!isInCheck(teamColor) && !doesTeamHaveAMove) {
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
