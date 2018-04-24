package pl.piotrmacha.playground.maskedpassword.math;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Polynomial {
    private BigInteger[] coefficients;

    public Polynomial(BigInteger[] coefficients) {
        this.coefficients = coefficients;
    }

    public BigInteger evaluate(BigInteger x) {
        BigInteger result = BigInteger.ZERO;
        for (int i = 0; i < coefficients.length; ++i) {
            result = result.add(coefficients[i].multiply(x.pow(i)));
        }
        return result;
    }

    public static Polynomial random(int degree) {
        BigInteger[] coefficients = new BigInteger[degree + 1];
        for (int i = 0; i < degree + 1; ++i) {
            coefficients[i] = new BigInteger(128, new SecureRandom());
        }
        return new Polynomial(coefficients);
    }
}
