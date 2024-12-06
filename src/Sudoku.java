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
    JButton btnResetGame = new JButton("Reset Game"); // Tombol untuk Reset Game


    // Constructor
    public Sudoku() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        cp.add(board, BorderLayout.CENTER);

        // Add a button to the south to re-start the game via board.newGame()
        // ......

        // Initialize the game board to start the game
        board.newGame();

        pack();     // Pack the UI components, instead of using setSize()
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // to handle window-closing
        setTitle("Sudoku");
        setVisible(true);

        // Panel bawah untuk tombol
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        southPanel.add(btnNewGame);
        southPanel.add(btnResetGame); // Tambahkan tombol Reset Game
        cp.add(southPanel, BorderLayout.SOUTH);

        // Tambahkan Action Listener untuk tombol Reset
        btnResetGame.addActionListener(e -> board.resetGame());

        // Tambahkan Action Listener untuk tombol New Game
        btnNewGame.addActionListener(e -> board.newGame());





        // Initialize the game board to start the game
        board.newGame();

        pack(); // Pack the UI components, instead of using setSize()
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Handle window-closing
        setTitle("Sudoku");
        setVisible(true);
    }


}