/*A class created to represent pieces on the board that allows
 for the creation of piece objects 

It also makes use of the List package from HW5
 */

package board;  
import list.*;

public class Piece{

	private int x;
	private int y;
	private int color;


//the piece constructor that takes in a position in 
//x & y and a color that's represented as an int
//creates a new DList that will contain all of the piece's neighbors	

	public Piece(int x, int y, int color){

		this.x = x;
		this.y = y;
		this.color = color;

	}

	//creating getter methods to obtain the values of the coordinates & list

//return the x coordinate
	public int getX(){
		return x;
	}

//return the y-value
	public int getY(){
		return y;
	}

//return the color of the piece
	public int getColor(){
		return color;  
	}  


//param board is the current board being examined
//keep a count of the current neighbors, which is 0
//make a DList called neighbors
//check the piece from left to right, and put it into a list	
//return the neighborList of the current piece object 

	public DList getNeighbors(Board board){
		int count = 0;
		Piece curr_piece;
		DList neighbors = new DList();
		for(int incr_x = -1; incr_x <= 1; incr_x++){
			for(int incr_y = -1; incr_y <= 1; incr_y++){
				try {
					if(((x + incr_x)!= -1) || ((x + incr_x)!= 8) || ((y + incr_y)!= -1) || ((y + incr_y)!= 8)){
						curr_piece = board.getPieceValue(x + incr_x, y + incr_y);
						if ((curr_piece != null) && (curr_piece.color == this.color)) {
							neighbors.insertBack(curr_piece);
							count +=1;
							if (count > 2){
								return neighbors;		
							}
						}
					}
				} catch (InvalidLocationException e) {
					continue;
				} catch (ArrayIndexOutOfBoundsException e2) {
					continue;
				}
			}
		}
		return neighbors;
	}



/* check how many neighbors surround a particular piece in order
   to apply the cluster rule of the game. This number will be checked
   by the legal moves, which determines when to apply cluster rule
   param board is a board that is passed in
   returns an int that represents the number of neighbors that a current piece on the board has
*/
   
/* catch the nullpoitnerexception just in case it happens */

 
	public int numNeighbors(Board board){
		int count = 0;
		Piece curr_piece;
		for(int incr_x = -1; incr_x <= 1; incr_x++){
			for(int incr_y = -1; incr_y <= 1; incr_y++){
				try {
					if(((x + incr_x)!= -1) || ((x + incr_x)!= 8) || ((y + incr_y)!= -1) || ((y + incr_y)!= 8)){
						curr_piece = board.getPieceValue(x + incr_x, y + incr_y);
						if ((curr_piece != null) && (curr_piece.color == this.color)) {
							count++;
						}
						if (count > 2){
							return count; 		
						}
					}
				} catch (InvalidLocationException e) {
					continue;
				} catch (ArrayIndexOutOfBoundsException e2) {
					continue;
				}
			}
		}
		return count;
   }



}