package ch.hearc.parapa_II;

public class Color {
    private static final String ANSI_RESET = "\u001B[0m";

    private String name;
    private String code;
    private boolean isAlreadyUsed;

    public static final Color NONE = new Color("None", "");

    /**
     * Constructor of the color
     * @param name Name of the color
     * @param code Code of the color
     */
    public Color(String name, String code) {
        this.name = name;
        this.code = code;
        this.isAlreadyUsed = false;
    }

    /**
     *  Get the name of the color
     * @return the name of the color
     */
    public String getName() {
        return name;
    }

    /**
     * Get the code of the color
     * @return the code of the color
     */
    public String getCode() {
        return code;
    }

    /**
     * Get the boolean isAlreadyUsed
     * @return the boolean isAlreadyUsed
     */
    public boolean isAlreadyUsed() {
        return isAlreadyUsed;
    }

    /**
     * Set the boolean isAlreadyUsed
     * @param alreadyUsed the boolean isAlreadyUsed
     */
    public void setAlreadyUsed(boolean alreadyUsed) {
        isAlreadyUsed = alreadyUsed;
    }

    /**
     * Get the colored text
     * @param text the text to color
     * @return the colored text
     */
    public String getColoredText(String text) {
        return code + text + ANSI_RESET;
    }
}
