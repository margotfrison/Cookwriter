package me.margotfrison.cookwriter.dto;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import me.margotfrison.cookwriter.R;
import me.margotfrison.cookwriter.android.utils.AndroidResources;

@AllArgsConstructor
public enum Price {
    FREE(R.string.price_free, 0), // Can be done with only already available and common commodities in the kitchen (Ex: fleur de sel made from salt)
    LOW_BUDGET(R.string.price_low_budget, 1),
    AFFORDABLE(R.string.price_affordable, 2),
    EXPENSIVE(R.string.price_expensive, 3),
    TOP_OF_THE_SHELF(R.string.price_top_of_the_shelf, 4); // Recipe can only be done with top-of-the-shelf or unique ingredients that can cost a fortune to get

    public final int stringRes;
    public final int spinnerIndex;

    @NonNull
    @Override
    public String toString() {
        return AndroidResources.getString(stringRes);
    }
}
