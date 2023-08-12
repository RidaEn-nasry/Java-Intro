import java.util.Scanner;

class Program {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        long res = 0;

        while (input.hasNextLong()) {
            long num = input.nextLong();
            if (num == 42) {
                break;
            }
            int sum = 0;
            for (long i = num; i > 0; i /= 10)
                sum += (i % 10);
            boolean prime = true;
            if (sum <= 2) {
                continue;
            } else {
                for (int i = 2; i <= sum / 2; i++) {
                    if (sum % i == 0) {
                        prime = false;
                        break;
                    }
                }
                if (prime)
                    res += 1;
            }
        }
        input.close();
        System.out.println("Count of coffee-request : " + res);
    }
}