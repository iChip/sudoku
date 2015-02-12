package sudoku;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Place for your code.
 */
public class SudokuSolver {

	public static int BOARD_SIZE = 9;
	public static int TOTAL_DOMAINS = BOARD_SIZE*BOARD_SIZE;
	public static int TIMESREDUCED = 0;

	LinkedList<Domains> solutions = new LinkedList<Domains>();
	LinkedList<Domains> toDo = new LinkedList<Domains>();

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
		checkArcConsistency( start );
		solutions.clear();
		toDo.clear();

		toDo.push( start );
		Domains current = null;

		while ( !toDo.isEmpty() ){
			current = toDo.peekFirst();

			// iterate through all variables in board
			for ( int i = 0; i < BOARD_SIZE; i++ ){
				for ( int j = 0; j < BOARD_SIZE; j++ ){
					successor( start, i , j );
				}
			}

		}
		return solutions.peekFirst().toBoard();
	}

	// reduce a domain and run arc consistency
	public void successor( Domains current, int row, int col ){

		// check if domain is unary
		if ( isSolution(current) ) {
			if ( toDo.size() > 0 ) toDo.pop();
			solutions.push(current);
			return;
		} else if ( hasInvalidDomain( current ) ) {
			toDo.pop();
			return;
		}

		// Variable Has Multiple Values
		else {
			Domains firstHalf = current;
			Domains secondHalf = current;
			int domainLength = current.grid[row][col].size();

			LinkedList<Integer> first = current.grid[row][col];
			for ( int i = 0; i < domainLength / 2; i++ ){
				first.removeLast();
			}

			LinkedList<Integer> second = current.grid[row][col];
			for ( int i = ( domainLength / 2 ); i < domainLength; i++ ){
				second.removeFirst();
			}

			firstHalf.grid[row][col] = first;
			secondHalf.grid[row][col] = second;

			checkArcConsistency( firstHalf );
			checkArcConsistency( secondHalf );

			toDo.push( firstHalf );
			toDo.push( secondHalf );
		}
	}

	public boolean isSolution( Domains current ){
		// check if all domains are unary
		boolean allUnary = true;
		for ( int i = 0; i < BOARD_SIZE; i++ ){
			for ( int j = 0; j< BOARD_SIZE; j++ ){
				if ( current.grid[i][j].size() != 1 ) allUnary = false;
			}
		}
		return allUnary;
	}

	public boolean hasInvalidDomain( Domains current ){
		boolean hasInvalidDomain = false;
		for ( int i = 0; i < BOARD_SIZE; i++ ){
			for ( int j = 0; j< BOARD_SIZE; j++ ){
				if (current.grid[i][j].size() == 0 ) hasInvalidDomain = true;
			}
		}
		return hasInvalidDomain;
	}

	public void checkArcConsistency( Domains grid ) {
		boolean done = false;

		while ( !done ) {
			int numberConsistentDomains = 0;
			boolean consistent = false;

			for ( int i = 0; i < BOARD_SIZE; i++ ){
				for ( int j = 0; j < BOARD_SIZE; j++ ){
					consistent = checkConsistency( grid, i, j );
					if ( consistent ) numberConsistentDomains++;
				}
			}
			if ( numberConsistentDomains == TOTAL_DOMAINS) done = true;
		}
		//returns consistent input
		// reduce all domains to empty if
	}

	// Return true if this variable's domain is consistent across all constraints
	public boolean checkConsistency( Domains grid, int i, int j ){
		// check row values
		boolean rowConsistent = checkRow( grid, i, j );
		// check column values
		boolean colConsistent = checkColumn( grid, i, j );
		// check sub-square values
		boolean subSquareConsistent = checkSubSquare( grid, i, j );

		return ( rowConsistent && colConsistent && subSquareConsistent );
	}

	// return true if row constraint is satisfied
	public boolean checkRow( Domains grid, int i, int j ){
		boolean consistent = true;
		for ( int k = 0; k < BOARD_SIZE; k++ ){
			if ( k != j ) {
				if ( grid.grid[i][k].size() == 1 ) {
					int toRemove = grid.grid[i][k].getFirst();
					if ( grid.grid[i][j].removeFirstOccurrence( toRemove ) ){
						TIMESREDUCED++;
						consistent = false;
					}
				}
			}
		}
		return consistent;
	}

	public boolean checkColumn( Domains grid, int i, int j ){
		boolean consistent = true;
		for ( int k = 0; k < BOARD_SIZE; k++ ){
			if ( k != i ) {
				if ( grid.grid[k][j].size() == 1 ) {
					int toRemove = grid.grid[k][j].getFirst();
					if ( grid.grid[i][j].removeFirstOccurrence(toRemove) ){
						TIMESREDUCED++;
						consistent = false;
					}
				}
			}
		}
		return consistent;
	}

	public boolean checkSubSquare( Domains domain, int row, int col ){

		boolean consistent = true;

		int rowGroup = row / 3;
		int colGroup = col / 3;

		int rowGrpStart = rowGroup * 3;
		int colGrpStart = colGroup * 3;

		for ( int i = rowGrpStart; i < (rowGrpStart + 3); i++ ){
			for ( int j = colGrpStart; j < (colGrpStart + 3); j++ ){
				if ( !( i == row && j == col ) ){
					if ( domain.grid[i][j].size() == 1 ){
						int toRemove = domain.grid[i][j].getFirst();
						if ( domain.grid[row][col].removeFirstOccurrence( toRemove ) ){
							TIMESREDUCED++;
							consistent = false;
						}
					}
				}
			}
		}
		return consistent;
	}

	public class Domains {
		LinkedList<Integer>[][] grid = ( LinkedList<Integer>[][] ) Array.newInstance( LinkedList.class, BOARD_SIZE, BOARD_SIZE );

		public Domains(int[][] board) {
			for (int i = 0; i < BOARD_SIZE; i++){
				for (int j = 0; j < BOARD_SIZE; j++){
					if (board[i][j] == 0){
						grid[i][j] = new LinkedList<Integer>( Arrays.asList( 1, 2, 3, 4, 5, 6, 7, 8, 9 ));
					} else {
						grid[i][j] = new LinkedList<Integer>( Arrays.asList( board[i][j] ) );
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
