package pl.piotrmacha.playground.maskedpassword;

import pl.piotrmacha.playground.maskedpassword.math.Matrix;
import pl.piotrmacha.playground.maskedpassword.math.Point;
import pl.piotrmacha.playground.maskedpassword.math.Polynomial;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Password {
    private static final int PASSWORD_MAX_LENGTH = 32;
    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_CHARACTERS_NEEDED_TO_APPROVE = 6;

    private final BigInteger intersection;
    private final Point[] points;

    public Password(BigInteger intersection, Point[] points) {
        this.intersection = intersection;
        this.points = points;
    }

    public Authenticator requestAuthentication() {
        return new Authenticator();
    }

    public static Password create(String password) {
        if (password.length() < Password.PASSWORD_MIN_LENGTH) {
            throw new RuntimeException("Password too short");
        }
        if (password.length() > Password.PASSWORD_MAX_LENGTH) {
            throw new RuntimeException("Password too long");
        }

        Polynomial polynomial = Polynomial.random(Password.PASSWORD_CHARACTERS_NEEDED_TO_APPROVE - 1);

        List<Point> points = password.codePoints().mapToObj(ascii -> {
            BigInteger x = new BigInteger(128, new SecureRandom());
            return new Point(x, polynomial.evaluate(x).subtract(BigInteger.valueOf(ascii)));
        }).collect(Collectors.toList());
        BigInteger intersection = polynomial.evaluate(BigInteger.ZERO);

        return new Password(intersection, points.toArray(new Point[password.length()]));
    }

    public class Authenticator {
        private int[] characterPositions;

        Authenticator() {
            characterPositions = new int[Password.PASSWORD_CHARACTERS_NEEDED_TO_APPROVE];
            generateCharacterPositions();
        }

        public int[] getCharacterPositions() {
            return this.characterPositions;
        }

        public boolean verify(char[] characters) {
            if (characters.length != characterPositions.length) {
                throw new RuntimeException("Invalid amount of characters provided");
            }

            Point[] maybePoints = new Point[Password.PASSWORD_CHARACTERS_NEEDED_TO_APPROVE];
            for (int i = 0; i < characterPositions.length; ++i) {
                int position = characterPositions[i] - 1;
                Point found = points[position];
                maybePoints[i] = new Point(found.x(), found.y().add(BigInteger.valueOf((int) characters[i])));
            }

            BigInteger[][] matrixTemplate = new BigInteger[maybePoints.length][maybePoints.length];
            for (int i = 0; i < maybePoints.length; ++i) {
                Point point = maybePoints[i];
                for (int j = 0; j < maybePoints.length; ++j) {
                    matrixTemplate[i][j] = point.x().pow(Password.PASSWORD_CHARACTERS_NEEDED_TO_APPROVE - 1 - j);
                }
            }

            Matrix A = new Matrix(matrixTemplate);
            BigInteger[] B = new BigInteger[maybePoints.length];
            for (int i = 0; i < maybePoints.length; ++i) {
                B[i] = maybePoints[i].y();
            }

            BigInteger[] solvedCoefficients = A.solve(B);
            BigInteger guessedIntersection = solvedCoefficients[solvedCoefficients.length - 1];

            return guessedIntersection.equals(intersection);
        }

        private void generateCharacterPositions() {
            List<Integer> available = IntStream.rangeClosed(1, points.length).boxed().collect(Collectors.toList());
            Random prng = new Random();
            for (int i = 0; i < Password.PASSWORD_CHARACTERS_NEEDED_TO_APPROVE; ++i) {
                characterPositions[i] = available.remove(prng.nextInt(available.size()));
            }
        }
    }
}
