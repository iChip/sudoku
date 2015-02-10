package sudoku;

/**
 * Place for your code.
 */
public class SudokuSolver {

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
	public int[][] solve(int[][] board) {
		// TODO write it;
		return board;
	}

	// Successor Function:
	// reduce a domain and run arc consistency
	public void successor(){

	}

	public void checkArcConsistency() {
		/*
		Procedure GAC(V,dom,C)
			Inputs
				V: a set of variables
				dom: a function such that dom(X) is the domain of variable X
				C: set of constraints to be satisfied
			Output
				arc-consistent domains for each variable
			Local
				DX is a set of values for each variable X
				TDA is a set of arcs

			for each variable X do DX ←dom(X)
				TDA ←{〈X,c〉| X ∈ V, c ∈ C and X ∈ scope(c)}

			while (TDA != {})
				select 〈X,c〉 ∈TDA
				TDA ←TDA \ {〈X,c〉}
				NDX ←{x|x∈DX and for some y∈DY s.t.(x,y) satisfies c}
				if (NDX != DX) then
					TDA ←TDA ∪ { 〈Z,c'〉 | X ∈ scope(c'), c' != c, Z ∈ scope(c') \ {X} }
					DX ←NDX

		return {DX| X is a variable}
		*/

	}
}
