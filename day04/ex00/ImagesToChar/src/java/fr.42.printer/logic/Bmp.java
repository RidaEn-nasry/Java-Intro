package fr._42.printer.logic;


import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Bmp {
    private int[][] pixels = new int[16][16]; 

    public Bmp(String filePath) throws IOException {
        loadBmp(filePath);
    }

    public static boolean isBmpFile(File imageFile) {
        String fileName = imageFile.getName().toLowerCase();
        return fileName.endsWith(".bmp");
    }

    private void loadBmp(String filePath) throws IOException, IllegalArgumentException {
        File bmpFile = new File(filePath);
        if (isBmpFile(bmpFile) == false) {
            throw new IllegalArgumentException("Err: only bmp images are allowed");
        }

        BufferedImage bmpImg = ImageIO.read(bmpFile);
        if (bmpImg.getWidth() != 16 || bmpImg.getHeight() != 16) {
            throw new IllegalArgumentException("Err: only 16x16 bmp images are allowed");
        }

        for (int y = 0; y < bmpImg.getHeight(); y++) {
            for (int x = 0; x < bmpImg.getWidth(); x++) {
                pixels[y][x] = bmpImg.getRGB(x, y);
            }
        }
    }

    public int[][] getPixels() {
        return pixels;
    }

}
