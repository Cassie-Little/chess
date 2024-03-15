package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoardUI {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 6;

    private static final String EMPTY = "   ";


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        var board = new chess.ChessBoard();
        out.print(ERASE_SCREEN);
        drawHeaders(out);
        for (int rowNum = 1; rowNum < BOARD_SIZE_IN_SQUARES; ++rowNum) {
            drawRowOfSquares(out, BOARD_SIZE_IN_SQUARES - rowNum); // Reverse row numbers
        }
        out.print(SET_BG_COLOR_DARK_GREEN);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out) {


        String[] headers = {" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        String[] sideNums = {"8", "7", "6", "5", "4", "3", "2", "1"};
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);

            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY.repeat(1));
            }
        }
//        for (int lineRow = 0; lineRow < 1; ++lineRow) {
//            setBlack(out);
//            out.print(EMPTY.repeat(8));
//            drawSideNums(out, sideNums[lineRow]);
//
//            if (lineRow < 7) {
//                out.print(EMPTY.repeat(1));
//            }
//        }
        out.println();
    }

    private static void drawSideNums(PrintStream out, String numText) {

        int boardSizeInSpaces = BOARD_SIZE_IN_SQUARES * SQUARE_SIZE_IN_CHARS +
                (BOARD_SIZE_IN_SQUARES - 1) * 1;

        for (int lineRow = 0; lineRow < 1; ++lineRow) {
            setGreen(out);
            out.print(EMPTY.repeat(boardSizeInSpaces));

            setBlack(out);
            out.println();
        }

//        for (int lineRow = 0; lineRow < 1; ++lineRow) {
//            //setBlack(out);
//            out.print(EMPTY.repeat(1));
//            printNumText(out, numText);
//            setBlack(out);
//            out.println();
//        }
    }

    private static void printNumText(PrintStream out, String nums) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print(nums);
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print(player);
    }

    private static void drawBoard(PrintStream out, chess.ChessBoard board) {

        //for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
        //drawRowOfSquares(out );
//            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
//                drawSideNums(out, "1");
//                setBlack(out);
        //}


        //}
    }

    private static void drawRowOfSquares(PrintStream out, int rowNum) {
        out.print(rowNum + 1);
        out.print(" ");

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_CHARS; ++squareRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                setWhite(out);

                if (squareRow == SQUARE_SIZE_IN_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;

                    out.print(EMPTY.repeat(prefixLength));
                    printPlayer(out, EMPTY);
                    out.print(EMPTY.repeat(suffixLength));
                } else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
                }
                // makes the vertical lines

                setGreen(out);
                //out.print(EMPTY);

            }
            out.println();
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setGreen(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREEN);
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
