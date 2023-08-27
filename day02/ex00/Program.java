
package day02.ex00;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Program {

    public static void main(String args[]) {
        if (args.length < 1) {
            System.err.println("Usage: java Program <file_path>");
            System.exit(-1);
        }
        MagicNumberAnalyzer mgc = new MagicNumberAnalyzer(args[0]);

        FileOutputStream out = null;
        try {
            out = new FileOutputStream("results.txt");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(-1);
        }

        Scanner input = new Scanner(System.in);
        while (input.hasNext()) {
            if (input.equals("42")) {
                break;
            }
            String fileType = mgc.getFileType(input.next());
            if (!fileType.equals("UNDIFINED")) {
                try {
                    out.write(fileType.getBytes());
                } catch (IOException e) {
                    System.err.println("Error: " + e.getMessage());
                    System.exit(-1);
                }
                System.out.println("PROCESSED");
            } else {
                System.out.println("UNDEFINED");
            }
        }
        input.close();
    }

}
