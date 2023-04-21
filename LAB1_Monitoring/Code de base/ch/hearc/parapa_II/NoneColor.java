package ch.hearc.parapa_II;

public class NoneColor extends Color {

    public NoneColor() {
        super("None", "");
    }

    @Override
    public String getColoredText(String text) {
        return text;
    }
}
