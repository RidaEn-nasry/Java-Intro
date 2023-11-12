package app;

import java.io.IOException;

import logic.Bmp;
import logic.ConsolePrinter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

@Parameters(separators = "=")
class Args {
    @Parameter(names = "--black", description = "Black pixel background")
    public String blackPixel = "RED";

    @Parameter(names = "--white", description = "White pixel background")
    public String whitePixel = "BLUE";
}

public class App {
    public static void main(String[] args) {
        Args arguments = new Args();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(arguments)
                .build();
        try {
            jCommander.parse(args);
        } catch (ParameterException e) {
            System.out.println(e.getMessage());
            jCommander.usage();
            System.exit(1);
        }

        String filePath = System.getProperty("user.dir") + "/target/resources/image.bmp";
        Bmp bmp = null;
        try {
            bmp = new Bmp(filePath);
        } catch (Exception e) {
            System.err.println("Err: " + e.getMessage());
            System.exit(1);
        }
        ConsolePrinter consolePrinter = new ConsolePrinter(bmp, arguments.blackPixel,
                arguments.whitePixel);
        consolePrinter.printToConsole();
    }
}
