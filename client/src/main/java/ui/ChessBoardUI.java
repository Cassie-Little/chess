package ui;

import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoardUI {
    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_CHARS = 3;

    private static final String EMPTY = " ";
    private static final String[] whiteHeaders = {" ", "a", "b", "c", "d", "e", "f", "g", "h", " "};
    private static final String[] blackHeaders = {" ", "h", "g", "f", "e", "d", "c", "b", "a", " "};
    private static final String[] whiteSideNums = {"8", "7", "6", "5", "4", "3", "2", "1"};
    private static final String[] blackSideNums = {"1", "2", "3", "4", "5", "6", "7", "8"};



    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        var board = new chess.ChessBoard();
        board.resetBoard();
        displayBoard(out, board, ChessGame.TeamColor.WHITE);
    }
    public static void displayBoard(PrintStream out, chess.ChessBoard board, ChessGame.TeamColor teamColor) {
        out.print(ERASE_SCREEN);
        drawHeaders(out, teamColor);
        drawBoard(out, board, teamColor);
        drawHeaders(out, teamColor);
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_MAGENTA);
    }



    private static void drawHeaders(PrintStream out, ChessGame.TeamColor teamColor) {

        setGreen(out);
        var headers = teamColor == ChessGame.TeamColor.WHITE? whiteHeaders : blackHeaders;
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);
        }
        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        out.print(EMPTY);
        printHeaderText(out, headerText);
        out.print(EMPTY);
    }

    private static void printHeaderText(PrintStream out, String player) {
        //out.print(SET_BG_COLOR_DARK_GREEN);
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print(player);
        setGreen(out);
    }

    private static void drawBoard(PrintStream out, chess.ChessBoard board, ChessGame.TeamColor teamColor) {
        if (teamColor == ChessGame.TeamColor.WHITE) {
            drawBoardWhite(out, board);
        }
        else {
            drawBoardBlack(out, board);
        }

    }
    private static void drawBoardWhite(PrintStream out, chess.ChessBoard board) {
        var sideNums =  blackSideNums;
        for (int boardRow = 0; boardRow < 8; ++boardRow) {
            drawSideNumber(out, sideNums[boardRow]);
            for (int col = 0; col < 8; ++col) {
                var position = new chess.ChessPosition(boardRow+1, col+1);
                var piece = board.getPiece(position);
                drawPiece(out, piece, boardRow, col);
            }
            drawSideNumber(out, sideNums[boardRow]);
            out.println();
        }

    }
    private static void drawBoardBlack(PrintStream out, chess.ChessBoard board) {
        var sideNums = blackSideNums;
        for (int boardRow = 7; boardRow >= 0; --boardRow) {
            drawSideNumber(out, sideNums[boardRow]);
            for (int col = 7; col >= 0; --col) {
                var position = new chess.ChessPosition(boardRow+1, col+1);
                var piece = board.getPiece(position);
                drawPiece(out, piece, boardRow, col);
            }
            drawSideNumber(out, sideNums[boardRow]);
            out.println();
        }

    }

    private static String getPieceText(ChessPiece.PieceType type) {
        return switch (type) {
            case KING -> "♔";
            case QUEEN -> "♕";
            case ROOK -> "♖";
            case BISHOP -> "♗";
            case PAWN -> "♙";
            case KNIGHT -> "♘";
            default -> " ";
        };
    }

    private static String getPieceColor(ChessGame.TeamColor color) {
        return switch (color) {
            case WHITE -> SET_TEXT_COLOR_WHITE;
            case BLACK -> SET_TEXT_COLOR_BLACK;
            default -> SET_TEXT_COLOR_MAGENTA;
        };
    }

    private static void drawPiece(PrintStream out, chess.ChessPiece piece, int rowNumber, int colNumber) {
        var pieceText = " ";
        var textColor = SET_TEXT_COLOR_YELLOW;
        if (piece != null) {
            pieceText = getPieceText(piece.getPieceType());
            textColor = getPieceColor(piece.getTeamColor());
        }
        if ((colNumber+ rowNumber) % 2 == 0) {
            out.print(SET_BG_COLOR_MAGENTA);
        } else {
            out.print(SET_BG_COLOR_DARK_GREEN);
        }
        out.print(textColor);
        out.print(EMPTY);
        out.print(pieceText);
        out.print(EMPTY);
        setGreen(out);
    }

    private static void drawSideNumber(PrintStream out, String numText) {
        out.print(EMPTY);
        printHeaderText(out, numText);
        out.print(EMPTY);

    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setGreen(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void printPlayer(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setWhite(out);
    }
}
