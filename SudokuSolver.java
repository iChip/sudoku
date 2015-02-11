package sudoku;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Place for your code.
 */
public class SudokuSolver {

	public static int BOARD_SIZE = 9;
	public static int TIMESREDUCED = 0;

	/**
	 * @return names of the authors and their student IDs (1 per line).
	 */
	public String authors() {
		/*
		Name (last, first):  Chipperfield, Ian
		Student ID: 94393071
		Partner’s Name (last, first): Jeffery, Jon
		Student ID: 26434126
		*/
		return "Ian Chipperfield 94393071\nJon Jeffery 26434126";
	}

	/**
	 * Performs constraint satisfaction on the given Sudoku board using Arc Consistency and Domain Splitting.
	 * 
	 * @param board the 2d int array representing the Sudoku board. Zeros indicate unfilled cells.
	 * @return the solved Sudoku board
	 */
	public int[][] solve( int[][] board ) {
		Domains start = new Domains( board );
		checkArcConsistency(start);
		System.out.println(TIMESREDUCED);



		return start.toBoard();
		//return board;
	}

	// reduce a domain and run arc consistency
	public void successor(){
		// check if current domain is empty
		// check if domain is unary
		// if multiple values, split
	}

	public void checkArcConsistency( Domains grid ) {
		for (int i = 0; i< BOARD_SIZE; i++){
			for (int j = 0; j< BOARD_SIZE; j++){
				checkConsistency( grid, i, j );
			}
		}
		//returns consistent input
		// reduce all domains to empty if
	}

	public void checkConsistency( Domains grid, int i, int j ){
		// check row values
		checkRow( grid, i, j );
		// check column values
		checkColumn( grid, i, j );
		// check sub-square values
		checkSubSquare( grid, i, j );
	}

	public void checkRow( Domains grid, int i, int j ){
		for ( int k = 0; k < BOARD_SIZE; k++ ){
			if ( k != j ) {
				if ( grid.grid[i][k].size() == 1 ) {
					int toRemove = grid.grid[i][k].getFirst();
					if ( grid.grid[i][j].contains( toRemove ) ){
						grid.grid[i][j].removeFirstOccurrence( toRemove );
						TIMESREDUCED++;
					}
				}
			}
		}
	}

	public void checkColumn( Domains grid, int i, int j ){
		for ( int k = 0; k < BOARD_SIZE; k++ ){
			if ( k != i ) {
				if ( grid.grid[k][j].size() == 1 ) {
					int toRemove = grid.grid[k][j].getFirst();
					if ( grid.grid[i][j].contains( toRemove ) ){
						grid.grid[i][j].removeFirstOccurrence(toRemove);
						TIMESREDUCED++;

					}
				}
			}
		}
	}

	public void checkSubSquare( Domains domain, int row, int col ){

		int rowGroup = row / 3;
		int colGroup = col / 3;

		int rowGrpStart = rowGroup * 3;
		int colGrpStart = colGroup * 3;

		for ( int i = rowGrpStart; i < (rowGrpStart + 3); i++ ){
			for ( int j = colGrpStart; j < (colGrpStart + 3); j++ ){
				if ( !( i == row && j == col ) ){
					if ( domain.grid[i][j].size() == 1 ){
						int toRemove = domain.grid[i][j].getFirst();
						domain.grid[row][col].removeFirstOccurrence( toRemove );
						TIMESREDUCED++;

					}
				}
			}
		}
	}

	public class Domains {
		LinkedList<Integer>[][] grid = ( LinkedList<Integer>[][] ) Array.newInstance( LinkedList.class, BOARD_SIZE, BOARD_SIZE );

		public Domains(int[][] board) {
			for (int i = 0; i < BOARD_SIZE; i++){
				for (int j = 0; j < BOARD_SIZE; j++){
					if (board[i][j] == 0){
						grid[i][j] = new LinkedList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
					} else {
						grid[i][j] = new LinkedList<Integer>(Arrays.asList(board[i][j]));
					}
				}
			}
		}

		public int[][] toBoard(){
			int[][] result = new int[BOARD_SIZE][BOARD_SIZE];
			for (int i = 0; i < BOARD_SIZE; i++){
				for (int j = 0; j < BOARD_SIZE; j++){
					result[i][j] = grid[i][j].getFirst();
				}
			}
			return result;
		}
	}
}
