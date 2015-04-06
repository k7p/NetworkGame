/* MachinePlayer.java */

package player;
import list.*;  
import board.*;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {
  private Board board;
  private int color, opponentColor, searchDepth;  
  LegalMoves legal_moves;  

  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color) {
    this(color, 2);
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
    if (color == 0) {
      this.color = Board.BLACK;
      this.opponentColor = Board.WHITE;
    } else {
      this.color = Board.WHITE;
      this.opponentColor = Board.BLACK;
    }

    this.searchDepth = searchDepth;
    legal_moves = new LegalMoves();
    board = new Board();
    
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
 
 

  public Move chooseMove() { 
    try {
    
      if (color == board.BLACK) {  
        if (board.pieceTotal(color) == 0) {
            Move add = new Move(4, 3);
            if(LegalMoves.isLegalMove(add, board, color)){
            board.makeMove(add, color);
            return add;
          }else{
            Move planb = new Move(4, 4);
            board.makeMove(planb, color);
            return planb;
          }
        }
        if (board.pieceTotal(color) == 1) { 
            Move add = new Move(4, 5);
            Move planb = new Move(4, 6);
            Move planc = new Move(3, 6);
            if(LegalMoves.isLegalMove(add, board, color)){
                board.makeMove(add, color);
                return add;
            }else if(LegalMoves.isLegalMove(planb, board, color)){
                board.makeMove(planb, color);
                return planb;
            }else{
                board.makeMove(planc, color);
                return planc;
            }
        }
      }
      if (color == board.WHITE) {
        if (board.pieceTotal(color) == 0) {
            Move add = new Move(3, 3);
            if(LegalMoves.isLegalMove(add, board, color)){
            board.makeMove(add, color);
            return add;
          }else{
            Move planb = new Move(4, 4);
            board.makeMove(planb, color);
            return planb;
        }
      }
        if (board.pieceTotal(color) == 1) { 
            Move add = new Move(5, 5);
            Move planb = new Move(3, 5);
            
             
            if(LegalMoves.isLegalMove(add, board, color)){
                board.makeMove(add, color);
                return add;
            }else if(LegalMoves.isLegalMove(planb, board, color)){
                board.makeMove(planb, color);
                return planb;
            }
        }
      }
    }catch (InvalidLocationException e) {  
      return new Move();
    }  

    Move move = AI.gameTree(color, board, searchDepth);
    try{
    
    board.makeMove(move, color);
    
  }catch (InvalidLocationException e){
        }
        return move;
  }

  

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
     try{
        if (legal_moves.isLegalMove(m, board, opponentColor)) {
    board.makeMove(m, opponentColor);
    return true;
  }else{
    return false;
  }
    }catch (InvalidLocationException e){
        }
        return true;
  }


  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m) {
    try{
        if (legal_moves.isLegalMove(m, board, color)) {
            board.makeMove(m, color);
            return true;
        }
        else
            return false;
        }
    catch (InvalidLocationException e){
    }
        return true;
  }
}
