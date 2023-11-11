
package day03.ex03;

/*
 * GPT-4 Feedback
 * Your logic seems to be reasonable for achieving the distribution of work among 
 * multiple threads. You are using a vector to store the file URLs, which is a thread-safe
 * collection that can handle concurrent access and modification. You are also using a lock 
 * object to synchronize the block of code where you remove the last element from the vector 
 * and assign it to a local variable. This ensures that no two threads can get the same file
 * URL at the same time. Then, you call the downloadFile method outside the synchronized block
 * which allows other threads to proceed with their work while the current thread is 
 * downloading the file. This way, you are minimizing the amount of time that the threads have 
 * to wait for the lock to be released.
 * 
 */
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Vector;

class Program {

    static private class FileDownloader implements Runnable {
        Object lock;
        Vector<String> urlsVec;

        FileDownloader(Object lock, Vector<String> urlsVec) {
            this.urlsVec = urlsVec;
            this.lock = lock;
        }

        public void run() {
            while (urlsVec.isEmpty() == false) {
                String file = null;
                int index = 0;
                synchronized (this.lock) {
                    index = urlsVec.size();
                    file = urlsVec.remove(urlsVec.size() - 1);
                }
                downloadFile(file, index);
            }
        }

    }

    private static Vector<String> readFileUrls(String filename) {
        Vector<String> urlsVec = new Vector<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        String url = null;
        try {
            while ((url = reader.readLine()) != null) {
                if (url.isEmpty() == true) {
                    continue;
                }
                urlsVec.add(url);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return urlsVec;
    }

    static private void writeToFile(String fileName, InputStream in) {
        try {
            byte[] buffer = new byte[1024];
            FileOutputStream out = new FileOutputStream(fileName);
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.close();
        } catch (IOException e) {
            System.err.println("Err: " + e.getMessage());
            System.exit(1);
        }
    }

    static private void downloadFile(String resource, int fileNum) {
        System.out.println(Thread.currentThread().getName() + " start download file number " + fileNum);
        try {
            URL url = new URI(resource).toURL();
            String fileName = resource.substring(resource.lastIndexOf('/') + 1);
            InputStream in = url.openStream();
            writeToFile(fileName, in);
            in.close();
            System.out.println(Thread.currentThread().getName() + " finish download file number " + fileNum);
        } catch (Exception e) {
            System.out.println("Err: " + e.getMessage());
            System.exit(1);
        }
    }

    static public void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Program.java --threadsCount=<number>");
            System.exit(1);
        }
        long threadsCount = 0;
        try {
            String[] argsArr = args[0].split("=");
            if (argsArr[0].equals("--threadsCount") == false) {
                throw new IllegalArgumentException();

            }
            threadsCount = Long.parseLong(argsArr[1]);
            if (threadsCount <= 0) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            System.err.println("Err: Please provide a valid argument: " + e.getMessage());
            System.exit(1);
        }

        String filename = "files_urls.txt";
        Vector<String> urlsVec = readFileUrls(filename);
        Thread[] threads = new Thread[(int) threadsCount];
        Object lock = new Object();
        for (int i = 0; i < threads.length; i++) {
            Thread th = new Thread(new FileDownloader(lock, urlsVec));
            threads[i] = th;
            th.start();
        }
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (Exception e) {
                System.err.println("Err: " + e.getMessage());
                System.exit(1);
            }
        }
    }
}
