import java.io.IOException;
import java.util.Scanner;

public class Menu {
    public Menu(Scanner _scanner) {
        scanner = _scanner;
    }

    private final Scanner scanner;

    public void printMainMenu() {
        clearConsole();
        System.out.println(Color.CYAN.apply(" $$$$$$\\  $$\\   $$\\ $$$$$$$\\   $$$$$$\\  $$\\   $$\\ $$\\   $$\\ \n" +
                "$$  __$$\\ $$ |  $$ |$$  __$$\\ $$  __$$\\ $$ | $$  |$$ |  $$ |\n" +
                "$$ /  \\__|$$ |  $$ |$$ |  $$ |$$ /  $$ |$$ |$$  / $$ |  $$ |\n" +
                "\\$$$$$$\\  $$ |  $$ |$$ |  $$ |$$ |  $$ |$$$$$  /  $$ |  $$ |\n" +
                " \\____$$\\ $$ |  $$ |$$ |  $$ |$$ |  $$ |$$  $$<   $$ |  $$ |\n" +
                "$$\\   $$ |$$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |\\$$\\  $$ |  $$ |\n" +
                "\\$$$$$$  |\\$$$$$$  |$$$$$$$  | $$$$$$  |$$ | \\$$\\ \\$$$$$$  |\n" +
                " \\______/  \\______/ \\_______/  \\______/ \\__|  \\__| \\______/ "));
        System.out.println(Color.GREEN.apply("\t\t 1. Play"));
        System.out.println(Color.BLUE.apply("\t\t 2. Settings"));
        System.out.println(Color.RED.apply("\t\t 3. Quit"));
    }

    public void printGameMenu(Grid grid, boolean checkedGrid) {
        clearConsole();
        if (checkedGrid) {
            grid.printCheckedGrid();
        } else {
            grid.printGrid();
        }
        System.out.println(Color.GREEN.apply("\t\t 1. Insert"));
        System.out.println(Color.ORANGE.apply("\t\t 2. Erase"));
        System.out.println(Color.YELLOW.apply("\t\t 3. Check square"));
        System.out.println(Color.PURPLE.apply("\t\t 4. Check grid"));
        System.out.println(Color.RED.apply("\t\t 5. Quit"));
    }

    public void printSettingsMenu() {
        clearConsole();
        System.out.println(Color.BLUE.apply("Settings."));
        System.out.println(Color.PURPLE.apply("\t\t 1. Set difficulty"));
        System.out.println(Color.CYAN.apply("\t\t 2. Clear cache"));
        System.out.println(Color.RED.apply("\t\t 3. Back to menu"));
    }

    public void printDifficultyMenu() {
        clearConsole();
        System.out.println(Color.BLUE.apply("Select your difficulty:"));
        System.out.println(Color.GREEN.apply("\t\t1. Easy"));
        System.out.println(Color.YELLOW.apply("\t\t2. Medium"));
        System.out.println(Color.RED.apply("\t\t3. Hard"));
    }

    public int getMenuInput() {
        if (scanner.hasNextInt()) {
            int val = scanner.nextInt();
            scanner.nextLine();
            return val;
        } else {
            scanner.nextLine();
            return -1;
        }
    }

    public Vector2 parseGameOption(int gridSize) {
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
        if (number < 0 || number >= gridSize) {
            throw new IllegalArgumentException("Number out of range.");
        }

        int letterInInt = letter - 'A';
        if (letterInInt < 0 || letterInInt >= gridSize) {
            throw new IllegalArgumentException("Letter out of range.");
        }

        return new Vector2(number, letterInInt);
    }

    public void clearConsole() {
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
}
