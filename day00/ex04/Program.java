import java.util.Scanner;

class Program {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();
        // create an array to store the counts of each character
        // since bmp is 0-65535, we need an array of size 65536
        int[] counts = new int[65536];

        // get frequency of each character
        // and get the max count

        int maxCount = 0;
        for (char c : input.toCharArray()) {
            counts[c]++;
            if (counts[c] > maxCount) {
                maxCount = counts[c];
            }
        }

        // get the scale factor of the histogram
        double scale = 1;
        if (maxCount > 10) {
            scale = (double) maxCount / 10.0;
        }

        char[] chars = new char[10];
        int[] topCounts = new int[10];

        // get the top 10 characters and their counts
        for (int i = 0; i < 10; i++) {
            int max = 0;
            int maxIndex = 0;
            for (int j = 0; j < counts.length; j++) {
                if (counts[j] > max) {
                    max = counts[j];
                    maxIndex = j;
                }
            }
            chars[i] = (char) maxIndex;
            topCounts[i] = max;
            counts[maxIndex] = 0;
        }

        // start from 11 to print the numbers
        for (int i = 11; i > 0; i--) {

            for (int j = 0; j < 10; j++) {
                if (chars[j] == 0) {
                    continue;
                }
                if ((int) ((topCounts[j] / scale) + 1) == i) {
                    System.out.print(topCounts[j] + " ");
                } else if (topCounts[j] / scale >= i) {
                    System.out.print("# ");
                } else {
                    System.out.print("  ");
                }
            }

            System.out.println();
        }

        // print characters at the bottom of the histogram
        for (char c : chars) {
            if (c == 0) {
                continue;
            }
            System.out.print(c + " ");
        }
    }
}
