package me.margotfrison.cookwriter.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import me.margotfrison.cookwriter.exceptions.InvalidQuantityFormatException;

@RunWith(MockitoJUnitRunner.class)
public class TestQuantity {
    @Test
    public void test_from_noUnitInteger() throws InvalidQuantityFormatException {
        Quantity quantity = Quantity.from("5");
        assertEquals(quantity.getNumerator(), 5);
        assertEquals(quantity.getDenominator(), 1);
        assertNull(quantity.getUnit());
    }

    @Test
    public void test_from_noUnitFraction() throws InvalidQuantityFormatException {
        Quantity quantity = Quantity.from("2/3");
        assertEquals(quantity.getNumerator(), 2);
        assertEquals(quantity.getDenominator(), 3);
        assertNull(quantity.getUnit());
    }

    @Test
    public void test_from_unitInteger() throws InvalidQuantityFormatException {
        Quantity quantity = Quantity.from("20 cl");
        assertEquals(quantity.getNumerator(), 20);
        assertEquals(quantity.getDenominator(), 1);
        assertEquals(quantity.getUnit(), "cl");
    }

    @Test
    public void test_from_unitFraction() throws InvalidQuantityFormatException {
        Quantity quantity = Quantity.from("1/2 kg");
        assertEquals(quantity.getNumerator(), 1);
        assertEquals(quantity.getDenominator(), 2);
        assertEquals(quantity.getUnit(), "kg");
    }
}
