package me.margotfrison.cookwriter.dto;

import java.time.Duration;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Recipe {
    private String name;
    private String description;
    private String source;
    private Duration totalDuration;
    private Season season;
    private List<String> categories;
    private List<String> labels;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private boolean done;
}
