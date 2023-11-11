package logic;

import java.awt.Color;
import logic.Bmp;

public class ConsolePrinter {
    private Bmp bmp;
    private char whitePixel;
    private char blackPixel;

    public ConsolePrinter(Bmp bmp, char whitePixel, char blackPixel) {
        this.bmp = bmp;
        this.whitePixel = whitePixel;
        this.blackPixel = blackPixel;
    }

    public void printToConsole() {
        int[][] pixels = bmp.getPixels();
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                System.out.print(pixels[y][x] == Color.BLACK.getRGB() ? blackPixel : whitePixel);
            }
            System.out.println();
        }
    }
}
