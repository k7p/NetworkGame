package board;
import list.*;
import player.*;

public class Evaluator{    
  

/*   
Takes in the player (b or w) whose pieces we're trying to find and board that contains
the pieces.

make a new DList called pieces & go through every element in the baord row by row 

insert pieces of the same color as a player into a list and then return the list

param player is the player whose pieces we are evaluating
param board is the board the pieces are on
return DList of pieces with the same color as the player
*/

	protected static DList piecesOfPlayer(int player, Board board){
		DList pieces = new DList();
		for (int x = 0; x < board.WIDTH; x++){
			for(int y = 0; y < board.HEIGHT; y++){
				try{
					if (board.getPieceValue(x,y).getColor() == player){
						pieces.insertBack(board.getPieceValue(x,y));
					}
				}
				catch (InvalidLocationException e) {
					continue;
				}
				catch (NullPointerException e) {  
					continue;
				}
			}
		}
		return pieces;
	}



/*

start out a counter
assign the name pieces to the DList returned by the piecesOfPlayer method
Let curr be the front of the list called pieces
while curr is indeed a valid node

move curr pointer to the next pointer

each connected counted twice so divide by 2

param player is the player whose pieces we are evaluating
param board is the board the pieces are on
return an int that is the number of connections a player has.
*/

	protected static int numConnections(int player,Board board) throws InvalidNodeException{
		int counter = 0;
		DList pieces = piecesOfPlayer(player, board);
		DListNode curr = (DListNode) pieces.front();
		while(curr.isValidNode()){
			counter += Connections.locateConnections((Piece)curr.item(),0,board).length();
			curr = (DListNode) curr.next();
		}
		return counter/2; 
	}


	//param player is the player whose pieces we are evaluating
	//param board is the board the pieces are on
	// return the number of pieces that a player has that are in a goal
	//and the player has 2 goals

	//start looking at start of the list
	//currentPiece is now the first item
//white checks top or bottom if the first item is at the bottom or top for white, just increase the total

	protected static int piecesInGoal(int player, Board board) throws InvalidNodeException{
		int tot = 0, goal_locator;
		DList pieces = piecesOfPlayer(player,board);
		
		DListNode curr = (DListNode) pieces.front();
		
		Piece currentPiece;

		while(curr.isValidNode()){  

			currentPiece = (Piece) curr.item();  
			goal_locator = goalLocator(currentPiece, board);
			if (player == board.BLACK && (goal_locator == 2 || goal_locator == -2)) {
				tot++;
			}
			if (player == board.WHITE && (goal_locator == 1 || goal_locator == -1)) {
				tot++;
			}

			curr = (DListNode) curr.next();    
		}
		return tot;
	}

	
	
	
	

	//postive values are for the left and top
	//param chip is the piece being looked at to see which goal it is in.
	//param board is the board the pieces are on
	//return an int which tells which goal the piece is in. 
	protected static int goalLocator(Piece chip, Board board){
		int curr_x = chip.getX(), curr_y = chip.getY();
		if (curr_x == 0){
			return 1;
		} else if (curr_x == board.WIDTH-1) {
			return -1;
		}
		if (curr_y == 0) {
			return 2;
		} else if (curr_y == board.HEIGHT-1) {
			return -2;
		}
		return 0; 
	}

//You start at piece A and compare its position to piece B, and return 
//the direction of the connection between them	
//greater y-values mean lower on the grid
//greater x-values mean more to the right of the grid
//if b's y-value is higher than a's y-value, so it's lower
//go to position 7 and go lower else, go up	
//if the distance between x & y is the same, you have a diagonal
//if they're in the same position, do nothing 	
//param a is one of the pieces used to find the direction
//param b is the other piece used to find the direction
//return a number giving the direction of the pieces
//In a 3x3 matrix, the current piece is in the center.
//1 is the northwest direction, 2 is the north direction, 3 is the northeast direction, 4 is the west direction, 5 is the east direction, 
//6 is the southwest direction, 7 is the south direction, and 8 is the southeast direction

	protected static int determineDirection(Piece a, Piece b){
        int b_x = b.getX(), a_x = a.getX(), b_y = b.getY(), a_y = a.getY();
		if(b_x == a_x && b_y == a_y) {
		} 
		else if (b_x == a_x){
			if (b_y > a_y) {  
				return 7;
			}
			else {
				return 2;
			}
		}

		else if (b_y == a_y){
			if (b_x > a_x) {
				return 5;
			}
			else {
				return 4;
			}
		}

		else if (b_y - a_y == b_x- a_x){
			if (b_x > a_x) {
				return 8;
			} 
			else {
				return 1;
			} 
		}
		else if (b_y - a_y == -1*(b_x- a_x)){  
			if (b_y > a_y) {
				return 6;
			} 
			else {
				return 3;  
			}  
		}
		return 98765432;  

	}  

	
	//you want to get all of the pieces that are not visited here, and it looks through a list of all the pieces that
	//have been visited already and should now be ignored.
	//if topOrLeft is true and you are either in the top or left goal or if bottomOrRight and you're in the bottom or right goal
	//name the curr item remove	 
	//param pieces is a DList of pieces. 
	//param visited is a DList of visited pieces. These pieces will be removed from pieces.
	//param board is the board the pieces are on.
	//return a DList of pieces that have not been visited. The pieces that are the same in pieces and visited are removed.
	

	protected static DList notVisited(DList pieces, DList visited, Board board) throws InvalidNodeException{
		if (visited.length() == 0){ 
			return pieces;  
		}
		DListNode remove = null; 
		boolean topOrLeft = alreadyVisitedTopOrLeftGoal(visited,board); 

		boolean botOrRight = alreadyVisitedBottomOrRightGoal(visited,board); 
		
		DListNode curr = (DListNode) pieces.front(); 
						
		Piece currentPiece; 

		DListNode currentVisited; 

		Piece currentVisitedPiece; 
		int goal_locator;
		while(curr.isValidNode()){

			currentPiece = (Piece) curr.item(); 	
			goal_locator = goalLocator(currentPiece, board);
			if((topOrLeft && (goal_locator > 0) ) || (botOrRight && (goal_locator < 0))){
				remove = curr;
			}  

			else{
				currentVisited = (DListNode) visited.front(); 
				while(currentVisited.isValidNode()){
					currentVisitedPiece = (Piece) currentVisited.item(); 

					if (identicalCoordinates(currentPiece,currentVisitedPiece)){
						remove = curr;
						break; 					  
					}
					currentVisited = (DListNode) currentVisited.next(); 
				}
			}

			

			curr = (DListNode) curr.next(); 
			if (remove != null){ 
				remove.remove();
				remove = null;
		
			}
		}
		return pieces;
	}


	//param visited is a DList of visited pieces
	//param board is the board that the pieces are on
	//return true if the top or left goal has been visited

	protected static boolean alreadyVisitedTopOrLeftGoal(DList visited, Board board) throws InvalidNodeException{
		DListNode curr = (DListNode) visited.front();
		while(curr.isValidNode()){
			if (goalLocator((Piece)curr.item(),board) > 0){
				return true;
			}
			curr = (DListNode) curr.next();
		}
		return false;
	}

	

	//curr is casted to dlistnode
	//cast dlisnode because you have a list of pieces
	//re-cast curr to be a piece to use in whichgoal
	//recast piece again to dlisnode to use the next method
	//param visited is a DList of visited pieces
	//param board is the board that the pieces are on
	//return true if the bottom or right goal has been visited
	

	protected static boolean alreadyVisitedBottomOrRightGoal(DList visited, Board board) throws InvalidNodeException{
		DListNode curr = (DListNode) visited.front(); 
		
		while(curr.isValidNode()){
			if (goalLocator((Piece)curr.item(),board) < 0){ 
				return true;
			}
			curr= (DListNode) curr.next(); 
		}
		return false;     
	}
 

	

	//curr.item() is node so must cast piece to it 
	//param pieces is a list of pieces
	//param board is the board the pieces are on
	//return a new list that contains the pieces that are located in the goal area

	
	
	protected static DList piecesInGoal(DList pieces, Board board) throws InvalidNodeException{
		DList piecesInGoal = new DList();

		DListNode curr = (DListNode) pieces.front();

		while(curr.isValidNode()){

			if (goalLocator((Piece) curr.item(),board) != 0){
				
				piecesInGoal.insertBack((Piece)curr.item());
				
			}
			curr = (DListNode) curr.next();
		}
		return piecesInGoal;  
	}

	/* 

See if two pieces have the same coordinates 
param pieceA is the first piece
param pieceB is the second piece
return a boolean true if pieceA and pieceB have identical coordinates.

*/

	protected static boolean identicalCoordinates(Piece pieceA, Piece pieceB){
		if (pieceA.getX() == pieceB.getX() && pieceA.getY() == pieceB.getY()) {
			return true;
		}
		return false;
	}
  

	
//let goalpieces be all the pieces of the same color as the player 
//find the pieces of the color of the player that are in the goal 	
//look at the front of the goalpieces list 
//keep track of all the visited pieces 
//return true if a network indeed exists 
//move pointer onto the next node in the list
//if winningNetwork doesn't exist, return false	
	

	public static boolean winningNetwork(Board board, int player) throws InvalidNodeException{
		
		DList goalpieces = piecesOfPlayer(player,board);

		goalpieces = piecesInGoal(goalpieces,board);

		DListNode curr = (DListNode) goalpieces.front();  

		while(curr.isValidNode()){
			DList visited = new DList();
			
			if (networkStartsAt((Piece) curr.item(),board, visited)){
				return true;
				
			}
			curr = (DListNode) curr.next(); 
			 
		}
		return false;  
	}    

/* 

Start from a piece, then you find the piece's connections, you're placing the piece that you found
the connections of into the visted list. Then you assign connections to the value of calling
notVisited on the list that has the visited chip in it, and ignores visited and goes to non-visited
connections is the list of the other terms, and if something is in visited, you delete it from 
connections, and that gives you the not-visited pieces 

*/

//copy is a copy of the visited nodes
//find connections to each piece that is connected to the particular piece
//param piece is the piece we look at to find if the network starts from that piece
//param board is the board the pieces are on.
//param visited is a DList of pieces that have been visited. We don't want to look at pieces that have already been visited.
//return ture if a netwrok exists from the piece



	public static boolean networkStartsAt(Piece piece, Board board, DList visited) throws InvalidNodeException{
		DList copy = (DList) visited.copy();


		DList connections = Connections.locateConnections(piece,0,board);


		copy.insertBack(piece);

		connections = notVisited(connections,copy,board);
	

		if (connections.length()==0){
			return validNetwork(copy,board);
		}

		if (validNetwork(copy,board)) {
			return true;
		}	

		DListNode curr = (DListNode) connections.front();  
		while(curr.isValidNode()) {

			if(networkStartsAt((Piece)curr.item(),board,copy)){
				return true;	
			}

			curr = (DListNode) curr.next();  

		}  
		return false;
	}

	
		/*

		checking directions of the first 2 and then next 2, and if the directions are the same,
		return false becausae that's illegal because you can't have 3 pieces of the same color
		pointing in the same direction 
		param visited is a DList of visited pieces
		param board is the board that the pieces are on
		return true if the network is valid

		*/


	public static boolean validNetwork(DList visited,Board board) throws InvalidNodeException{
		if(visited.length() < 6) {
			return false;
		}
		Piece goal1 = (Piece) visited.front().item(); 
		Piece goal2 = (Piece) visited.back().item();
		int goal_locator1 = goalLocator(goal1, board), goal_locator2 = goalLocator(goal2, board);
		if ((goal_locator1==0) || (goal_locator2==0)) { 
			return false;
		}
		if (goal_locator1 != (goal_locator2*-1)) { 
			return false;  
		} 

		DListNode curr = (DListNode) visited.front().next().next();

		DListNode prev = (DListNode) visited.front().next();  

		DListNode prev2 = (DListNode) visited.front();    


		while(curr.isValidNode()){
			int directionA = determineDirection((Piece) prev.item(), (Piece) curr.item());
			int directionB = determineDirection((Piece)prev2.item(), (Piece) prev.item());
			if (directionA == directionB){
				return false;
			}
			prev2 = prev; 
			prev = curr;  
			curr = (DListNode) curr.next(); 
		} 
		return true;
	}  
   	
/*
param board is the board being looked at.
param player is the color of player whose turn it is.
return a number for the player. A higher number is better and a lower number is worse.

if the player has a winning network, it returns the max number.
if the opponent has a winning network, it returns the negative max number.
the score returned by evaluate takes the number of connections of the player and subtracts the number of connections of the opponent from it.
evaluate also added points for putting pieces in the goals but it takes away points if there are too many pieces in the goals.
*/
	 public static double evaluate(Board board, int color ) throws InvalidNodeException{
		
        int player = color;
		int opponent = 1 - player;
        int pieces = board.pieceTotal(player);
        boolean opponent_win = winningNetwork(board, opponent);
        
             
		if ( (winningNetwork(board,player)) && (!(opponent_win)) ) {
			
			return Double.MAX_VALUE;
		}
		
		if (opponent_win) {
			
			return -Double.MAX_VALUE;
		}    


		double connectionTotalMachine = 0;

		double connectionTotalOpponent = 0;

		int num_connections_p = numConnections(player, board);
		int num_connections_o = numConnections(opponent, board);
  
		double score = 0;

		connectionTotalMachine += num_connections_p;

		  

		if (connectionTotalMachine < 0){	
			score -= connectionTotalMachine*2;
			}
		if (connectionTotalMachine > 0){
			score += connectionTotalMachine*2;
			}
		if (connectionTotalMachine == 0){
			score -= 0.50;  
			}

  
		connectionTotalOpponent -= num_connections_o;

		

		if (connectionTotalOpponent < 0)
			
			{score -= connectionTotalMachine*2;}
		if (connectionTotalMachine > 0)
		 
			{score += connectionTotalMachine*2;}
		if (connectionTotalOpponent == 0)  
			{score -= 0.50; }   

		
		double goalTotal = 0;
		goalTotal += piecesInGoal(player,board);


		if(goalTotal > 3){  
			goalTotal *= -1;
		}
		
		double finalTotal = goalTotal + score;

		return finalTotal;
	}	
    




}
