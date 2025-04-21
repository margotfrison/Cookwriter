package me.margotfrison.cookwriter.android.utils;

import me.margotfrison.cookwriter.android.App;

/**
 * Give access to {@link android.content.res.Resources} anywhere in the app statically
 */
public class AndroidResources {
    /**
     * Get a {@link android.content.res.Resources} string from its id
     * @param res the {@link android.content.res.Resources}' id
     * @return the {@link String} corresponding to this id
     */
    public static String getString(int res) {
        return App.getContext().getString(res);
    }
}
