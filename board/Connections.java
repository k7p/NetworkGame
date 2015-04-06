package board;
import list.*;
import player.*;

public class Connections{

	/*
Looks at a board and a piece in particular to find connections around that piece. 
Logic is based off of a 3x3 matrix, and you specify a direction that you do not look at when 
looking for connections, and connections are returned in the form of chips in a list
In a 3x3 matrix, the current piece is in the center.
1 is the northwest direction, 2 is the north direction, 3 is the northeast direction, 4 is the west direction, 5 is the east direction, 
6 is the southwest direction, 7 is the south direction, and 8 is the southeast direction
When a piece of the same color of the player is found, it adds that piece to the list of connections.


param curr is the piece that is being looked at to find the connected pieces
param direction is the direction that is not being searched in
param board is the board being looked at
return DList of pieces that are connected to a piece
 */	

	protected static DList locateConnections(Piece curr, int direction, Board board){
		DList connections = new DList();
		Piece curr_piece;
		int player = curr.getColor();
		int curr_x = curr.getX(), curr_y = curr.getY();
		try{
			for(int x1 = curr_x+1; x1 < board.WIDTH; x1++){
				if (direction == 5 || direction == 4){
					break;
				}
				curr_piece = board.getPieceValue(x1, curr_y);
				if (curr_piece != null){
					if (curr_piece.getColor() == player){
						connections.insertFront(curr_piece);
						break;
					}
					break;
				}
			}
		} catch (InvalidLocationException e){

		} 
		
		try{
			for (int x2 = curr_x-1; x2 >= 0; x2--){
				if (direction == 5 || direction == 4){
					break;
				}
				curr_piece = board.getPieceValue(x2, curr_y);
				if (curr_piece != null){
					if (curr_piece.getColor() == player){
						connections.insertFront(curr_piece);
						break;
					}
					break;
				}
			}
		} catch (InvalidLocationException e){

		}
		
		try{
			for (int y1 = curr_y+1; y1 < board.HEIGHT; y1++){ 
				if (direction == 2 || direction == 7){
					break;
				}
				curr_piece = board.getPieceValue(curr_x,y1);
				if (curr_piece != null){
					if (curr_piece.getColor() == player){
						connections.insertFront(curr_piece);
						break;
					}
					break;
				}
			}
		} catch (InvalidLocationException e){

		}

		try{
			for (int y2 = curr_y-1; y2 >= 0; y2--){  
				if (direction == 2 || direction == 7){
					break;
				}
				curr_piece = board.getPieceValue(curr_x,y2);
				if (curr_piece != null){
					if (curr_piece.getColor() == player){
						connections.insertFront(curr_piece);
						break;
					}
					break;
				}
			}
		} catch (InvalidLocationException e){

		}
		
		try{
			for (int z1 = 1; z1 < Math.min(board.HEIGHT-curr_y,board.WIDTH-curr_x);z1++){  
				if (direction == 8 || direction == 1){
					break;
				}
				curr_piece = board.getPieceValue(curr_x+z1,curr_y+z1);
				if (curr_piece != null){
					if (curr_piece.getColor() == player){
						connections.insertFront(curr_piece);
						break;
					}
					break;
				}
			}
		} catch (InvalidLocationException e){

		}

		try{
			for(int z2 = 1; z2 < Math.min(curr_y,curr_x); z2++){ 
				if (direction == 8 || direction == 1){
					break;
				}
				curr_piece = board.getPieceValue(curr_x-z2,curr_y-z2);
				if (curr_piece != null){
					if (curr_piece.getColor() == player){
						connections.insertFront(curr_piece);
						break;
					}
					break;
				}
			}
		} catch (InvalidLocationException e){

		}
		
		try{
			for (int z3 = 1; z3 < Math.min(board.HEIGHT-curr_y,curr_x);z3++){
				if (direction == 6 || direction == 3){
					break;
				}
				curr_piece = board.getPieceValue(curr_x-z3,curr_y+z3);
				if (curr_piece != null){
					if (curr_piece.getColor() == player){
						connections.insertFront(curr_piece);
						break;
					}
					break;
				}
			}
		} catch (InvalidLocationException e){

		}

		try{
			for (int z4 = 1; z4 < Math.min(curr_y,board.WIDTH-curr_x);z4++){  
				if (direction == 6 || direction == 3){
					break;
				}
				curr_piece = board.getPieceValue(curr_x+z4,curr_y-z4);
				if (curr_piece != null){
					if (curr_piece.getColor() == player){
						connections.insertFront(curr_piece);
						break;
					}
					break;
				}
			}
		} catch (InvalidLocationException e){
			
		}  
		
		return connections;
	} 
}