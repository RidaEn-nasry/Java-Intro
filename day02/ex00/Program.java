package day03.ex00;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Vector;

public class Program {

    // get magic number from file
    public static byte[] getMagicNumber(String fileName) throws IOException {
        byte[] magicNumber = new byte[4];
        FileInputStream fileInputStream = new FileInputStream(fileName);
        fileInputStream.read(magicNumber, 0, 4);
        fileInputStream.close();
        return magicNumber;

    }

    // public static Map

    public static void main(String args[]) {

        try {
            // get magic number from file
            byte[] magicNumber = getMagicNumber(args[0]);
            for (int i = 0; i < magicNumber.length; i++) {
                System.out.printf("%02X ", magicNumber[i]);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

}
