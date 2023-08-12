import java.util.Scanner;

class Program {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int index = 1;
        long res = 0;
        while (input.hasNextLine()) {
            String line = input.nextLine();
            if (line.equals("42")) {
                break;
            }
            String week = "Week " + index;
            if (line.equals(week)) {
                int min = 10;
                for (int i = 0; i < 5; i++) {
                    int num = input.nextInt();
                    if (num < min) {
                        min = num;
                    }
                }
                input.nextLine(); // to skip the new line of numbers
                res *= 10;
                res += min;
                index++;
            } else {
                System.err.println("IllegalArgument");
                System.exit(-1);
            }
        }
        input.close();
        // reversing the long
        long reversedRes = 0;
        while (res > 0) {
            reversedRes = reversedRes * 10 + res % 10;
            res /= 10;
        }
        // printing the result
        int week = 1;
        while (reversedRes > 0) {
            long min = reversedRes % 10;
            reversedRes /= 10;
            System.out.print("Week " + week + " ");
            for (int i = 0; i < min; i++) {
                System.out.print("=");
            }
            System.out.println(">");
            week += 1;
        }
        System.exit(0);
    }
}
