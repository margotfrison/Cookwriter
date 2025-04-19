package me.margotfrison.cookwriter.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Step {
    private List<String> stepDescriptions;
}
