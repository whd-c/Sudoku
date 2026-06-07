public enum Color {
    RESET("\u001B[0m"),

    RED("\u001B[31m"),
    GREEN("\u001B[92m"),
    YELLOW("\u001B[93m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[96m"),
    ORANGE("\033[38;5;208m"),
    BLACK("\033[30m");

    private final String color;

    Color(String _color) {
        color = _color;
    }

    @Override
    public String toString() {
        return color;
    }

    public String apply(String text) {
        return color + text + RESET;
    }
}