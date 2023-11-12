
package logic;

import java.awt.Color;
import logic.Bmp;
import com.diogonunes.jcolor.Ansi;
import com.diogonunes.jcolor.Attribute;
import com.diogonunes.jcolor.AnsiFormat;

public class ConsolePrinter {
    private Bmp bmp;
    private Attribute whiteBackground;
    private Attribute blackBackground;

    private Attribute getColorAttribute(String colorName) throws IllegalArgumentException {
        switch (colorName.toUpperCase()) {
            case "BLACK":
                return Attribute.BLACK_BACK();
            case "RED":
                return Attribute.RED_BACK();
            case "GREEN":
                return Attribute.GREEN_BACK();
            case "YELLOW":
                return Attribute.YELLOW_BACK();
            case "BLUE":
                return Attribute.BLUE_BACK();
            case "MAGENTA":
                return Attribute.MAGENTA_BACK();
            case "CYAN":
                return Attribute.CYAN_BACK();
            case "WHITE":
                return Attribute.WHITE_BACK();
            default:
                throw new IllegalArgumentException("Unknown color: " + colorName);
        }
    }

    public ConsolePrinter(Bmp bmp, String whiteColor, String blackColor) {
        this.bmp = bmp;
        try {
            this.whiteBackground = getColorAttribute(whiteColor);
            this.blackBackground = getColorAttribute(blackColor);
        } catch (IllegalArgumentException e) {
            System.err.println("Err: " + e.getMessage());
            System.exit(1);
        }
    }

    public void printToConsole() {
        int[][] pixels = bmp.getPixels();
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                Attribute bgColor = (pixels[y][x] == Color.BLACK.getRGB()) ? this.blackBackground
                        : this.whiteBackground;
                // Colorize the space character with the chosen background color
                System.out.print(Ansi.colorize(" ", bgColor));
            }
            System.out.println();
        }
    }
}
