
package day03.ex01;

class Program {

    static class SharedData {
        boolean eggTurn;

        SharedData() {
            this.eggTurn = true;
        }
    }

    public static class myRunnable implements Runnable {
        private String word;
        private int num;
        private SharedData sharedData;

        myRunnable(String word, int num, SharedData sharedData) {
            this.word = word;
            this.num = num;
            this.sharedData = sharedData;
        }

        public void run() {
            while (num-- != 0) {
                synchronized (this.sharedData) {
                    // eggTurn should be true and executing thread should be that of Egg
                    while (this.sharedData.eggTurn != this.word.equals("Egg")) {
                        try {
                            this.sharedData.wait();
                        } catch (InterruptedException e) {
                            System.err.println("Thread have been interrupted");
                        }
                    }
                    System.out.println(this.word);
                    this.sharedData.eggTurn = !this.sharedData.eggTurn;
                    this.sharedData.notify();

                }
            }
        }
    }

    static public void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Program.java --count=<number>");
            System.exit(-1);
        }
        int num = 0;
        try {
            String numArgs[] = args[0].split("=");
            if (numArgs[0].equals("--count") != true) {
                throw new IllegalArgumentException();
            }
            num = Integer.parseInt(numArgs[1]);
        } catch (Exception e) {
            System.err.println("Err: Please provide a valid argument");
            System.exit(-1);
        }

        SharedData sharedData = new SharedData();

        myRunnable job1 = new myRunnable("Egg", num, sharedData);
        myRunnable job2 = new myRunnable("Hen", num, sharedData);
        Thread th1 = new Thread(job1);
        Thread th2 = new Thread(job2);

        th1.start();
        th2.start();

        try {
            th1.join();
            th2.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        while (num-- != 0) {
            System.out.println("Human");
        }
    }
}
