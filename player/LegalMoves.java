package player;
import list.*;
import board.*;

//Determines legal moves for a player

public class LegalMoves{  
/*
this method returns a DList of legal moves for a player. 
if there are fewer than 10 chips of a player, it returns a list of add moves.
if there are 10 or more chips it returns a list of step moves

param board is the board being looked at.
param player is the color of player whose turn it is.
return DList containing all legal moves available to a player
*/
public static DList allLegalMoves(Board board, int player) {
	if (board.pieceTotal(player) < 10) {
		return allLegalAddMoves(board, player);
	} else {
		return allLegalStepMoves(board, player);
	} 
}

/*	
this method goes through the board and tests a move at every location to see if it is legal.
if the move is legal it inserts that move into a DList.

param board is the board being looked at.
param player is the color of player whose turn it is.
return DList containing all legal add moves available to a player.
*/
private static DList allLegalAddMoves(Board board, int player) {
	DList addMoves = new DList();
	for(int j=0; j < board.HEIGHT; j++){
		for(int i=0; i < board.WIDTH; i++){
			Move testMove = new Move(i,j);
			if (isLegalMove(testMove, board, player)){
				addMoves.insertBack(testMove);
			}
		}  
	}
	return addMoves;
}


/*
This method goes through every row of the matrix, and looks at every piece. It checks if piece
is eligible for a Step move, and inserts that piece value into the myPieces
list. This forms a list of all the possible pieces that are eligible for playing a step move on.

param board is the board being looked at.
param player is the color of player whose turn it is.
return DList containing all legal add moves available to a player.
*/
private static DList allLegalStepMoves(Board board, int player) {

	DList stepMoves = new DList(); 
	DList myPieces = new DList();
	DListNode chipNode;
	Move testStepMove;
			
	for (int j = 0; j < board.HEIGHT; j++) {
		for (int i = 0; i < board.WIDTH; i++) {
			try {
				Piece step = board.getPieceValue(i, j);	
				if ((step != null) && (step.getColor() == player)) {
					myPieces.insertBack(step);
				}
			} catch (board.InvalidLocationException e) {
				continue;
			}
		}
	}

	try {
		chipNode = (DListNode) myPieces.front();
		while (chipNode.isValidNode()) {
			Piece test = (Piece) chipNode.item();
			for (int j = 0; j < board.HEIGHT; j++) {
				for (int i = 0; i < board.WIDTH; i++) {
					testStepMove = new Move(i, j, test.getX(), test.getY());
          	    	if (isLegalMove(testStepMove, board, player)) {
                       stepMoves.insertBack(testStepMove);
					}
				}
			}
            chipNode = (DListNode)chipNode.next();
		}

	} catch (InvalidNodeException e) {
			return null;
	}
	myPieces = null;
	chipNode = null;
	testStepMove = null;
	return stepMoves;         
}

/*
if the piece at the move has two neighbors it is an illegal move
if the piece has one neighbor, then it checks one depth further

param currentMove is the move being looked at to see it's neighbors.
param board is the board being looked at.
param player is the color of player whose turn it is.
return false if there is a cluster.
*/
private static boolean checkNeighbors(Move currentMove, Board board, int player) {

	Piece newPiece = new Piece(currentMove.x1, currentMove.y1, player), curr_piece;
	DList listNeighbors = newPiece.getNeighbors(board);

	try{
		if (listNeighbors.size() >= 2) {
			listNeighbors = null;
			return false;
		} else if (listNeighbors.size() == 1) {
			DListNode curr_neighbor = (DListNode) listNeighbors.front();
			while (curr_neighbor.isValidNode()){
				curr_piece = (Piece) curr_neighbor.item();
				if (curr_piece.getNeighbors(board).size() == 2) {
					listNeighbors = null;
					return false;
				}
				curr_neighbor = (DListNode) curr_neighbor.next();
			}
		}

	} catch (list.InvalidNodeException e2) {
		listNeighbors = null;
		return false;
	}
	listNeighbors = null;
	return true;
}
    
/*  
isLegalMove is a method that checks to see if a move is legal or not. 
this method checks if a move is in the opponent's goal.
it checks if a step move moves to the same spot.
it checks the number of black or white chips. if there are less than 10 chips, STEP moves are illegal.
it checks if a spot on the board is occupied. if it is the move is illegal.
it uses checkNeighbors to determine if there is a cluster.

param currentMove is the move being looked at to see if it is legal.
param board is the board being looked at.
param player is the color of player whose turn it is.
return true if the move is legal. return false if move is illegal.
*/
public static boolean isLegalMove(Move currentMove, Board board, int player) {

	boolean return_value;

	if (player==Board.BLACK){
		if ((currentMove.x1==0) || (currentMove.x1==7)) {
			return false;
		}    
	} else if (player==Board.WHITE){
		if ((currentMove.y1==0) || (currentMove.y1==7)) {
			return false;
		}
	}

	try{
		if (currentMove.moveKind == Move.STEP) {
	    	if ((currentMove.x1 == currentMove.x2) && (currentMove.y1 == currentMove.y2)){
	        	return false;
	    	} else if (board.pieceTotal(player) < 10) {
	    		return false;
		    } else if (board.getPieceValue(currentMove.x1, currentMove.y1) != null) {
		    	return false;
	    	}

		    board.removePiece(currentMove.x2, currentMove.y2);
		    return_value =  checkNeighbors(currentMove, board, player);
	    	board.insertPiece(currentMove.x2, currentMove.y2, player);
		    return return_value;

		} else if (currentMove.moveKind == Move.ADD) {
			if (board.pieceTotal(player) >= 10) {
				return false;
			} else if (board.getPieceValue(currentMove.x1, currentMove.y1) != null){
				return false;
			}
			return checkNeighbors(currentMove, board, player);

		}
	} catch (board.InvalidLocationException e3) {
		return false;
	}

	return true;

}

}