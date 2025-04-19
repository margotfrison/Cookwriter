package me.margotfrison.cookwriter.dto;

import lombok.AllArgsConstructor;
import me.margotfrison.cookwriter.R;
import me.margotfrison.cookwriter.utils.AndroidResources;

@AllArgsConstructor
public enum Price {
    FREE(R.string.price_free), // Can be done with only already available and common commodities in the kitchen (Ex: fleur de sel made from salt)
    LOW_BUDGET(R.string.price_low_budget),
    AFFORDABLE(R.string.price_affordable),
    EXPENSIVE(R.string.price_expensive),
    TOP_OF_THE_SHELF(R.string.price_top_of_the_shelf); // Recipe can only be done with top-of-the-shelf or unique ingredients that cost a fortune to get

    public final int stringRes;

    public String getString() {
        return AndroidResources.getString(stringRes);
    }
}
