
package day03.ex02;

import java.util.Random;

class Program {

    static long result;
    static long actualResult;

    private static int[] generateRandomArray(int size) {
        int arr[] = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(50);
            actualResult += arr[i];
        }
        System.out.println("Sum: " + actualResult);

        return arr;
    }

    static private class Accumulator implements Runnable {
        int start;
        int end;
        int arr[];
        int id;
        Object lock;

        Accumulator(int arr[], int start, int end, Object lock, int id) {
            this.start = start;
            this.end = end;
            this.arr = arr;
            this.lock = lock;
            this.id = id;
        }

        public void run() {
            int sum = 0;
            synchronized (this.lock) {
                System.out.print("Thread " + this.id + " from " + start + " to " + end);
                while (this.start < this.end) {
                    sum += this.arr[this.start];
                    this.start += 1;
                }
                result += sum;
                System.out.println(" sum is " + sum);
            }

        }

    }

    static private void sumMultiThreaded(int arrSize, int threadsCount, int arr[]) {
        Thread threads[] = new Thread[threadsCount];
        int section = arrSize / threadsCount;
        // if threadsCount > arrSize , we have more threads than elem
        if (section == 0) {
            section = 1;
        }

        Object lock = new Object();
        int launchedThreads = 0;
        // having more threads than arr elements is a waste
        for (; launchedThreads < threadsCount; launchedThreads++) {
            if (launchedThreads >= arrSize) {
                break;
            }
            int start = launchedThreads * section;
            int end = (launchedThreads + 1) == threadsCount ? arrSize : start + section;
            Thread th = new Thread(new Accumulator(arr, start, end, lock, launchedThreads + 1));
            th.start();
            threads[launchedThreads] = th;
        }

        for (int i = 0; i < launchedThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.out.println("Thread Interrupted");
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java Program --arraySize=<number> --threadsCount=<number>");
            System.exit(-1);
        }
        int arrSize = 0;
        int threadsCount = 0;

        try {
            String arrArgs[] = args[0].split("=");
            String threadsArgs[] = args[1].split("=");
            if (arrArgs[0].equals("--arraySize") == false
                    || threadsArgs[0].equals("--threadsCount") == false) {
                throw new IllegalArgumentException();
            }
            arrSize = Integer.parseInt(arrArgs[1]);
            threadsCount = Integer.parseInt(threadsArgs[1]);
            if (arrSize <= 0 || threadsCount <= 0) {
                System.err.println("Err: Please provide valid argument");
                System.exit(-1);
            }

        } catch (Exception e) {
            System.err.println("Err: Please provide a valid argument");
            System.exit(-1);
        }
        int arr[] = generateRandomArray(arrSize);

        sumMultiThreaded(arrSize, threadsCount, arr);
        System.out.println("Sum by threads: " + result);
    }
}
