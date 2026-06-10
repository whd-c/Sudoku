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

        gridSize = n;
        grid = new int[n][n];
        solvedGrid = new int[n][n];
        startingGrid = new boolean[n][n];
    }

    private final int gridSize;
    private final int[][] grid;
    private final int[][] solvedGrid;
    private final boolean[][] startingGrid;

    public void generate(int cellsToRemove) {
        clearBoard();
        fillGrid(0, 0);
        for (int i = 0; i < gridSize; i++) {
            System.arraycopy(solvedGrid[i], 0, grid[i], 0, gridSize);
        }
        removeRandomNumbers(cellsToRemove);
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                startingGrid[i][j] = grid[i][j] != 0;
            }
        }
    }

    private void clearBoard() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                solvedGrid[i][j] = 0;
                grid[i][j] = 0;
                startingGrid[i][j] = false;
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
                    unsolvedSquares.add(new Vector2(j, i));
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

    public boolean isStartingSquare(int i, int j) {
        return startingGrid[i][j];
    }

    public void printGrid() {
        printStyledGrid(null);
    }

    public void printCheckedGrid() {
        var wrongSquares = getUnsolvedSquares();
        printStyledGrid(wrongSquares);
    }


    private void printStyledGrid(ArrayList<Vector2> highlightingSquares) {
        int boxSize = (int) Math.sqrt(gridSize);

        System.out.print("   ");
        for (int i = 0; i < gridSize; i++) {
            System.out.print(" " + Color.CYAN.apply(Integer.toString(i + 1)) + " ");
            if ((i + 1) % boxSize == 0 && i < gridSize - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();

        for (int i = 0; i < gridSize; i++) {
            if (i > 0 && i % boxSize == 0) {
                System.out.print("   ");
                for (int j = 0; j < gridSize; j++) {
                    System.out.print(Color.CYAN.apply("───"));
                    if ((j + 1) % boxSize == 0 && j < gridSize - 1) {
                        System.out.print(Color.CYAN.apply("┼"));
                    }
                }
                System.out.println();
            }

            System.out.print(Color.CYAN.apply(Character.toString((char) (i + 65))) + "  ");

            for (int j = 0; j < gridSize; j++) {
                int val = grid[i][j];

                if (val == 0) {
                    System.out.print(" . ");
                } else {
                    boolean isWrong = false;
                    if (highlightingSquares != null) {
                        for (Vector2 square : highlightingSquares) {
                            if (square.x() == j && square.y() == i) {
                                isWrong = true;
                                break;
                            }
                        }
                    }

                    if (isWrong) {
                        System.out.print(" " + Color.RED.apply(Integer.toString(val)) + " ");
                    } else {
                        System.out.print(" " + val + " ");
                    }
                }

                if ((j + 1) % boxSize == 0 && j < gridSize - 1) {
                    System.out.print(Color.CYAN.apply("│"));
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
