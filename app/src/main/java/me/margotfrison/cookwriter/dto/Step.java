package me.margotfrison.cookwriter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Step {
    private String stepDescriptions;
}
