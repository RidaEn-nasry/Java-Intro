
package day02.ex01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Vector;

class CosineSimilariy {
    // vec of our word frequencies
    private Vector<Hashtable<String, Integer>> wordFreq;
    private BufferedWriter out;
    // our dictionary
    private Set<String> dict;
    public CosineSimilariy() {
        wordFreq = new Vector<Hashtable<String, Integer>>();
        dict = new TreeSet<String>();
        try {
            out = new BufferedWriter(new FileWriter("dictionary.txt"));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    };

    // construct with one file
    public CosineSimilariy(String filePath1, String filePath2) {
        this();
        this.addDocument(filePath1);
        this.addDocument(filePath2);
    }

    public void writeToDoc(String word) {
        try {
            out.write(word + "\n");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // add a doc to the corpus
    private void addDocument(String filePath) {
        if (wordFreq.size() >= 2) {
            System.out.println("Only 2 documents are allowed");
            return;
        }

        // if file size > 10MB, don't add it
        File file = new File(filePath);
        if (file.length() > 10000000) {
            System.out.println("File size is too big");
            return;
        }
        if (file.length() == 0) {
            wordFreq.add(null);
        }

        Hashtable<String, Integer> words = new Hashtable<String, Integer>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(" ");
                for (String token : tokens) {
                    if (token.length() == 0) {
                        continue;
                    }
                
                    if (dict.add(token) == true) {
                        writeToDoc(token);
                    }
                    if (words.get(token) == null) {
                        words.put(token, 1);
                    } else {
                        words.put(token, words.get(token) + 1);
                    }
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        wordFreq.add(words);
    }

    // calcuate the cosine similarity between two specified documents
    public double calcuateCosSim() {
        // one of files was empty, similarity is 0
        if (wordFreq.get(0) == null || wordFreq.get(1) == null) {
            return 0;
        }
        Hashtable<String, Integer> words1 = wordFreq.get(0);
        Hashtable<String, Integer> words2 = wordFreq.get(1);
        if (words1 == null || words2 == null) {
            System.out.println("Document not found");
            return -1;
        }
        double dotProduct = 0;
        double norm1 = 0;
        double norm2 = 0;
        try {
            for (String word : dict) {
                int occr1 = words1.get(word) == null ? 0 : words1.get(word);
                int occr2 = words2.get(word) == null ? 0 : words2.get(word);
                dotProduct += occr1 * occr2;
                norm1 += words1.get(word) == null ? 0 : words1.get(word) * words1.get(word);
                norm2 += words2.get(word) == null ? 0 : words2.get(word) * words2.get(word);
            }
            return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
        } catch (Exception e) {
            System.out.println("Error calculating cosine similarity");
            return -1;
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

}
