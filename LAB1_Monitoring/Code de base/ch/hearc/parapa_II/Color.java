package ch.hearc.parapa_II;
public class Color {
    private static final String ANSI_RESET = "\u001B[0m";

    private String name;
    private String code;
    private boolean isAlreadyUsed;

    public Color(String name, String code) {
        this.name = name;
        this.code = code;
        this.isAlreadyUsed = false;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public boolean isAlreadyUsed() {
        return isAlreadyUsed;
    }

    public void setAlreadyUsed(boolean alreadyUsed) {
        isAlreadyUsed = alreadyUsed;
    }

    public String getColoredText(String text) {
        return code + text + ANSI_RESET;
    }
}
