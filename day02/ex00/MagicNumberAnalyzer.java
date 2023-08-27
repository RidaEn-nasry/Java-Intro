
package day02.ex00;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;

class MagicNumberAnalyzer {
    private Hashtable<String, String> signatures;

    private String getLine(FileInputStream fileInputStream, boolean readAsBytes) {
        String line = "";
        int c;
        try {
            do {
                c = fileInputStream.read();
                if (c == '\n') {
                    break;
                }
                if (readAsBytes) { // read as bytes (hexadecimal)
                    line += String.format("%02X", c) + " ";
                } else {
                    line += (char) c;
                }
            } while (c != -1);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return line;
    }

    private void readSignaturesFile(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        while (fileInputStream.available() > 0) {
            String line = getLine(fileInputStream, false);
            if (line.isEmpty())
                continue;
            try {
                // get the magic number and the file type
                // 01 06 01 00;Job File
                String[] magicNumberAndFileType = line.split(";");
                signatures.put(magicNumberAndFileType[0], magicNumberAndFileType[1]);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    Hashtable<String, String> getSignatures() {
        return signatures;
    }

    private String matchMagic(String line) {
        for (String key : signatures.keySet()) {
            if (line.startsWith(key)) {
                return signatures.get(key);
            }
        }
        return "UNDEFINED";
    }

    public String getFileType(String filePath) {
        String fileType = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            String line = getLine(fileInputStream, true);

            fileType = matchMagic(line);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return fileType;
    }

    MagicNumberAnalyzer(String filePath) {
        signatures = new Hashtable<>();
        try {
            readSignaturesFile(filePath);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}