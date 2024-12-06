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
        int cellsToGuess;


        switch (level) {
            case "Easy":
                cellsToGuess = 36;
                break;
            case "Medium":
                cellsToGuess = 45;
                break;
            case "Hard":
                cellsToGuess = 49;
                break;
            default:
                cellsToGuess = 36; // Default to Easy
        }

        board.newGame(cellsToGuess);

    }


    /** The entry main() entry method */
    public static void play() {
        // Run the game in the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> new Sudoku());
    }
}