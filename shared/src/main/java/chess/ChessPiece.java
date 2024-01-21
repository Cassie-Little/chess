package chess;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor color;
    private final ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color =  pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }
    public static boolean isInbounds(int row, int col) {
        if (row >= 1 && col >= 1) {
            if (row <= 8 && col <= 8) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
       return switch(type){
            case BISHOP -> new BishopMoves().bishopPieceMoves(board, myPosition, this);
            case ROOK -> new RookMoves().rookPieceMoves(board,myPosition, this);
            case KNIGHT -> new KnightMoves().knightPieceMoves(board, myPosition, this);
            case QUEEN -> new QueenMoves().queenPieceMoves(board, myPosition, this);
            case KING -> new KingMoves().kingPieceMoves(board, myPosition, this);
            case PAWN -> new PawnMoves().pawnPieceMoves(board, myPosition, this);
            default -> throw new RuntimeException( type + " not implemented");
        };
    }
}
