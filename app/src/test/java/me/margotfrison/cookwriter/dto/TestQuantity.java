package me.margotfrison.cookwriter.dto;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import me.margotfrison.cookwriter.exceptions.InvalidQuantityFormatException;

@RunWith(MockitoJUnitRunner.class)
public class TestQuantity {
    @Test
    public void test_from_noUnitInteger() throws InvalidQuantityFormatException {
        Quantity quantity = Quantity.from("5");
        Assert.assertEquals(quantity.getNumerator(), 5);
        Assert.assertEquals(quantity.getDenominator(), 1);
        Assert.assertNull(quantity.getUnit());
    }

    @Test
    public void test_from_noUnitFraction() throws InvalidQuantityFormatException {
        Quantity quantity = Quantity.from("2/3");
        Assert.assertEquals(quantity.getNumerator(), 2);
        Assert.assertEquals(quantity.getDenominator(), 3);
        Assert.assertNull(quantity.getUnit());
    }

    @Test
    public void test_from_unitInteger() throws InvalidQuantityFormatException {
        Quantity quantity = Quantity.from("20 cl");
        Assert.assertEquals(quantity.getNumerator(), 20);
        Assert.assertEquals(quantity.getDenominator(), 1);
        Assert.assertEquals(quantity.getUnit(), "cl");
    }

    @Test
    public void test_from_unitFraction() throws InvalidQuantityFormatException {
        Quantity quantity = Quantity.from("1/2 kg");
        Assert.assertEquals(quantity.getNumerator(), 1);
        Assert.assertEquals(quantity.getDenominator(), 2);
        Assert.assertEquals(quantity.getUnit(), "kg");
    }
}
