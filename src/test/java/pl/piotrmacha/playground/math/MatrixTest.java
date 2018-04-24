package pl.piotrmacha.playground.math;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import pl.piotrmacha.playground.maskedpassword.math.Matrix;

import java.math.BigInteger;

@RunWith(JUnit4.class)
public class MatrixTest {
    @Test
    public void testDeterminant() {
        // 2 x 2
        Matrix matrix1 = new Matrix(new BigInteger[][] {
                {BigInteger.valueOf(4), BigInteger.valueOf(7)},
                {BigInteger.valueOf(2), BigInteger.valueOf(1)}
        });
        Assert.assertEquals(BigInteger.valueOf(-10), matrix1.det());

        Matrix matrix2 = new Matrix(new BigInteger[][] {
                {BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(1), BigInteger.valueOf(0)},
                {BigInteger.valueOf(0), BigInteger.valueOf(3), BigInteger.valueOf(1), BigInteger.valueOf(1)},
                {BigInteger.valueOf(-1), BigInteger.valueOf(0), BigInteger.valueOf(3), BigInteger.valueOf(1)},
                {BigInteger.valueOf(3), BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(0)}
        });
        Assert.assertEquals(BigInteger.valueOf(16), matrix2.det());
    }

    @Test
    public void testSolve() {
        Matrix A = new Matrix(new BigInteger[][] {
                {BigInteger.valueOf(4), BigInteger.valueOf(2), BigInteger.valueOf(1)},
                {BigInteger.valueOf(3), BigInteger.valueOf(0), BigInteger.valueOf(2)},
                {BigInteger.valueOf(7), BigInteger.valueOf(1), BigInteger.valueOf(0)}
        });
        BigInteger[] B = new BigInteger[] {
            BigInteger.valueOf(27), BigInteger.valueOf(11), BigInteger.valueOf(28)
        };

        BigInteger[] X = new BigInteger[] {
            BigInteger.valueOf(3), BigInteger.valueOf(7), BigInteger.valueOf(1)
        };

        Assert.assertEquals(X, A.solve(B));
    }
}
