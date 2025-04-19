package me.margotfrison.cookwriter.dto;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.margotfrison.cookwriter.R;
import me.margotfrison.cookwriter.exceptions.InvalidQuantityFormatException;

@Data
@Builder
@AllArgsConstructor
public class Quantity {
    private int numerator;
    private int denominator;
    private String unit; // null means "to the unit" (i.e. 2 onions -> unit is null)

    @Override
    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        double doubleQuantity = (double) denominator / (double) numerator;
        if (doubleQuantity % 1 == 0) // computed quantity is an int
            sb.append(Math.round(doubleQuantity));
        else
            sb.append(numerator).append('/').append(denominator);
        if (unit != null)
            sb.append(' ').append(unit);
        return sb.toString();
    }

    public static Quantity from(String str) throws InvalidQuantityFormatException {
        // Parse unit, if it exist
        String fraction = str;
        String unit = null;
        int firstSpace = str.indexOf(' ');
        if (firstSpace >= 0) {
            fraction = str.substring(0, firstSpace);
            unit = str.substring(firstSpace + 1);
        }

        // Parse the fraction
        if (fraction.indexOf('/') > 0) {
            String[] fractionNumbers = fraction.split("/");
            if (fractionNumbers.length != 2)
                throw new InvalidQuantityFormatException(R.string.err_quantity_invalid_fraction, fraction);
            try {
                return Quantity.builder()
                        .numerator(Integer.parseInt(fractionNumbers[0]))
                        .denominator(Integer.parseInt(fractionNumbers[1]))
                        .unit(unit)
                        .build();
            } catch (NumberFormatException e) {
                throw new InvalidQuantityFormatException(R.string.err_quantity_invalid_fraction, fraction, e);
            }
        } else {
            try {
                return Quantity.builder()
                        .numerator(Integer.parseInt(fraction))
                        .denominator(1)
                        .unit(unit)
                        .build();
            } catch (NumberFormatException e) {
                throw new InvalidQuantityFormatException(R.string.err_quantity_invalid_integer, fraction, e);
            }
        }
    }
}
