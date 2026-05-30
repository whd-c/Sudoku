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
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                System.out.print("[ ");
                if (grid[i][j] != 0)
                    System.out.print(grid[i][j]);
                System.out.print(" ]" + " ");
            }
            System.out.println();
        }
    }
}
