import java.util.ArrayList;

public class Grid {
    public Grid(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Grid size can't be smaller than 1.");
        }
        this.gridSize = n;
        this.grid = new int[n][n];
        this.solvedGrid = new int[n][n];
    }

    private int gridSize;
    private int[][] grid;
    private int[][] solvedGrid;

    public void generate() {

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
