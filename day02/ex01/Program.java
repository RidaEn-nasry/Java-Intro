
package day02.ex01;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;

class Program {
    public void main(String[] args) {

        if (args.length < 2) {
            // usage file1, file1
            System.err.println("usage: java Program file1 file2");
            System.exit(-1);
        }
        // FileWriter
        // FileReader
        BufferedReader bufferedReader = null;
        BufferedReader bufferedReader2 = null;
        try {
         bufferedReader = new BufferedReader(new FileReader(args[0]));
         bufferedReader2 = new BufferedReader(new FileReader(args[1]));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        
        }        

    }
}