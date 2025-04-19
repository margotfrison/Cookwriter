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
    private String name; // meant to describe the original/clean recipe name (ex: Boeuf bourguignon)
    private String customName; // meant for the user to give a funny name to their recipe (ex: Grandma's boeuf bourguignon)
    private String description;
    private String source;
    private Duration totalDuration;
    private Season season;
    private Difficulty difficulty;
    private Price price;
    private List<String> categories;
    private List<String> labels;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private boolean done;
}
