package day03.ex00;


class Program {

    public static class myRunnable implements Runnable {
        private Integer runNUm = 0;
        private String word;
        myRunnable(Integer runNum, String word) {
            this.runNUm = runNum;
            this.word = word;
        }
        public void run() {
            while (runNUm-- != 0) {
                System.out.println(word);
            }
        }
    }

    public static void main(String[] args) {
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

        Runnable job1 = new myRunnable(num, "Egg");
        Runnable job2 = new myRunnable(num, "Hen");
        Thread thread1 = new Thread(job1);
        Thread thread2 = new Thread(job2);
        thread1.start();
        thread2.start();
        // wait for threads to end
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }

        while (num-- != 0) {
            System.out.println("Human");
        }
    }
}

