import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BlobsState implements Cloneable {
	public static final String O = "O";
	public static final String X = "X";
	public static final String EMPTY = "-";

	private int passCount = 0;
	private int pRow = -3;
	private int pCol = -3;
	private int xPassCount = 0;
	private int oPassCount = 0;
	public int xCount = 2;
	public int oCount = 2;
	public int rowSize = 7;
	public int columnSize = 7;
	private String[] board = new String[rowSize*columnSize];
	private double utility = -1; // 1: win for X, 0: win for O, 0.5: draw
	public String playerToMove = X;
	
	public BlobsState(){
		for(int i = 0; i < (rowSize * columnSize); i++){
			board[i] = EMPTY;
		}
		board[getAbsPosition(0, 0)] = O;
		board[getAbsPosition(columnSize-1, rowSize-1)] = O;
		board[getAbsPosition(0, rowSize-1)] = X;
		board[getAbsPosition(columnSize-1 , 0)] = X;
	}
	private void analyzeUtility() {
		// if all the spots are filled, end game and return winner
		if((xCount + oCount) == (rowSize * columnSize)){
			if(xCount > oCount){
				utility = 1;
			}
			else if (xCount < oCount){
				utility = 0;
			}
			else {
				utility = 0.5;
			}
		}
		// if both players pass, end game
		if(passCount == 2){
			if(xCount > oCount){

				utility = 1;
			}
			else if (xCount < oCount){

				utility = 0;
			}
			else {

				utility = 0.5;
			}
		}
	}
	
	public String getPlayerToMove(){
		return playerToMove;
	}
	
	public void mark(int col, int row) {
		//Check there is a possible move for current player
		if(col == -22 && row == -22){
			System.out.println("No possible moves for " + playerToMove);
			
			if(playerToMove == X){
				playerToMove = O;
				if(xPassCount == 0){
					passCount ++;
					xPassCount++;
				}
			}
			else {	
				playerToMove = X;
				if(oPassCount == 0){
				
					passCount ++;
					oPassCount++;
				}			
			}
			analyzeUtility();
			System.out.println(passCount);

		}
		else if (utility == -1 && getValue(col, row) == EMPTY) {
			// if a move is selects, move it and remove it from original location
			if(pRow == -3 && pCol == -3){
				board[getAbsPosition(col, row)] = playerToMove;
				if(playerToMove == X){
					xCount++;
				}
				else {
					oCount++;
				}
			}
			else{
				board[getAbsPosition(col, row)] = playerToMove;
				board[getAbsPosition(pCol, pRow)] = EMPTY;
			}

			if(playerToMove == X){
				playerToMove = O;
			}
			else {
				playerToMove = X;
			}
			analyzeUtility();
		}

	}
	// mark the XYLocation action
	public void mark(XYLocation action) {
		if(action != null){
			pCol = action.getPCol();
			pRow = action.getPRow();
			mark(action.getXCoOrdinate(), action.getYCoOrdinate());
		}
	}
	// return all the possible moves for current position
	public List<XYLocation> getUnMarkedPositions() {
		List<XYLocation> result = new ArrayList<XYLocation>();
		boolean empty = true;
		for (int col = 0; col < columnSize; col++) {
			for (int row = 0; row < rowSize; row++) {
				if (board[getAbsPosition(col, row)] == getPlayerToMove()){
					for (int x = col - 1 ; x <= col + 1; x++) {
						for (int y = row - 1 ; y <= row + 1; y++) {
							if (inBounds(x, y)){ // Check if the input is inBounds
								if(isEmpty(x, y)) { // Check if the spot is empty
									result.add(new XYLocation(x, y));	
									empty = false;
								}
							}
						}
					}
					// Move pieces 2 space
					for (int x = col - 2 ; x <= col + 2; x+=2) {
						for (int y = row - 2 ; y <= row + 2; y+=2) {
							if( inBounds(x,y) && isEmpty(x,y)){
								empty = false;
								XYLocation loc = new XYLocation(x, y);
								loc.setPCol(col); // Add the original location of col
								loc.setPRow(row); // Add the original location of row
								result.add(loc); // Add piece to move 2 spaces
							}
						}
					}
				}
			}
		}
		if(empty){
			result.add(new XYLocation(-22, -22));
		}
		return result;
	}
	public boolean inBounds(int col, int row) {
		if (row >= 0 && col >= 0 && row < rowSize && col < columnSize) {
			return true;
		}
		else {
			return false;
		}
	} // inBounds
	// return the number of empty spots
	public int getNumberOfMarkedPositions() {
		int retVal = 0;
		for (int col = 0; col < columnSize; col++) {
			for (int row = 0; row < rowSize; row++) {
				if (!(isEmpty(col, row))) {
					retVal++;
				}
			}
		}
		return retVal;
	}

	public double getUtility() {
		// TODO Auto-generated method stub
		return utility;
	}

	public boolean isEmpty(int col, int row) {
		return board[getAbsPosition(col, row)] == EMPTY;
	}
	@Override
	public BlobsState clone() {
		
		BlobsState copy = null;
		try {
			copy = (BlobsState) super.clone();
			copy.board = Arrays.copyOf(board, board.length);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace(); 
		}
		return copy;
	}

	@Override
	public boolean equals(Object anObj) {
		if (anObj != null && anObj.getClass() == getClass()) {
			BlobsState anotherState = (BlobsState) anObj;
			for (int i = 0; i < (rowSize*columnSize); i++) {
				if (board[i] != anotherState.board[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		// Need to ensure equal objects have equivalent hashcodes (Issue 77).
		return toString().hashCode();
	}

	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < columnSize; col++) {
				strBuilder.append(getValue(col, row) + " ");
			}
			strBuilder.append("\n");
		}
		return strBuilder.toString();
	}

	//
	// PRIVATE METHODS
	//

	private String getValue(int col, int row) {
		// TODO Auto-generated method stub
		return board[getAbsPosition(col, row)];
	}

	private int getAbsPosition(int col, int row) {
		return row * columnSize + col;
	}
	//Prints winner
	public void printWinner() {
		// TODO Auto-generated method stub
		if(xCount > oCount){
			System.out.println("******************");
			System.out.println("X is the winner!");
			System.out.println("Number of X's: " + xCount);
			System.out.println("Number of O's: " + oCount);
			System.out.println("******************");

		}
		else if (xCount < oCount){
			System.out.println("******************");
			System.out.println("O is the winner!");
			System.out.println("Number of X's: " + xCount);
			System.out.println("Number of O's: " + oCount);			
			System.out.println("******************");

		}
		else {
			System.out.println("******************");
			System.out.println("It's a tie game!");
			System.out.println("Number of X's: " + xCount);
			System.out.println("Number of O's: " + oCount);
			System.out.println("******************");
		}
	}
}
