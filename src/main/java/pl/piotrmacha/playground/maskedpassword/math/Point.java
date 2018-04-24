package pl.piotrmacha.playground.maskedpassword.math;

import java.math.BigInteger;

public class Point {
    private final BigInteger x;
    private final BigInteger y;

    public Point(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    public BigInteger x()
    {
        return this.x;
    }

    public BigInteger y()
    {
        return this.y;
    }
}
