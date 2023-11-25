
package fr._42.numbers;

import fr._42.numbers.NumberWorker;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.math.BigDecimal;

public class NumberWorkerTest {

    private NumberWorker worker = new NumberWorker();

    @ParameterizedTest
    @ValueSource(ints = { 2, 3, 5, 13441, 25253 })
    public void isPrimeForPrimes(int number) {
        assertEquals(true, worker.isPrime(number));
        assertEquals(true, worker.isPrime(number));
        assertEquals(true, worker.isPrime(number));
        assertEquals(true, worker.isPrime(number));
        assertEquals(true, worker.isPrime(number));
    }

    @Test
    public void isPrimeForNotPrimes() {
        assertEquals(false, worker.isPrime(1));
        assertEquals(false, worker.isPrime(4));
        assertEquals(false, worker.isPrime(6));
        assertEquals(false, worker.isPrime(13442));
        assertEquals(false, worker.isPrime(25254));
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, 0, 1 })
    public void isPrimeForIncorrectNumbers(int number) {
        assertEquals(false, worker.isPrime(number));
        assertEquals(false, worker.isPrime(number));
        assertEquals(false, worker.isPrime(number));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/data.csv")
    public void digitsum(int number, int sum) {
        assertEquals(sum, worker.digitsum(number));
    }
}
