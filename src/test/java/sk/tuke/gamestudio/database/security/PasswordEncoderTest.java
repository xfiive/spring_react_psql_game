package sk.tuke.gamestudio.database.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordEncoderTest {

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void getPasswordEncoder() {
        this.passwordEncoder = new PasswordEncoder();
    }

    @Test
    void testGetHashedPassword() throws NoSuchAlgorithmException {
        String password = "password123";
        String expectedHash = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";

        String actualHash = passwordEncoder.getHashedPassword(password);

        assertNotEquals(expectedHash, actualHash);
    }

    @Test
    void testComparePassword() throws NoSuchAlgorithmException {
        String password = "password123";
        String hash = passwordEncoder.getHashedPassword(password);

        boolean result = new PasswordEncoder().comparePassword(hash, password);

        assertTrue(result);
    }

    @Test
    void testComparePassword_WrongPassword() throws NoSuchAlgorithmException {
        String correctPassword = "password123";
        String wrongPassword = "wrongpassword";
        String hash = passwordEncoder.getHashedPassword(correctPassword);

        boolean correctResult = new PasswordEncoder().comparePassword(hash, correctPassword);
        boolean wrongResult = new PasswordEncoder().comparePassword(hash, wrongPassword);

        assertTrue(correctResult);
        assertFalse(wrongResult);
    }

    @Test
    void testComparePassword_NullHash() {
        String password = "password123";
        String nullHash = null;

        assertThrows(IllegalArgumentException.class, () -> passwordEncoder.comparePassword(nullHash, password));
    }

    @Test
    void testComparePassword_NullPasswordEntered() throws NoSuchAlgorithmException {
        assertThrows(IllegalArgumentException.class, () -> new PasswordEncoder().comparePassword("someHash", null));
    }

    @Test
    void testGetHashedPassword_NullPassword() {
        assertThrows(IllegalArgumentException.class, () -> passwordEncoder.getHashedPassword(null));
    }
}
