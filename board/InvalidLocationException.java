package board;

public class InvalidLocationException extends Exception {

	protected InvalidLocationException() {
		super("Invalid Move");
	}

//calls Exception default constructor that constructs new exception with null as its message

	protected InvalidLocationException(String s) {
		super("Invalid Move " + s);
  	}	

//calls Exception constructor that takes in a string and can display an output method 

}