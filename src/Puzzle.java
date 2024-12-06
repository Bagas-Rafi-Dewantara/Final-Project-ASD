/**
 * The Sudoku number puzzle to be solved
 */
public class Puzzle {
    // All variables have package access
    // The numbers on the puzzle
    int[][] numbers = new int[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    // The clues - isGiven (no need to guess) or need to guess
    boolean[][] isGiven = new boolean[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];

    // Constructor
    public Puzzle() {
        super();
    }

    // Generate a new puzzle given the number of cells to be guessed, which can be used
    //  to control the difficulty level.
    // This method shall set (or update) the arrays numbers and isGiven
    public void newPuzzle(int cellsToGuess) {
        // The solution/answer to the Sudoku puzzle
        int[][] solution = {
                {5, 3, 4, 6, 7, 8, 9, 1, 2},
                {6, 7, 2, 1, 9, 5, 3, 4, 8},
                {1, 9, 8, 3, 4, 2, 5, 6, 7},
                {8, 5, 9, 7, 6, 1, 4, 2, 3},
                {4, 2, 6, 8, 5, 3, 7, 9, 1},
                {7, 1, 3, 9, 2, 4, 8, 5, 6},
                {9, 6, 1, 5, 3, 7, 2, 8, 4},
                {2, 8, 7, 4, 1, 9, 6, 3, 5},
                {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };

        // Copy the solution into the numbers array
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                numbers[row][col] = solution[row][col];
            }
        }

        // Initialize all cells as given
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                isGiven[row][col] = true;
            }
        }

        // Set difficulty levels based on cellsToGuess parameter
        // Easy: 45-50 given numbers (31-36 empty)
        // Medium: 36-44 given numbers (37-45 empty)
        // Hard: 32-35 given numbers (46-49 empty)
        boolean[][] difficultyPatterns;

        if (cellsToGuess <= 36) { // Easy
            difficultyPatterns = new boolean[][] {
                    {true,  true,  true,  true,  true,  true,  false, true,  true},
                    {true,  false, true,  true,  false, true,  true,  true,  true},
                    {true,  true,  false, true,  true,  true,  true,  false, true},
                    {true,  true,  true,  false, true,  true,  true,  true,  false},
                    {false, true,  true,  true,  true,  true,  false, true,  true},
                    {true,  true,  false, true,  true,  false, true,  true,  true},
                    {true,  false, true,  true,  true,  true,  true,  true,  false},
                    {true,  true,  true,  false, true,  true,  false, true,  true},
                    {false, true,  true,  true,  false, true,  true,  true,  true}
            };
        } else if (cellsToGuess <= 45) { // Medium
            difficultyPatterns = new boolean[][] {
                    {true,  false, true,  false, true,  false, true,  false, true},
                    {false, true,  false, true,  false, true,  false, true,  false},
                    {true,  false, true,  false, true,  false, true,  false, true},
                    {false, true,  false, true,  false, true,  false, true,  false},
                    {true,  false, true,  false, true,  false, true,  false, true},
                    {false, true,  false, true,  false, true,  false, true,  false},
                    {true,  false, true,  false, true,  false, true,  false, true},
                    {false, true,  false, true,  false, true,  false, true,  false},
                    {true,  false, true,  false, true,  false, true,  false, true}
            };
        } else { // Hard
            difficultyPatterns = new boolean[][] {
                    {false, true,  false, false, true,  false, false, true,  false},
                    {true,  false, false, true,  false, false, true,  false, false},
                    {false, false, true,  false, false, true,  false, false, true},
                    {false, true,  false, false, true,  false, false, true,  false},
                    {true,  false, false, true,  false, false, true,  false, false},
                    {false, false, true,  false, false, true,  false, false, true},
                    {false, true,  false, false, true,  false, false, true,  false},
                    {true,  false, false, true,  false, false, true,  false, false},
                    {false, false, true,  false, false, true,  false, false, true}
            };
        }

        // Apply the difficulty pattern
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                isGiven[row][col] = difficultyPatterns[row][col];
            }
        }
    }

    // Get the solution for the current puzzle
    public int[][] getSolution() {
        return new int[][] {
                {5, 3, 4, 6, 7, 8, 9, 1, 2},
                {6, 7, 2, 1, 9, 5, 3, 4, 8},
                {1, 9, 8, 3, 4, 2, 5, 6, 7},
                {8, 5, 9, 7, 6, 1, 4, 2, 3},
                {4, 2, 6, 8, 5, 3, 7, 9, 1},
                {7, 1, 3, 9, 2, 4, 8, 5, 6},
                {9, 6, 1, 5, 3, 7, 2, 8, 4},
                {2, 8, 7, 4, 1, 9, 6, 3, 5},
                {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };
    }

    //(For advanced students) use singleton design pattern for this class
}