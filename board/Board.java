package board;
import list.*;
import player.*;  

public class Board {


	private Piece[][] matrix;

	public static final int HEIGHT = 8;
	public static final int WIDTH = 8;

	public static final int WHITE = 1;
	public static final int BLACK = 0;  

	public static final int EMPTY = 2; 
    
	private int totalWhite = 0;  
	private int totalBlack = 0;  


//Creates a new board to play on, which is repesented by an 8x8 array   

	public Board(){

		matrix = new Piece[HEIGHT][WIDTH];

	}  

//insert a chip of the specified color at the position at x & y in the matrix 
//it's basically the add move	
//param x is the x-coordinate, param y is the y-coordinate, color is color of the piece 	
    
	public void insertPiece(int x, int y, int color) {

		Piece piece = new Piece(x, y, color);
		this.matrix[x][y] = piece;
		if (color==WHITE) {
			this.totalWhite++;
		} else {
			this.totalBlack++;
		}

	}

 //PieceTotal returns the total amount of chips of each color that's on the board
 //param color is an int that represents the color black or white 	
//returns the total amount of white or black pieces that are currently on the board	

	public int pieceTotal(int color) {
		if (color == BLACK) {
			return this.totalBlack;
		} else {
			return this.totalWhite;
		}
	}




//getPieceValue returns a piece at a specific position 
//param x is x-coordinate or piece, & param y is y-coordinate	

	public Piece getPieceValue(int x, int y) throws InvalidLocationException {
	 	if ( this.outOfBounds(x,y) ) { 
	  		throw new InvalidLocationException(); 
		} else {
	  		return this.matrix[x][y];    
	  	}
	}   
	

//outOfBounds checks if a piece is located in one of the illegal Piece 4 corners	
//param x is x-coordinate of the piece, & param y is y-coordinate of the piece
//returns true if outOfBounds and false otherwise	    

	private boolean outOfBounds(int x, int y) {
		if (x==0) {
			if ((y==0) || (y==7)) {
				return true;
			}
		} else if (x==7) {
			if ((y==0) || (y==7)) {
				return true;
			}
		} else if ( ((x<0) || (x>7)) || ((y<0) || (y>7))) {
			return true;
		}

		return false;
	}
 
//getGrid method returns a Piece covered board with different chips on it
//takes in no parameters 
//returns the grid	

	public Piece[][] getGrid() {
		return this.matrix;
	}


//This method removes a game piece at a position x,y from the board
//params x & y are the x and y coordinates on the board   	

	public void removePiece(int x, int y) throws InvalidLocationException{

		try {
			Piece originalPiece = getPieceValue(x, y);
			if (originalPiece != null){
				this.matrix[x][y]= null;
				if (originalPiece.getColor() == WHITE){
					this.totalWhite--;
				} else {
					this.totalBlack--;
				}

			}
		} catch (InvalidLocationException e2) {
			throw new InvalidLocationException();
			}  

	}

//This method performs a STEP move, where a user replaces a position with a new one
//if value of getchip at x,y is not null, and value of getchip at the newx and newY that the
//user passes in is also null, then you want to replace the chip at x,y with the chip
//that is at getChip(newX, newY) because a step is just replacing the old chip with a new
//one 
/*
Then you make a new chip called original, and that is actually the original chip
that happens to be found when you call getChip(x,y), so now you have a reference to the 
original chip.

After that, you want to obtain the color of the original chip, so you simply say
transitionalChip.getColor() to obtain the int value that corresponds to Black 0
or White, which is 1

The whole reason for doing this transitional chip thing is to figure out the color
of the oriignal chip

Then you simply remove the chip that is at x,y, and then you add a chip with the 
new coordinates but with the same color as the original chip

 */

//perform a step move on the board with coordinates for 2 pieces 
//params x and y represent coordinates of the first move, and
//params x2 and y2 represent coordinates of another move that is the new move

	public void stepMove(int x2, int y2, int x, int y) throws InvalidLocationException {

		try {  
			if( (getPieceValue(x,y) != null) && (getPieceValue(x2,y2) == null) ) {	
				Piece original = getPieceValue(x,y);
				int originalColor = original.getColor();
				this.removePiece(x,y);
				this.insertPiece(x2, y2, originalColor);
			}
		} catch (InvalidLocationException e3) {
			throw new InvalidLocationException();
		}
	}

	//makeMove plays a move that is either a step move or an add move 
	//param move takes in either an add or step move
	//param color takes in an int representing a black or white piece

	public void makeMove(Move move, int color) throws InvalidLocationException{

		try {
			if (move.moveKind == move.ADD){  
				insertPiece(move.x1, move.y1, color);
			} else {
				stepMove(move.x1, move.y1, move.x2, move.y2);  
			}
		} catch (InvalidLocationException e4) {
			throw new InvalidLocationException();
		}
  
	}   

	//An undoMove function that gets used in the gametree. It just undos a move
	//param move takes in either an ADD or STEP move 
  
	public void undoMove(Move move) throws InvalidLocationException{

		try {
			if (move.moveKind == move.ADD) {  
				removePiece(move.x1, move.y1);  
			} else {
				stepMove(move.x2, move.y2, move.x1, move.y1);
			}
		} catch (InvalidLocationException e5) {
			throw new InvalidLocationException();  
		}
	}   

	

} //closes class
