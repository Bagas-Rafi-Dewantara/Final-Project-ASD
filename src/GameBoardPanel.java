/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #11
 * 1 - 5026231018 - Izzudin Hamadi Faiz
 * 2 - 5026231091 - Bagas Rafi Dewantara
 * 3 - 5026231116 - I Putu Febryan Khrisyantara
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

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

    private Timer timer;
    private int totalSeconds;
    private JLabel timerLabel;
    private JPanel timerPanel = new JPanel();
    private JPanel pointsPanel = new JPanel();
    private JPanel sudokuGrid = new JPanel();
    private JLabel pointsLabel;
    private int points;



    /** Constructor */
    public GameBoardPanel() {

        super.setLayout(new BorderLayout());

        super.add(timerPanel, BorderLayout.NORTH);
        super.add(pointsPanel, BorderLayout.SOUTH);
        super.add(sudokuGrid, BorderLayout.CENTER);

        sudokuGrid.setLayout(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE));



        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col);
                sudokuGrid.add(cells[row][col]);

                // Tambahkan border khusus untuk pemisah kotak 3x3
                Border thickBorderVertical = BorderFactory.createMatteBorder(0, 2, 0, 0, Color.BLACK);
                Border thickBorderHorizontal = BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK);


                if (col > 0 && col % 3 == 0) {
                    cells[row][col].setBorder(BorderFactory.createCompoundBorder(
                            thickBorderVertical,
                            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)
                    ));
                }


                if (row > 0 && row % 3 == 0) {
                    cells[row][col].setBorder(BorderFactory.createCompoundBorder(
                            thickBorderHorizontal,
                            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)
                    ));
                }


                // Untuk sel di pojok kiri atas setiap kotak 3x3
                if ((col > 0 && col % 3 == 0) && (row > 0 && row % 3 == 0)) {
                    cells[row][col].setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(2, 2, 0, 0, Color.BLACK),
                            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)
                    ));
                }

                sudokuGrid.add(cells[row][col]);


            }


        }

        points = 0;
        pointsLabel = new JLabel("Points: 0");
        pointsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        pointsPanel.add(pointsLabel);

        totalSeconds = 0;
        timerLabel = new JLabel("Timer: 0s");
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        timerPanel.add(timerLabel);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                totalSeconds++;
                updateTimerLabel();
            }
        });
        timer.start(); // Start the timer



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
        resetTimer();
        startTimer();

    }

    /**
     * Generate a new puzzle with default difficulty (Easy)
     */
    public void newGame() {
        newGame(36); // Default to Easy mode
        startTimer();

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
        resetTimer();

        startTimer();

    }

    /**
     * Check for conflicts in the row, column, and sub-grid of the given cell.
     * Highlights conflicting cells if any conflict is detected.
     */
    public void validateConflicts(int row, int col) {
        // Bersihkan highlight konflik terkait angka sebelumnya
        int oldValue = cells[row][col].getPreviousValue();
        if (oldValue != 0) {
            for (int r = 0; r < SudokuConstants.GRID_SIZE; ++r) {
                for (int c = 0; c < SudokuConstants.GRID_SIZE; ++c) {
                    if (cells[r][c].getText().equals(String.valueOf(oldValue))) {
                        cells[r][c].setBackground(Cell.BG_GIVEN); // Reset warna ke default
                    }
                }
            }
        }

        // Periksa konflik untuk angka baru
        int newValue = cells[row][col].getText().isEmpty() ? 0 : Integer.parseInt(cells[row][col].getText());
        if (newValue == 0) return; // Tidak perlu memeriksa sel kosong

        boolean conflictFound = false;

        // Periksa konflik pada baris
        for (int c = 0; c < SudokuConstants.GRID_SIZE; ++c) {
            if (c != col && cells[row][c].getText().equals(String.valueOf(newValue))) {
                cells[row][c].setBackground(Cell.BG_CONFLICT);
                cells[row][col].setBackground(Cell.BG_CONFLICT);
                conflictFound = true;
            }
        }

        // Periksa konflik pada kolom
        for (int r = 0; r < SudokuConstants.GRID_SIZE; ++r) {
            if (r != row && cells[r][col].getText().equals(String.valueOf(newValue))) {
                cells[r][col].setBackground(Cell.BG_CONFLICT);
                cells[row][col].setBackground(Cell.BG_CONFLICT);
                conflictFound = true;
            }
        }

        // Periksa konflik pada subgrid
        int startRow = (row / SudokuConstants.SUBGRID_SIZE) * SudokuConstants.SUBGRID_SIZE;
        int startCol = (col / SudokuConstants.SUBGRID_SIZE) * SudokuConstants.SUBGRID_SIZE;
        for (int r = startRow; r < startRow + SudokuConstants.SUBGRID_SIZE; ++r) {
            for (int c = startCol; c < startCol + SudokuConstants.SUBGRID_SIZE; ++c) {
                if ((r != row || c != col) && cells[r][c].getText().equals(String.valueOf(newValue))) {
                    cells[r][c].setBackground(Cell.BG_CONFLICT);
                    cells[row][col].setBackground(Cell.BG_CONFLICT);
                    conflictFound = true;
                }
            }
        }

        // Update status berdasarkan konflik
        if (conflictFound) {
            cells[row][col].status = CellStatus.WRONG_GUESS;

        } else {
            cells[row][col].status = CellStatus.CORRECT_GUESS;
        }
    }

    private void clearPreviousConflicts(int oldValue, int row, int col) {
        if (oldValue == 0) return; // Tidak ada konflik jika angka sebelumnya adalah 0 (kosong)

        // Bersihkan highlight pada baris
        for (int c = 0; c < SudokuConstants.GRID_SIZE; ++c) {
            if (cells[row][c].getText().equals(String.valueOf(oldValue))) {
                cells[row][c].setBackground(Cell.BG_GIVEN);
            }
        }

        // Bersihkan highlight pada kolom
        for (int r = 0; r < SudokuConstants.GRID_SIZE; ++r) {
            if (cells[r][col].getText().equals(String.valueOf(oldValue))) {
                cells[r][col].setBackground(Cell.BG_GIVEN);
            }
        }

        // Bersihkan highlight pada subgrid
        int startRow = (row / SudokuConstants.SUBGRID_SIZE) * SudokuConstants.SUBGRID_SIZE;
        int startCol = (col / SudokuConstants.SUBGRID_SIZE) * SudokuConstants.SUBGRID_SIZE;
        for (int r = startRow; r < startRow + SudokuConstants.SUBGRID_SIZE; ++r) {
            for (int c = startCol; c < startCol + SudokuConstants.SUBGRID_SIZE; ++c) {
                if (cells[r][c].getText().equals(String.valueOf(oldValue))) {
                    cells[r][c].setBackground(Cell.BG_GIVEN);
                }
            }
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
                // Simpan angka sebelumnya sebelum mengubah teks sel
                int oldValue = sourceCell.getPreviousValue();
                sourceCell.setPreviousValue(sourceCell.getText().isEmpty() ? 0 : Integer.parseInt(sourceCell.getText()));

                // Hapus highlight konflik angka sebelumnya
                clearPreviousConflicts(oldValue, sourceCell.row, sourceCell.col);

                // Validasi konflik setelah input diubah
                validateConflicts(sourceCell.row, sourceCell.col);

                if (sourceCell.status == CellStatus.CORRECT_GUESS) {
                    points++;
                    SoundPlayer.playSound("src/correct.wav ");
                    sourceCell.paint();

                } else if (sourceCell.status == CellStatus.WRONG_GUESS) {
                    points--;
                    SoundPlayer.playSound("src/wrong.wav");
                    sourceCell.paint();

                }
                updatePointsLabel();




                // Check if the puzzle is solved
                if (isSolved()) {
                    SoundPlayer.playSound("src/win.wav");
                    JOptionPane.showMessageDialog(null, "Congratulations! You solved the puzzle!");

                }
            } catch (NumberFormatException ex) {
                SoundPlayer.playSound("src/wrong.wav");
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
    public void resetTimer() {
        timer.stop();
        totalSeconds = 0; // Reset total detik ke 0
        updateTimerLabel(); // Perbarui tampilan timer
    }
    public void startTimer() {
        timer.start();
    }

    public void stopTimer() {
        timer.stop();
    }

    public void updateTimerLabel() {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        timerLabel.setText(String.format("Timer: %02d:%02d:%02d", hours, minutes, seconds));
    }

    private void updatePointsLabel() {
        pointsLabel.setText("Points: " + points);
    }




}