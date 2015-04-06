package player;
import board.*;
import list.*;  


public class AI {    

  public static Move gameTree(int color, Board board, int depth) {
    Best scored = treeHelper(color, color, board, depth, -Double.MAX_VALUE,
        Double.MAX_VALUE);
    return scored.move;           
  } 

  
  private static Best treeHelper(int color, int AIcolor, Board board,
      int depth, double alpha, double beta) {
    

    Best optimalMove = new Best();
    Best response;

try{
    if (depth == 0 || Evaluator.winningNetwork(board, AIcolor) || Evaluator.winningNetwork(board, changeColor(AIcolor))) {
      optimalMove.score = Evaluator.evaluate(board, AIcolor);  
      return optimalMove;  
    }

    if (color == AIcolor) {
      optimalMove.score = -Double.MAX_VALUE;      
    }  
    else {
      optimalMove.score = Double.MAX_VALUE;
    }


DListNode curr_move_node = (DListNode)LegalMoves.allLegalMoves(board,color).front();


  while(curr_move_node!=null){
    Move curr_move = (Move)curr_move_node.item();
    board.makeMove(curr_move,color);
    response = treeHelper(changeColor(color), AIcolor, board, depth-1, alpha, beta);
    board.undoMove(curr_move);

    if (color == AIcolor && optimalMove.score <= response.score) {
    optimalMove.move=curr_move;
    optimalMove.score=response.score;
    alpha=response.score;
    }
    else if(color==changeColor(AIcolor)&& optimalMove.score>=response.score){
      optimalMove.move=curr_move;
    optimalMove.score=response.score;
    beta=response.score;
    }
    if (beta < alpha) { 
            break;
          }
          curr_move_node=(DListNode)curr_move_node.next();
        }
        curr_move_node = null;
        return optimalMove;


    }
        catch (InvalidNodeException e) {
          
        }
        catch (board.InvalidLocationException e) {
            
        }
    return optimalMove;      
      }
      
  /**
   * Changes a color.
   */

  private static int changeColor(int color){
    if(color == Board.WHITE){
      return Board.BLACK;
    } else if (color == Board.BLACK){
      return Board.WHITE;
    } else {
      return Board.EMPTY;
    }
  }




}
