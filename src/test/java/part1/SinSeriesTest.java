package part1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static part1.SinSeries.*;

public class SinSeriesTest {

    @Test
    @DisplayName("Test sin(0) = 0")
    public void testSinZero() {
        assertEquals(0.0, sinTaylor(0, 10), 1e-10);
        assertEquals(0.0, sinTaylorPrecision(0, 1e-10), 1e-10);
    }

    @ParameterizedTest
    @CsvSource({
            "0.0, 0.0",
            "1.57079632679, 1.0",  // π/2
            "3.14159265359, 0.0",  // π
            "4.71238898, -1.0",    // 3π/2
            "6.28318530718, 0.0"   // 2π
    })
    @DisplayName("Test special angles")
    public void testSpecialAngles(double input, double expected) {
        // Use more terms for better accuracy
        assertEquals(expected, sinTaylor(input, 30), 1e-5);
        assertEquals(expected, sinTaylorPrecision(input, 1e-10), 1e-5);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.1, 0.5, 1.0, 1.5, 2.0})
    @DisplayName("Test random values against Math.sin")
    public void testAgainstMathSin(double input) {
        double expected = Math.sin(input);
        assertEquals(expected, sinTaylor(input, 20), 1e-8);
        assertEquals(expected, sinTaylorPrecision(input, 1e-10), 1e-8);
    }

    @Test
    @DisplayName("Test convergence with increasing number of terms")
    public void testConvergence() {
        double x = 2.0;
        double exact = Math.sin(x);

        double prevError = Double.MAX_VALUE;

        // Test that error decreases as we add more terms
        for (int terms = 1; terms <= 10; terms++) {
            double result = sinTaylor(x, terms);
            double error = Math.abs(result - exact);

            // Each iteration should generally improve accuracy
            // (not strictly monotonic, but trend should be decreasing)
            if (terms > 1) {
                assertTrue(error <= prevError * 1.1, // Allow small increases due to oscillations
                        "Error increased from " + prevError + " to " + error + " at terms=" + terms);
            }

            prevError = error;
        }

        // Final accuracy should be good
        double finalResult = sinTaylor(x, 20);
        assertEquals(exact, finalResult, 1e-8);
    }

    @Test
    @DisplayName("Test periodic property")
    public void testPeriodicity() {
        double x1 = 1.0;
        double x2 = 1.0 + 2 * Math.PI;

        // Use more terms for large x values
        double result1 = sinTaylor(x1, 30);
        double result2 = sinTaylor(x2, 30);

        assertEquals(result1, result2, 1e-5);

        // Also test with precision-based method
        assertEquals(sinTaylorPrecision(x1, 1e-10),
                sinTaylorPrecision(x2, 1e-10), 1e-5);
    }

    @Test
    @DisplayName("Test symmetry property sin(-x) = -sin(x)")
    public void testSymmetry() {
        double x = 1.5;
        assertEquals(-sinTaylor(x, 20), sinTaylor(-x, 20), 1e-10);
        assertEquals(-sinTaylorPrecision(x, 1e-10),
                sinTaylorPrecision(-x, 1e-10), 1e-10);
    }

    @Test
    @DisplayName("Test large angles")
    public void testLargeAngles() {
        double x = 100.0;
        double expected = Math.sin(x);

        // For large x, we need more terms or range reduction
        assertEquals(expected, sinTaylor(x, 50), 1e-5);
        assertEquals(expected, sinTaylorPrecision(x, 1e-8), 1e-5);
    }
}