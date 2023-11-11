package app;

import java.io.IOException;

import logic.Bmp;
import logic.ConsolePrinter;

// i need to read the images, define the two colored images pixels and print them

public class App {

    static char blackPixelChar, whitePixelChar;

    private static String getArg(String args, String argName) throws IllegalArgumentException {
        String[] arguments = args.split("=");
        if (arguments[0].equals(argName) == false) {
            throw new IllegalArgumentException("Err: Please provide a valid argument\n\nUsage: ...");
        }
        return arguments[1];
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println(
                    "Usage: java -cp target fr._42.printer.app.App --black=<char> --white=<char> --path=<path>");
            System.exit(1);
        }
        String filePath = null;
        try {
            String[] argsNames = {
                    "--black",
                    "--white",
                    "--path"
            };
            String[] argsValues = new String[args.length];
            for (int i = 0; i < args.length; i++) {
                argsValues[i] = getArg(args[i], argsNames[i]);
            }
            // checking if --black , --white contains one single character
            Character blackPixel = argsValues[0].toCharArray().length == 1 ? argsValues[0].toCharArray()[0] : null;
            Character whitePixel = argsValues[1].toCharArray().length == 1 ? argsValues[1].toCharArray()[0] : null;
            if (blackPixel == null || whitePixel == null) {
                throw new IllegalArgumentException();
            }
            filePath = argsValues[2];
            blackPixelChar = blackPixel;
            whitePixelChar = whitePixel;
        } catch (Exception e) {
            System.err.println("Err: " + e.getMessage());
            System.exit(1);
        }
        Bmp bmp = null;
        try {
            bmp = new Bmp(filePath);
        } catch (Exception e) {
            System.err.println("Err: " + e.getMessage());
            System.exit(1);
        }
        // bmp.getBmpHeaderInfo(filePath);
        ConsolePrinter consolePrinter = new ConsolePrinter(bmp, blackPixelChar, whitePixelChar);
        consolePrinter.printToConsole();
    }
}
