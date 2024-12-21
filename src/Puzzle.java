/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #11
 * 1 - 5026231018 - Izzudin Hamadi Faiz
 * 2 - 5026231091 - Bagas Rafi Dewantara
 * 3 - 5026231116 - I Putu Febryan Khrisyantara
 */

import java.util.Random;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Puzzle {
    int[][] numbers = new int[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    boolean[][] isGiven = new boolean[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    private static final int[][] baseSolution = {
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
    private Random rand = new Random();

    public Puzzle() {
        super();
    }

    public void newPuzzle(int cellsToFill) {
        generateRandomSolution();
        initializeIsGivenArray();
        applyDifficultyPattern(cellsToFill);
    }

    private void generateRandomSolution() {
        // Copy the baseSolution into numbers array and then shuffle it
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                numbers[row][col] = baseSolution[row][col];
            }
        }

        // Shuffle the rows and columns within each 3x3 subgrid to get a random solution
        for (int i = 0; i < 3; ++i) {
            shuffleSubgridRows(i * 3);
            shuffleSubgridCols(i * 3);
        }
    }

    private void shuffleSubgridRows(int baseRow) {
        List<Integer> rows = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            rows.add(baseRow + i);
        }
        Collections.shuffle(rows, rand);

        for (int i = 0; i < 3; ++i) {
            int row1 = rows.get(i);
            int row2 = rows.get((i + 1) % 3);
            if (row1 != row2) {
                for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                    int temp = numbers[row1][col];
                    numbers[row1][col] = numbers[row2][col];
                    numbers[row2][col] = temp;
                }
            }
        }
    }

    private void shuffleSubgridCols(int baseCol) {
        List<Integer> cols = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            cols.add(baseCol + i);
        }
        Collections.shuffle(cols, rand);

        for (int i = 0; i < 3; ++i) {
            int col1 = cols.get(i);
            int col2 = cols.get((i + 1) % 3);
            if (col1 != col2) {
                for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
                    int temp = numbers[row][col1];
                    numbers[row][col1] = numbers[row][col2];
                    numbers[row][col2] = temp;
                }
            }
        }
    }

    private void initializeIsGivenArray() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                isGiven[row][col] = true;
            }
        }
    }

    private void applyDifficultyPattern(int cellsToFill) {
        int cellsToGuess = SudokuConstants.GRID_SIZE * SudokuConstants.GRID_SIZE - cellsToFill;

        // Apply the random difficulty pattern
        while (cellsToGuess > 0) {
            int row = rand.nextInt(SudokuConstants.GRID_SIZE);
            int col = rand.nextInt(SudokuConstants.GRID_SIZE);
            if (isGiven[row][col]) {
                isGiven[row][col] = false;
                cellsToGuess--;
            }
        }
    }

    public int[][] getSolution() {
        return numbers;
    }
}
