import java.io.IOException;
import java.util.Scanner;

public class Game {

    public Game() {

        GRID_SIZE = 9;
        grid = new Grid(GRID_SIZE);
    }

    public void run() {
        boolean isPlaying = true;
        while (isPlaying) {
            printMainMenu();
            MenuOption menuOption = intToMenuOption(getMenuInput());
            switch (menuOption) {
                case PLAY:
                    playGameLoop();
                    break;
                case QUIT:
                    isPlaying = false;
                    break;
            }
        }
    }

    private void playGameLoop() {
        boolean inGame = true;
        while (inGame) {
            printGameMenu();
            GameOption gameOption = intToGameOption(getMenuInput());
            switch (gameOption) {
                case INSERT:
                    //parse
                    break;
                case ERASE:
                    //parse2
                    break;
                case CHECK_AT:
                    //parse3
                    break;
                case CHECK_GRID:
                    //color unsolved red
                    grid.isSolved();
                    break;
                case QUIT:
                    inGame = false;
                    break;
            }
        }
    }

    private enum MenuOption {
        PLAY,
        QUIT,
        ERROR,
    }

    private enum GameOption {
        INSERT,
        ERASE,
        CHECK_AT,
        CHECK_GRID,
        QUIT,
        ERROR,
    }

    private final int GRID_SIZE;
    private Grid grid;

    private void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    private void printMainMenu() {
        clearConsole();
        System.out.println(" $$$$$$\\  $$\\   $$\\ $$$$$$$\\   $$$$$$\\  $$\\   $$\\ $$\\   $$\\ \n" +
                "$$  __$$\\ $$ |  $$ |$$  __$$\\ $$  __$$\\ $$ | $$  |$$ |  $$ |\n" +
                "$$ /  \\__|$$ |  $$ |$$ |  $$ |$$ /  $$ |$$ |$$  / $$ |  $$ |\n" +
                "\\$$$$$$\\  $$ |  $$ |$$ |  $$ |$$ |  $$ |$$$$$  /  $$ |  $$ |\n" +
                " \\____$$\\ $$ |  $$ |$$ |  $$ |$$ |  $$ |$$  $$<   $$ |  $$ |\n" +
                "$$\\   $$ |$$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |\\$$\\  $$ |  $$ |\n" +
                "\\$$$$$$  |\\$$$$$$  |$$$$$$$  | $$$$$$  |$$ | \\$$\\ \\$$$$$$  |\n" +
                " \\______/  \\______/ \\_______/  \\______/ \\__|  \\__| \\______/ ");
        System.out.println("\t\t 1. Play");
        System.out.println("\t\t 2. Quit");
    }

    private void printGameMenu() {
        grid.printGrid();
        System.out.println("\t\t 1. Insert");
        System.out.println("\t\t 2. Erase");
        System.out.println("\t\t 3. Check square");
        System.out.println("\t\t 4. Check grid");
        System.out.println("\t\t 5. Quit");
    }

    private int getMenuInput() {
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        } else {
            return -1;
        }

    }

    private MenuOption intToMenuOption(int i) {
        return switch (i) {
            case 1 -> MenuOption.PLAY;
            case 2 -> MenuOption.QUIT;
            default -> MenuOption.ERROR;
        };
    }

    private GameOption intToGameOption(int i) {
        return switch (i) {
            case 1 -> GameOption.INSERT;
            case 2 -> GameOption.ERASE;
            case 3 -> GameOption.CHECK_AT;
            case 4 -> GameOption.CHECK_GRID;
            case 5 -> GameOption.QUIT;
            default -> GameOption.ERROR;
        };
    }

}