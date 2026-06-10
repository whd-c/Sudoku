import java.util.Optional;
import java.util.Scanner;

public class Game {

    public Game() {
        GRID_SIZE = 9;
        cacheManager = new CacheManager("cache.json");

        scanner = new Scanner(System.in);
        menu = new Menu(scanner);

        grid = new Grid(GRID_SIZE);
        printCheckedGrid = false;

        score = 0;
        difficulty = Difficulty.EASY;
        readCache();
    }

    public void run() {
        boolean isPlaying = true;
        while (isPlaying) {
            menu.printMainMenu();
            MenuOption menuOption = intToMenuOption(menu.getMenuInput()).orElse(null);
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
        menu.clearConsole();
        System.out.println(Color.BLUE.apply("Thanks for playing!"));
        System.out.println(Color.CYAN.apply("Final score: ") + Color.GREEN.apply(Long.toString(score)));
        writeToCache();
    }

    private void playGameLoop() {
        boolean inGame = true;
        grid.generate(emptyCellsAmount(difficulty));
        while (inGame) {
            if (grid.isSolved()) {
                switch (difficulty) {
                    case EASY -> score += 100;
                    case MEDIUM -> score += 200;
                    case HARD -> score += 300;
                }
                menu.clearConsole();
                System.out.println(Color.GREEN.apply(" ____  ____   ___   _____  _____   ____      ____   ___   ____  _____  _  \n" +
                        "|_  _||_  _|.'   `.|_   _||_   _| |_  _|    |_  _|.'   `.|_   \\|_   _|| | \n" +
                        "  \\ \\  / / /  .-.  \\ | |    | |     \\ \\  /\\  / / /  .-.  \\ |   \\ | |  | | \n" +
                        "   \\ \\/ /  | |   | | | '    ' |      \\ \\/  \\/ /  | |   | | | |\\ \\| |  | | \n" +
                        "   _|  |_  \\  `-'  /  \\ \\__/ /        \\  /\\  /   \\  `-'  /_| |_\\   |_ |_| \n" +
                        "  |______|  `.___.'    `.__.'          \\/  \\/     `.___.'|_____|\\____|(_) \n" +
                        "   "));
                System.out.println("Your score: " + score);
                scanner.nextLine();
                writeToCache();
                break;
            }
            menu.printGameMenu(grid, printCheckedGrid);
            GameOption gameOption = intToGameOption(menu.getMenuInput()).orElse(null);
            if (gameOption == null) {
                continue;
            }
            switch (gameOption) {
                case INSERT:
                    System.out.println("Enter square to insert to (LetterNumber format):");
                    try {
                        Vector2 square = menu.parseGameOption(GRID_SIZE);
                        if (grid.getAt(square.y(), square.x()) != 0) {
                            throw new IllegalArgumentException("Square is already occupied.");
                        }
                        System.out.println("Insert value to insert: ");
                        int val;
                        if (scanner.hasNextInt()) {
                            val = scanner.nextInt();
                            scanner.nextLine();
                        } else {
                            scanner.nextLine();
                            throw new IllegalArgumentException("Argument is not a number.");
                        }
                        if (val <= 0 || val > GRID_SIZE) {
                            throw new IllegalArgumentException("Value is out of range. (1 - " + GRID_SIZE + ")");
                        }
                        grid.insertAt(square.y(), square.x(), val);
                    } catch (IllegalArgumentException e) {
                        System.out.println(Color.RED.apply("Insert error: " + e.getMessage()));
                        scanner.nextLine();
                    }
                    break;
                case ERASE:
                    System.out.println("Enter square to erase (LetterNumber format):");
                    try {
                        Vector2 square = menu.parseGameOption(GRID_SIZE);
                        if (grid.isStartingSquare(square.y(), square.x())) {
                            throw new IllegalArgumentException("Cannot delete original squares.");
                        }
                        if (grid.getAt(square.y(), square.x()) == 0) {
                            throw new IllegalArgumentException("Square is already empty.");
                        }
                        grid.removeAt(square.y(), square.x());
                    } catch (IllegalArgumentException e) {
                        System.out.println(Color.RED.apply("Erase error: " + e.getMessage()));
                        scanner.nextLine();
                    }
                    break;
                case CHECK_AT:
                    System.out.println("Enter square to check (LetterNumber format):");
                    try {
                        Vector2 square = menu.parseGameOption(GRID_SIZE);
                        int currentValue = grid.getAt(square.y(), square.x());

                        if (currentValue == 0) {
                            throw new IllegalArgumentException("Square " + (char) (square.y() + 65) + (square.x() + 1) + " is empty.");
                        } else if (grid.isSolvedAt(square.y(), square.x())) {
                            System.out.println("The value " + currentValue + " is correct for this square.");
                        } else {
                            System.out.println("The value " + currentValue + " is wrong for this square.");
                        }
                        scanner.nextLine();
                    } catch (IllegalArgumentException e) {
                        System.out.println(Color.RED.apply("Check at error: " + e.getMessage()));
                        scanner.nextLine();
                    }
                    break;
                case CHECK_GRID:
                    printCheckedGrid = !printCheckedGrid;
                    break;
                case QUIT:
                    System.out.println(Color.RED.apply("Are you sure you wanna quit? (Y/N)"));
                    String ans = scanner.nextLine().trim().toUpperCase();
                    if (ans.equals("Y"))
                        inGame = false;
                    break;
            }
        }
    }

    private void goIntoSettings() {
        boolean inSettings = true;
        while (inSettings) {
            menu.printSettingsMenu();
            SettingsOption settingsOption = intToSettingsOption(menu.getMenuInput()).orElse(null);
            if (settingsOption == null) {
                continue;
            }
            switch (settingsOption) {
                case SET_DIFFICULTY:
                    setDifficultyMenu();
                    break;
                case CLEAR_CACHE:
                    clearCache();
                    break;
                case BACK:
                    inSettings = false;
                    break;

            }
        }
    }

    private void setDifficultyMenu() {
        menu.printDifficultyMenu();
        Difficulty diff = intToDifficulty(menu.getMenuInput()).orElse(null);
        if (diff == null) {
            return;
        }
        switch (diff) {
            case EASY -> difficulty = Difficulty.EASY;
            case MEDIUM -> difficulty = Difficulty.MEDIUM;
            case HARD -> difficulty = Difficulty.HARD;
        }
        writeToCache();
    }

    private void writeToCache() {
        cacheManager.write(new CacheData(difficulty.name(), score));
    }

    private void readCache() {
        CacheData data = cacheManager.read();

        score = data.score();

        try {
            difficulty = Difficulty.valueOf(data.difficulty());
        } catch (IllegalArgumentException e) {
            difficulty = Difficulty.EASY;
        }
    }

    private void clearCache() {
        difficulty = Difficulty.EASY;
        score = 0;
        cacheManager.clear();
        System.out.println("Cache cleared.");
        scanner.nextLine();
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

    private final CacheManager cacheManager;
    private final Menu menu;
    private final Grid grid;

    private final int GRID_SIZE;
    private final Scanner scanner;

    private long score;
    private Difficulty difficulty;
    private boolean printCheckedGrid;


    //zostawię tak te funkcje do konwersji enumów bo nie chce mi się robić metod
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

    private int emptyCellsAmount(Difficulty diff) {
        return switch (diff) {
            case EASY -> (int) (0.4 * GRID_SIZE * GRID_SIZE);
            case MEDIUM -> (int) (0.5 * GRID_SIZE * GRID_SIZE);
            case HARD -> (int) (0.6 * GRID_SIZE * GRID_SIZE);
        };
    }

}