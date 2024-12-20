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
import javax.swing.*;
/**
 * The main Sudoku program
 */
public class Sudoku extends JFrame {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // private variables
    GameBoardPanel board = new GameBoardPanel();
    JButton btnNewGame = new JButton("New Game");
    // Tambahkan di dalam kelas Sudoku
    JButton btnResetGame = new JButton("Reset Game"); // Tombol untuk Reset Game

    JComboBox<String> levelSelector;
    JPanel buttonPanel = new JPanel();

    // Constructor
    public Sudoku() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        cp.add(board, BorderLayout.CENTER);

        // Create level selector
        String[] levels = {"Easy", "Medium", "Hard"};
        levelSelector = new JComboBox<>(levels);

        // Add action listener to level selector for automatic game start
        levelSelector.addActionListener(e -> startNewGame());

        // Add components to button panel
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(new JLabel("Difficulty: "));
        buttonPanel.add(levelSelector);

        buttonPanel.add(btnResetGame);

        // Add button panel to the south
        cp.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listener for Reset Game button

        btnResetGame.addActionListener(e -> board.resetGame());

        // Initialize the game board to start the game
        startNewGame();
//        board.newGame();

        pack();     // Pack the UI components, instead of using setSize()
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // to handle window-closing
        setTitle("Sudoku");
        setVisible(true);
    }

    // Start a new game based on selected difficulty
    private void startNewGame() {
        String level = (String) levelSelector.getSelectedItem();
        int cellsToFill;


        switch (level) {
            case "Easy":
                cellsToFill = 71;
                break;
            case "Medium":
                cellsToFill = 61;
                break;
            case "Hard":
                cellsToFill = 51;
                break;
            default:
                cellsToFill = 71; // Default to Easy
        }

        board.newGame(cellsToFill);

    }


    /** The entry main() entry method */
    public static void play() {
        // Run the game in the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> new Sudoku());
    }
}