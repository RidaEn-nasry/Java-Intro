
package day02.ex00;
import java.util.Scanner;

public class Program {

    public static void main(String args[]) {
        String fileName = "signatures.txt";
        MagicNumberAnalyzer mgc = new MagicNumberAnalyzer(fileName);
        Scanner input = new Scanner(System.in);
        while (input.hasNext()) {
            String line = input.nextLine();
            if (line.equals("42")) {
                break;
            }
            String fileType = mgc.getFileType(line);
            // file don't exist
            if (fileType.isEmpty()) {
                continue;
            }
            // we have a file type
            else if (!fileType.equals("UNDEFINED")) {
                mgc.writeResult(fileType);
                System.out.println("PROCESSED");
            } else {
                System.out.println("UNDEFINED");
            }
        }
        input.close();
    }
}
