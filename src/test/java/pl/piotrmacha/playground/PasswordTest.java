package pl.piotrmacha.playground;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import pl.piotrmacha.playground.maskedpassword.Password;

@RunWith(JUnit4.class)
public class PasswordTest {
    @Test
    public void testPasswordCreationAndVerificationForValidPassword() {
        final String passphrase = "AleMaKotaIPsa";

        Password password = Password.create(passphrase);
        Password.Authenticator authenticator = password.requestAuthentication();

        int[] positions = authenticator.getCharacterPositions();
        char[] characters = new char[positions.length];
        for (int i = 0; i < positions.length; ++i) {
            characters[i] = passphrase.charAt(positions[i] - 1);
        }

        Assert.assertTrue(authenticator.verify(characters));
    }
    
    @Test
    public void testPasswordCreationAndVerificationForInvalidPassword() {
        Password password = Password.create("AlaMaKotaIPsa");
        Password.Authenticator authenticator = password.requestAuthentication();
        
        Assert.assertFalse(authenticator.verify(new char[] {'N', 'o', 't', 'a', 'p', 'W'}));
    }
}
