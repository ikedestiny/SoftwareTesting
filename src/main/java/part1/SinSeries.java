package part1;

public class SinSeries {

    public static double sinTaylor(double x, int terms) {
        // Reduce x to [-π, π] range for better convergence
        double reducedX = reduceToRange(x);
        return calculateSinSeries(reducedX, terms);
    }

    public static double sinTaylorPrecision(double x, double epsilon) {
        // Reduce x to [-π, π] range for better convergence
        double reducedX = reduceToRange(x);
        return calculateSinSeriesPrecision(reducedX, epsilon);
    }

    private static double calculateSinSeries(double x, int terms) {
        double result = 0.0;
        double term = x;

        for (int n = 1; n <= terms; n++) {
            if (n % 2 == 1) {
                result += term;
            } else {
                result -= term;
            }

            if (n < terms) {
                // Next term = current term * x^2 / ((2n)*(2n+1))
                term = term * x * x / ((2L * n) * (2L * n + 1));
            }
        }
        return result;
    }

    private static double calculateSinSeriesPrecision(double x, double epsilon) {
        double result = 0.0;
        double term = x;
        int n = 1;

        while (Math.abs(term) > epsilon) {
            if (n % 2 == 1) {
                result += term;
            } else {
                result -= term;
            }

            n++;
            term = term * x * x / ((2L * n - 2) * (2L * n - 1));

            // Safety check to prevent infinite loop
            if (n > 1000) {
                break;
            }
        }
        return result;
    }

    private static double reduceToRange(double x) {
        double TWO_PI = 2 * Math.PI;

        // Reduce to [0, 2π)
        double reduced = x % TWO_PI;
        if (reduced < 0) {
            reduced += TWO_PI;
        }

        // Reduce to [-π, π] for better Taylor convergence
        if (reduced > Math.PI) {
            reduced -= TWO_PI;
        }

        return reduced;
    }

    // For testing/debugging - version without range reduction
    public static double sinTaylorNoReduce(double x, int terms) {
        return calculateSinSeries(x, terms);
    }
}