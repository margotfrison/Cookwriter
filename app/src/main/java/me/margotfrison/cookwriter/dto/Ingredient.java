package me.margotfrison.cookwriter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Ingredient {
    private Quantity quantity;
    private String name;
    private boolean optional;
}
