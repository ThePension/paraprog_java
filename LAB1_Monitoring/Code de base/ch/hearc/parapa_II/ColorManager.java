package ch.hearc.parapa_II;
import java.util.ArrayList;

public class ColorManager {
    
    private ArrayList<Color> colors;

    private static ColorManager instance;

    private ColorManager() {
        colors = new ArrayList<>();
        colors.add(new Color("ANSI_BLACK", "\u001B[30m"));
        colors.add(new Color("ANSI_RED", "\u001B[31m"));
        colors.add(new Color("ANSI_GREEN", "\u001B[32m"));
        colors.add(new Color("ANSI_YELLOW", "\u001B[33m"));
        colors.add(new Color("ANSI_BLUE", "\u001B[34m"));
        colors.add(new Color("ANSI_PURPLE", "\u001B[35m"));
        colors.add(new Color("ANSI_CYAN", "\u001B[36m"));
        colors.add(new Color("ANSI_PINK", "\u001B[95m"));
        colors.add(new Color("ANSI_ORANGE", "\u001B[38;5;208m"));
        colors.add(new Color("ANSI_LIGHT_BLUE", "\u001B[38;5;39m"));
        colors.add(new Color("ANSI_LIGHT_GREEN", "\u001B[38;5;40m"));
    }

    /**
     * Get the instance of the ColorManager
     * @return the instance of the ColorManager
     */
    public static ColorManager getInstance() {
        if (instance == null) {
            instance = new ColorManager();
        }
        return instance;
    }

    /**
     * Get a random color that is not already used
     * @return a color
     */
    public Color getRandomColor() {
        Color color = null;
        do {
            color = colors.get((int) (Math.random() * colors.size()));
        } while (color.isAlreadyUsed());
        color.setAlreadyUsed(true);
        return color;
    }
}
