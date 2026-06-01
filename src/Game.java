import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

public class Game {

    public Game() {
        score = 0;
        GRID_SIZE = 9;
        grid = new Grid(GRID_SIZE);
    }

    public void run() {
        boolean isPlaying = true;
        while (isPlaying) {
            printMainMenu();
            MenuOption menuOption = intToMenuOption(getMenuInput()).orElse(null);
            if (menuOption == null) {
                continue;
            }
            switch (menuOption) {
                case PLAY:
                    playGameLoop();
                    break;
                case SETTINGS:
                    goIntoSettings();
                    break;
                case QUIT:
                    isPlaying = false;
                    break;
            }
        }
        clearConsole();
        System.out.println("Thanks for playing!");
        System.out.println("Final score: " + score);
    }

    private void playGameLoop() {
        boolean inGame = true;
        while (inGame) {
            printGameMenu();
            GameOption gameOption = intToGameOption(getMenuInput()).orElse(null);
            if (gameOption == null) {
                continue;
            }
            switch (gameOption) {
                case INSERT:
                    System.out.println("Enter square to insert to (LetterNumber format):");
                    try {
                        Vector2 square = parseGameOption();
                        if (grid.getAt(square.y, square.x) != 0) {
                            throw new IllegalArgumentException("Square is already occupied.");
                        }
                        System.out.println("Insert value to insert: ");
                        Scanner scanner = new Scanner(System.in);
                        int val;
                        if (scanner.hasNextInt()) {
                            val = scanner.nextInt();
                        } else {
                            throw new IllegalArgumentException("Argument is not a number.");
                        }
                        if (val <= 0 || val > GRID_SIZE) {
                            throw new IllegalArgumentException("Value is out of range. (1 - " + GRID_SIZE + ")");
                        }
                        grid.insertAt(square.y, square.x, val);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Insert error: " + e.getMessage());
                    }
                    break;
                case ERASE:
                    System.out.println("Enter square to erase (LetterNumber format):");
                    try {
                        Vector2 square = parseGameOption();
                        if (grid.getAt(square.y, square.x) == 0) {
                            throw new IllegalArgumentException("Square is already empty.");
                        }
                        grid.removeAt(square.y, square.x);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Erase error: " + e.getMessage());
                    }
                    break;
                case CHECK_AT:
                    System.out.println("Enter square to check (LetterNumber format):");
                    try {
                        Vector2 square = parseGameOption();
                    } catch (IllegalArgumentException e) {
                        System.out.println("Check at error: " + e.getMessage());
                    }
                    break;
                case CHECK_GRID:
                    //color unsolved red
                    //grid.printCheckedGrid();
                    break;
                case QUIT:
                    inGame = false;
                    break;
            }
        }
    }

    private void goIntoSettings() {
        printSettingsMenu();
        //TODO: Finish in the next commit
    }

    private enum MenuOption {
        PLAY,
        SETTINGS,
        QUIT,
    }

    private enum GameOption {
        INSERT,
        ERASE,
        CHECK_AT,
        CHECK_GRID,
        QUIT,
    }

    private enum SettingsOption {
        SET_DIFFICULTY,
        CLEAR_CACHE,
        BACK,
    }

    private enum Difficulty {
        EASY,
        MEDIUM,
        HARD,
    }

    private final int GRID_SIZE;
    private int score;
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
        clearConsole();
        grid.printGrid();
        System.out.println("\t\t 1. Insert");
        System.out.println("\t\t 2. Erase");
        System.out.println("\t\t 3. Check square");
        System.out.println("\t\t 4. Check grid");
        System.out.println("\t\t 5. Quit");
    }

    private void printSettingsMenu() {
        clearConsole();
        System.out.println("Settings.");
        System.out.println("\t\t 1. Set difficulty");
        System.out.println("\t\t 2. Clear cache");
        System.out.println("\t\t 3. Back to menu");
    }

    private int getMenuInput() {
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        } else {
            return -1;
        }
    }

    private Vector2 parseGameOption() {
        Scanner scanner = new Scanner(System.in);
        String in = scanner.nextLine().trim();
        int number;
        if (in.length() < 2) {
            throw new IllegalArgumentException("Invalid option.");
        }
        char letter = Character.toUpperCase(in.charAt(0));
        if (!Character.isLetter(letter)) {
            throw new IllegalArgumentException("Invalid letter.");
        }
        try {
            number = Integer.parseInt(in.substring(1)) - 1;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number.");
        }
        if (number < 0 || number >= GRID_SIZE) {
            throw new IllegalArgumentException("Number out of range.");
        }

        int letterInInt = letter - 'A';
        if (letterInInt < 0 || letterInInt >= GRID_SIZE) {
            throw new IllegalArgumentException("Letter out of range.");
        }

        return new Vector2(number, letterInInt);
    }

    private Optional<MenuOption> intToMenuOption(int i) {
        return switch (i) {
            case 1 -> Optional.of(MenuOption.PLAY);
            case 2 -> Optional.of(MenuOption.SETTINGS);
            case 3 -> Optional.of(MenuOption.QUIT);
            default -> Optional.empty();
        };
    }

    private Optional<GameOption> intToGameOption(int i) {
        return switch (i) {
            case 1 -> Optional.of(GameOption.INSERT);
            case 2 -> Optional.of(GameOption.ERASE);
            case 3 -> Optional.of(GameOption.CHECK_AT);
            case 4 -> Optional.of(GameOption.CHECK_GRID);
            case 5 -> Optional.of(GameOption.QUIT);
            default -> Optional.empty();
        };
    }

    private Optional<SettingsOption> intToSettingsOption(int i) {
        return switch (i) {
            case 1 -> Optional.of(SettingsOption.SET_DIFFICULTY);
            case 2 -> Optional.of(SettingsOption.CLEAR_CACHE);
            case 3 -> Optional.of(SettingsOption.BACK);
            default -> Optional.empty();
        };
    }

    private Optional<Difficulty> intToDifficulty(int i) {
        return switch (i) {
            case 1 -> Optional.of(Difficulty.EASY);
            case 2 -> Optional.of(Difficulty.MEDIUM);
            case 3 -> Optional.of(Difficulty.HARD);
            default -> Optional.empty();
        };
    }

}