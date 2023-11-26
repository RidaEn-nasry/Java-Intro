package fr._42.numbers;

import java.math.BigDecimal;

public class NumberWorker {

    public boolean isPrime(int number) {
        BigDecimal n = BigDecimal.valueOf(number);
        // if num is less than 2, it is not prime
        if (n.compareTo(BigDecimal.valueOf(2)) < 0) {
            return false;
        }
        // if num is divisible by any num2 in range [2, num/2], num is not prime
        BigDecimal half = n.divide(BigDecimal.valueOf(2));
        BigDecimal i = BigDecimal.valueOf(2);
        while (i.compareTo(half) <= 0) {
            if (n.remainder(i).compareTo(BigDecimal.ZERO) == 0) {
                return false;
            }
            i = i.add(BigDecimal.ONE);
        }
        return true;
    }

    public int digitsum(int number) {
        if (number <= 1) {
            throw new IllegalArgumentException("Number must be greater than 1");
        }
        int sum = 0;
        while (number > 0) {
            sum += number % 10;
            number /= 10;
        }
        return sum;
    }

    public static void main(String args[]) {
        System.out.println("Hello World!");
    }
}
