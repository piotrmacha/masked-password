package pl.piotrmacha.playground.maskedpassword.math;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * This class is not written safely, use with caution.
 */
public class Matrix {
    private BigInteger[][] matrix;

    public Matrix(BigInteger[][] matrix) {
        this.matrix = matrix;
    }

    private BigInteger[][] copy() {
        BigInteger[][] copy = new BigInteger[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; ++i) {
            System.arraycopy(matrix[i], 0, copy[i], 0, matrix[0].length);
        }
        return copy;
    }

    public Matrix replaceColumn(int col, BigInteger[] column) {
        BigInteger[][] copy = copy();
        for (int i = 0; i < matrix.length; ++i) {
            copy[i][col - 1] = column[i];
        }
        return new Matrix(copy);
    }

    public BigInteger cofactor(int col, int row) {
        BigInteger[][] newMatrix = new BigInteger[matrix.length - 1][matrix[0].length - 1];
        for (int r = 0, rr = 0; r < matrix.length; ++r) {
            if (r == row - 1) {
                continue;
            }
            for (int c = 0, cc = 0; c < matrix[0].length; ++c) {
                if (c == col - 1) {
                    continue;
                }
                newMatrix[rr][cc] = matrix[r][c];
                cc++;
            }
            rr++;
        }
        return new Matrix(newMatrix).det();
    }

    public BigInteger det() {
        if (matrix.length != matrix[0].length) {
            throw new RuntimeException("Det can be computed only for square matrices");
        }

        if (matrix.length == 2) {
            return matrix[0][0]
                    .multiply(matrix[1][1])
                    .subtract(
                            matrix[0][1].multiply(matrix[1][0])
                    );
        }

        BigInteger result = BigInteger.ZERO;
        for (int row = 1; row <= matrix.length; ++row) {
            BigInteger element =
                    BigInteger.ONE.negate().pow(row + 1).multiply(
                            matrix[row - 1][0].multiply(
                                    cofactor(1, row)
                            )
                    );
            result = result.add(element);
        }
        return result;
    }

    public BigInteger[] solve(BigInteger[] B) {
        BigInteger[] result = new BigInteger[B.length];

        BigInteger detA = det();
        if (detA.equals(BigInteger.ZERO)) {
            throw new RuntimeException("No or infinite solutions");
        }

        for (int i = 0; i < matrix.length; i++) {
            Matrix Ai = replaceColumn(i + 1, B);
            BigInteger detAi = Ai.det();
            // we don't need decimal I think in the project's use case
            result[i] = detAi.divide(detA);
        }

        return result;
    }
}
