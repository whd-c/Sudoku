import java.util.ArrayList;
import java.util.Collections;

public class Grid {
    public Grid(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Grid size can't be smaller than 1.");
        } else if (n > 26) {
            throw new IllegalArgumentException("Grid size can't be larger than 26.");
        }
        int boxSize = (int) Math.sqrt(n);
        if (boxSize * boxSize != n) {
            throw new IllegalArgumentException("Grid size must be a perfect square");
        }

        this.gridSize = n;
        this.grid = new int[n][n];
        this.solvedGrid = new int[n][n];
    }

    private int gridSize;
    private int[][] grid;
    private int[][] solvedGrid;

    public void generate(int cellsToRemove) {
        clearBoard();
        fillGrid(0, 0);
        for (int i = 0; i < gridSize; i++) {
            System.arraycopy(solvedGrid[i], 0, grid[i], 0, gridSize);
        }
        removeRandomNumbers(cellsToRemove);
    }

    private void clearBoard() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                solvedGrid[i][j] = 0;
                grid[i][j] = 0;
            }
        }
    }

    private boolean fillGrid(int row, int col) {
        if (row == gridSize - 1 && col == gridSize) return true;
        if (col == gridSize) {
            row++;
            col = 0;
        }
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= gridSize; i++) numbers.add(i);
        Collections.shuffle(numbers);

        for (int num : numbers) {
            if (isValid(row, col, num)) {
                solvedGrid[row][col] = num;
                if (fillGrid(row, col + 1)) {
                    return true;
                }
                solvedGrid[row][col] = 0;
            }
        }
        return false;
    }

    private boolean isValid(int row, int col, int num) {
        for (int x = 0; x < gridSize; x++) {
            if (solvedGrid[row][x] == num || solvedGrid[x][col] == num) {
                return false;
            }
        }

        int boxSize = (int) Math.sqrt(gridSize);
        int boxRowStart = row - row % boxSize;
        int boxColStart = col - col % boxSize;
        for (int i = 0; i < boxSize; i++) {
            for (int j = 0; j < boxSize; j++) {
                if (solvedGrid[i + boxRowStart][j + boxColStart] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private void removeRandomNumbers(int count) {
        int removed = 0;
        while (removed < count) {
            int cellId = (int) (Math.random() * (gridSize * gridSize));
            int i = cellId / gridSize;
            int j = cellId % gridSize;
            if (grid[i][j] != 0) {
                grid[i][j] = 0;
                removed++;
            }
        }
    }

    public ArrayList<Vector2> getUnsolvedSquares() {
        var unsolvedSquares = new ArrayList<Vector2>();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (!isSolvedAt(i, j) && grid[i][j] != 0) {
                    unsolvedSquares.add(new Vector2(i, j));
                }
            }
        }
        return unsolvedSquares;
    }

    public boolean isSolved() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (!isSolvedAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isSolvedAt(int i, int j) {
        return grid[i][j] == solvedGrid[i][j];
    }

    public void insertAt(int i, int j, int val) {
        grid[i][j] = val;
    }

    public int getAt(int i, int j) {
        return grid[i][j];
    }

    public void removeAt(int i, int j) {
        grid[i][j] = 0;
    }

    public void printGrid() {
        System.out.print("\t");
        for (int i = 1; i <= gridSize; i++) {
            System.out.print(i + "    ");
        }
        System.out.println();
        for (int i = 0; i < gridSize; i++) {
            System.out.print((char) (i + 65) + " ");
            for (int j = 0; j < gridSize; j++) {
                System.out.print("[  ");
                if (grid[i][j] != 0)
                    System.out.print(grid[i][j]);
                if (j < 9)
                    System.out.print(" ]");
                else
                    System.out.print("  ]");
            }
            System.out.println();
        }
    }
}
