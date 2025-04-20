package me.margotfrison.cookwriter.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Duration;

import me.margotfrison.cookwriter.exceptions.InvalidTimeFormatException;

@RunWith(AndroidJUnit4.class)
public class TestDurationConverter {
    @Test
    public void test_parseString_valid() throws InvalidTimeFormatException {
        Duration duration = DurationConverter.getInstance().parseString("1d 2h 3m 4s");
        assertEquals(93784, duration.getSeconds()); // 93784 = (1 * 24 * 60 * 60) + (2 * 60 * 60) + (3 * 60 + 4)
    }

    @Test
    public void test_parseString_invalid() {
        assertThrows(InvalidTimeFormatException.class, () -> DurationConverter.getInstance().parseString("invalid"));
        assertThrows(InvalidTimeFormatException.class, () -> DurationConverter.getInstance().parseString("1t 2h 3m 4s"));
    }

    @Test
    public void test_fromString_valid() {
        Duration duration = DurationConverter.getInstance().fromString("1d 2h 3m 4s");
        assertEquals(93784, duration.getSeconds()); // 93784 = (1 * 24 * 60 * 60) + (2 * 60 * 60) + (3 * 60 + 4)
    }

    @Test
    public void test_fromString_invalid() {
        assertNull(DurationConverter.getInstance().fromString("invalid"));
        assertNull(DurationConverter.getInstance().fromString("1t 2h 3m 4s"));
    }

    @Test
    public void test_checkString_valid() {
        assertTrue(DurationConverter.getInstance().checkString("1d 2h 3m 4s"));
    }

    @Test
    public void test_checkString_invalid() {
        assertFalse(DurationConverter.getInstance().checkString("invalid"));
        assertFalse(DurationConverter.getInstance().checkString("1t 2h 3m 4s"));
    }

    @Test
    public void test_toString() {
        assertEquals("1d 2h 3m 4s", DurationConverter.getInstance().toString(Duration.ofSeconds(93784))); // 93784 = (1 * 24 * 60 * 60) + (2 * 60 * 60) + (3 * 60 + 4)
    }
}
