
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOError;
import java.io.IOException;

import javax.imageio.ImageIO;;

class Program {

    public static void toBits(int color) {
        while (color != 0) {
            System.out.print(color % 2 == 1 ? '0' : '1');
            color /= 2;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        //
        String[] argsArr = args[0].split("=");
        try {
            File bmpFile = new File(argsArr[1]);
            BufferedImage image = ImageIO.read(bmpFile);
            System.out.println("Width : " + image.getWidth() + " Height: " + image.getHeight());
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int color = image.getRGB(y, x);

                    char pixelChar = (color == Color.WHITE.getRGB() ? '.' : '0');
                    toBits(color);
                    // System.out.print(pixelChar);
                }
                // System.out.println();
            }
            System.out.println(image.toString());
        } catch (IOException e) {
            System.err.println("Err: " + e.getMessage());
            System.exit(1);
        }
    }
}