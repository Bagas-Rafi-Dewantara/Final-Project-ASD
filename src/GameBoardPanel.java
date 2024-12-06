import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // Define named constants for UI sizes
    private String playerName;
    private boolean gameFinished = false;

    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static int screenHeight = screenSize.height;
    int screenWidth = screenSize.width;

    // Define named constants for UI sizes
    public static int CELL_SIZE = screenHeight/14;   // Cell width/height in pixels
    public static final int BOARD_WIDTH  = CELL_SIZE * SudokuConstants.GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;
    // Board width/height in pixels

    // Define properties
    /** The game board composes of 9x9 Cells (customized JTextFields) */
    private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    /** It also contains a Puzzle with array numbers and isGiven */
    private Puzzle puzzle = new Puzzle();
    JPanel gridsudoku = new JPanel();
    JComboBox<String> difficultyComboBox;
    private int totalScore;

    private JLabel scoreLabel = new JLabel("Total Score: 0");

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /** Constructor */
    public GameBoardPanel() {
        super.setLayout(new BorderLayout());
        super.add(gridsudoku, BorderLayout.CENTER);
        gridsudoku.setPreferredSize(new Dimension(BOARD_WIDTH,BOARD_HEIGHT));
        gridsudoku.setBorder(new LineBorder(new Color(190, 208, 238),15));
        gridsudoku.setLayout(new GridLayout(SudokuConstants.SUBGRID_SIZE, SudokuConstants.SUBGRID_SIZE));

        // Allocate the 2D array of Cell, and added into JPanel.
        for (int row = 0; row < SudokuConstants.SUBGRID_SIZE; row++) {
            for (int col = 0; col < SudokuConstants.SUBGRID_SIZE; col++) {
                JPanel subgridpanel = new JPanel();
                subgridpanel.setLayout(new GridLayout(SudokuConstants.SUBGRID_SIZE, SudokuConstants.SUBGRID_SIZE));
                subgridpanel.setBorder(new LineBorder(new Color(251, 217, 216),2));
                for (int i=0; i < SudokuConstants.SUBGRID_SIZE; i++) {
                    for (int j=0; j < SudokuConstants.SUBGRID_SIZE; j++) {
                        cells[row*3+i][col*3+j] = new Cell(row*3+i, col*3+j);
                        subgridpanel.add(cells[row*3+i][col*3+j]);
                    }
                }
                gridsudoku.add(subgridpanel);
            }
        }

        // [TODO 3] Allocate a common listener as the ActionEvent listener for all the
        //  Cells (JTextFields)
        CellInputListener listener = new CellInputListener();

        // [TODO 4] Adds this common listener to all editable cells
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].isEditable()) {
                    cells[row][col].addActionListener(listener);   // For all editable rows and cols
                }
            }
        }
        super.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    }

    /**
     * Generate a new puzzle; and reset the game board of cells based on the puzzle.
     * You can call this method to start a new game.
     * @param cellsToGuess number of cells to guess (controls difficulty)
     */
    public void newGame(int cellsToGuess) {
        // Generate a new puzzle
        puzzle.newPuzzle(cellsToGuess);

        // Initialize all the 9x9 cells, based on the puzzle.
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].newGame(puzzle.numbers[row][col], puzzle.isGiven[row][col]);
            }
        }
    }

    /**
     * Generate a new puzzle with default difficulty (Easy)
     */
    public void newGame() {
        newGame(36); // Default to Easy mode
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

    public void resetGame() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                boolean isGiven = puzzle.isGiven[row][col];
                int number = puzzle.numbers[row][col];
                cells[row][col].newGame(number, isGiven); // Reset cell
            }
        }
    }

    /**
     * Check for conflicts in the row, column, and sub-grid of the given cell.
     * Highlights conflicting cells if any conflict is detected.
     */
    public void validateConflicts(int row, int col) {
        // Clear all conflicts first
        for (int r = 0; r < SudokuConstants.GRID_SIZE; ++r) {
            for (int c = 0; c < SudokuConstants.GRID_SIZE; ++c) {
                if (cells[r][c].status == CellStatus.WRONG_GUESS) {
                    cells[r][c].paint(); // Revert the conflict background
                }
            }
        }

        // Get the value of the cell to validate
        int value = cells[row][col].getText().isEmpty() ? 0 : Integer.parseInt(cells[row][col].getText());
        if (value == 0) return; // No need to check empty cells

        // Check for conflicts in the row, column, and sub-grid
        boolean conflictFound = false;

        // Row check
        for (int c = 0; c < SudokuConstants.GRID_SIZE; ++c) {
            if (c != col && cells[row][c].getText().equals(String.valueOf(value))) {
                cells[row][c].setBackground(Cell.BG_CONFLICT);
                cells[row][col].setBackground(Cell.BG_CONFLICT);
                conflictFound = true;
            }
        }

        // Column check
        for (int r = 0; r < SudokuConstants.GRID_SIZE; ++r) {
            if (r != row && cells[r][col].getText().equals(String.valueOf(value))) {
                cells[r][col].setBackground(Cell.BG_CONFLICT);
                cells[row][col].setBackground(Cell.BG_CONFLICT);
                conflictFound = true;
            }
        }

        // Sub-grid check
        int startRow = (row / SudokuConstants.SUBGRID_SIZE) * SudokuConstants.SUBGRID_SIZE;
        int startCol = (col / SudokuConstants.SUBGRID_SIZE) * SudokuConstants.SUBGRID_SIZE;
        for (int r = startRow; r < startRow + SudokuConstants.SUBGRID_SIZE; ++r) {
            for (int c = startCol; c < startCol + SudokuConstants.SUBGRID_SIZE; ++c) {
                if ((r != row || c != col) && cells[r][c].getText().equals(String.valueOf(value))) {
                    cells[r][c].setBackground(Cell.BG_CONFLICT);
                    cells[row][col].setBackground(Cell.BG_CONFLICT);
                    conflictFound = true;
                }
            }
        }

        // If conflict is found, update cell status
        if (conflictFound) {
            cells[row][col].status = CellStatus.WRONG_GUESS;
        } else {
            cells[row][col].status = CellStatus.CORRECT_GUESS;
        }
    }




    // [TODO 2] Define a Listener Inner Class for all the editable Cells
    private class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get a reference of the JTextField that triggers this action event
            Cell sourceCell = (Cell)e.getSource();

            try {
                int numberIn = Integer.parseInt(sourceCell.getText());
                System.out.println("You entered " + numberIn);

                if (numberIn < 1 || numberIn > 9) {
                    throw new NumberFormatException();
                }

                // Validate conflicts in the row, column, and sub-grid
                validateConflicts(sourceCell.row, sourceCell.col);

                // Check if the puzzle is solved
                if (isSolved()) {
                    JOptionPane.showMessageDialog(null, "Congratulations! You solved the puzzle!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number between 1 and 9", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                sourceCell.setText(""); // Clear invalid input
            }
            /*
             * [TODO 6] (later)
             * Check if the player has solved the puzzle after this move,
             *   by calling isSolved(). Put up a congratulation JOptionPane, if so.
             */
            if(isSolved()) JOptionPane.showMessageDialog(null, "Congratulation!");
        }
    }

    public void keyTyped(KeyEvent e) {
        Cell sourceCell = (Cell) e.getSource();
        String text = sourceCell.getText() + e.getKeyChar();
        if (text.length() > 1) {
            e.consume();
            return;
        }
        try {
            int numberIn = Integer.parseInt(text);
            if (numberIn < 1 || numberIn > 9) {
                throw new NumberFormatException();
            } validateConflicts(sourceCell.row, sourceCell.col);
            if (isSolved()) {
                JOptionPane.showMessageDialog(null, "Congratulations! You solved the puzzle!");
            }
        } catch (NumberFormatException ex) {
            e.consume(); JOptionPane.showMessageDialog(null, "Please enter a valid number between 1 and 9", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

}