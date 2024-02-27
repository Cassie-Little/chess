package chess;

import java.util.Collection;
import java.util.HashSet;

public class KnightMoves {
    public KnightMoves() {
    }
        public Collection<ChessMove> knightPieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece){
            HashSet<ChessMove> listOfMoves = new HashSet<>();
            upLeft(board, myPosition, myPiece, listOfMoves);
            leftUp(board, myPosition, myPiece, listOfMoves);
            upRight(board, myPosition, myPiece, listOfMoves);
            rightUp(board, myPosition, myPiece, listOfMoves);
            leftDown(board, myPosition, myPiece, listOfMoves);
            downLeft(board, myPosition, myPiece, listOfMoves);
            downRight(board, myPosition, myPiece, listOfMoves);
            rightDown(board, myPosition, myPiece, listOfMoves);
            return listOfMoves;
        }
        public void leftUp( ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves){
        int tempRow = myPosition.getRow();
        int tempCol = myPosition.getColumn();
        int index = 1;
        while (index == 1) {
            tempRow += 1;
            tempCol = tempCol - 2;
            ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
            if (!SimplePieceMoves.addIfValidMove(board, myPosition, endPosition, myPiece, possibleMoves)) {
                break;
            }
            index ++;
        }

        }
        public void upLeft( ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves){
            int tempRow = myPosition.getRow();
            int tempCol = myPosition.getColumn();
            int index = 1;
            while (index == 1) {
                tempRow += 2;
                tempCol -= 1;
                ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
                if (!SimplePieceMoves.addIfValidMove(board, myPosition, endPosition, myPiece, possibleMoves)) {
                    break;
                }
                index ++;
            }
        }
        public void upRight( ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves){
            int tempRow = myPosition.getRow();
            int tempCol = myPosition.getColumn();
            int index = 1;
            while (index == 1) {
                tempRow += 2;
                tempCol += 1;
                ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
                if (!SimplePieceMoves.addIfValidMove(board, myPosition, endPosition, myPiece, possibleMoves)) {
                    break;
                }
                index ++;
            }
        }
        public void rightUp( ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves){
            int tempRow = myPosition.getRow();
            int tempCol = myPosition.getColumn();
            int index = 1;
            while (index ==1) {
                tempRow += 1;
                tempCol = tempCol + 2;
                ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
                if (!SimplePieceMoves.addIfValidMove(board, myPosition, endPosition, myPiece, possibleMoves)) {
                    break;
                }
                index ++;
            }
        }
        public void leftDown( ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves){
            int tempRow = myPosition.getRow();
            int tempCol = myPosition.getColumn();
            int index = 1;
            while (index == 1) {
                tempRow -= 1;
                tempCol = tempCol - 2;
                ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
                if (!SimplePieceMoves.addIfValidMove(board, myPosition,endPosition, myPiece, possibleMoves)){
                    break;
                }
                index++;
            }
        }
        public void downLeft( ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves){
            int tempRow = myPosition.getRow();
            int tempCol = myPosition.getColumn();
            int index = 1;
            while (index == 1) {
                tempRow -= 2;
                tempCol -= 1;
                ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
                if (!SimplePieceMoves.addIfValidMove(board, myPosition,endPosition, myPiece, possibleMoves)){
                    break;
                }
                index++;
            }
        }
        public void downRight( ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves){
            int tempRow = myPosition.getRow();
            int tempCol = myPosition.getColumn();
            int index = 1;
            while (index == 1) {
                tempRow -= 2;
                tempCol += 1;
                ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
                if (!SimplePieceMoves.addIfValidMove(board, myPosition,endPosition, myPiece, possibleMoves)){
                    break;
                }
                index++;
            }
        }
        public void rightDown( ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, Collection<ChessMove> possibleMoves){
            int tempRow = myPosition.getRow();
            int tempCol = myPosition.getColumn();
            int index = 1;
            while (index == 1) {
                tempRow -= 1;
                tempCol = tempCol + 2;
                ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
                if (!SimplePieceMoves.addIfValidMove(board, myPosition,endPosition, myPiece, possibleMoves)){
                    break;
                }
                index++;
            }
        }


}
