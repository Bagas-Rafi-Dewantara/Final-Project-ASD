import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // Define named constants for UI sizes
    public static final int CELL_SIZE = 60;   // Cell width/height in pixels
    public static final int BOARD_WIDTH  = CELL_SIZE * SudokuConstants.GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;
    // Board width/height in pixels

    // Define properties
    /** The game board composes of 9x9 Cells (customized JTextFields) */
    private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    /** It also contains a Puzzle with array numbers and isGiven */
    private Puzzle puzzle = new Puzzle();

    /** Constructor */
    public GameBoardPanel() {
        super.setLayout(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE));  // JPanel

        // Allocate the 2D array of Cell, and added into JPanel.
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col);
                super.add(cells[row][col]);   // JPanel
            }
        }

        // [TODO 3] Allocate a common listener as the ActionEvent listener for all the
        //  Cells (JTextFields)
        // .........
        CellInputListener listener = new CellInputListener();

        // [TODO 4] Adds this common listener to all editable cells
        // .........
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].isEditable()) {
                    cells[row][col].addActionListener(listener); // For all editable rows and cols
                }
            }
        }

        super.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    }

    /**
     * Generate a new puzzle; and reset the game board of cells based on the puzzle.
     * You can call this method to start a new game.
     */
    public void newGame() {
        // Generate a new puzzle
        puzzle.newPuzzle(2);

        // Initialize all the 9x9 cells, based on the puzzle.
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].newGame(puzzle.numbers[row][col], puzzle.isGiven[row][col]);
            }
        }
    }

    /**
     * Return true if the puzzle is solved
     * i.e., none of the cell have status of TO_GUESS or WRONG_GUESS
     */
    public boolean isSolved() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].status == CellStatus.TO_GUESS || cells[row][col].status == CellStatus.WRONG_GUESS) {
                    return false;
                }
            }
        }
        return true;
    }

    // [TODO 2] Define a Listener Inner Class for all the editable Cells

    private class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get a reference to the JTextField that triggers this action event
            Cell sourceCell = (Cell) e.getSource();

            // Retrieve the int entered
            try {
                int numberIn = Integer.parseInt(sourceCell.getText());
                // For debugging
                System.out.println("You entered " + numberIn);
                // [TODO 5] Check the numberIn against sourceCell.number
                if (numberIn == sourceCell.number) {
                    sourceCell.status = CellStatus.CORRECT_GUESS;
                    sourceCell.setEditable(false); // Disable further editing
                    sourceCell.setBackground(new Color(173, 216, 230)); // Light blue for correct
                } else {
                    sourceCell.status = CellStatus.WRONG_GUESS;
                    sourceCell.setBackground(new Color(255, 182, 193)); // Light pink for incorrect
                }
                sourceCell.paint(); // Re-paint this cell based on its status
                // [TODO 6] Check if the puzzle is solved
                if (isSolved()) {
                    JOptionPane.showMessageDialog(null, "Congratulations! You solved the puzzle!");
                }


            }catch (NumberFormatException ex) {
                // Handle invalid input
                JOptionPane.showMessageDialog(null, "Please enter a valid number between 1 and 9", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                sourceCell.setText(""); // Clear invalid input
            }
        }
    }

    public void resetGame() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                boolean isGiven = puzzle.isGiven[row][col];
                int number = puzzle.numbers[row][col];
                cells[row][col].newGame(number, isGiven); // Reset cell
            }
        }
    }

}