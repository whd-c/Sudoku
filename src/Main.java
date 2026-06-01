public class Main {
    public static void main(String[] args) {
        try {
            var game = new Game();
            game.run();
        } catch (IllegalArgumentException e) {
            System.err.println("An illegal argument exception occurred: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}