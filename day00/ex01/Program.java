import java.util.Scanner;

class Program {

    public static int sqrt(int num) {
        int sqrt = 0;
        for (int i = 1; i <= num / 2; i++) {
            if (i * i == num) {
                sqrt = i;
                break;
            } else if (i * i > num) {
                sqrt = i - 1;
                break;
            }
        }
        return sqrt;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int num = input.nextInt();
        if (num <= 0 || num == 1) {
            System.err.println("IllegalArgument");
            System.exit(-1);
        } else {
            int steps = 1;
            boolean prime = true;
            int sqrtNum = (int) sqrt(num);
            for (int i = 2; i <= sqrtNum; i += 1, steps += 1) {
                if (num % i == 0) {
                    prime = false;
                    break;
                }
            }
            if (prime) {
                System.out.println("true: " + steps);
            } else {
                System.out.println("false: " + steps);
            }
        }
        input.close();
        System.exit(0);
    }
}
