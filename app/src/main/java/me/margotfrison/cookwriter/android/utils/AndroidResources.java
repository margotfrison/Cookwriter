package me.margotfrison.cookwriter.android.utils;

import me.margotfrison.cookwriter.android.App;

public class AndroidResources {
    public static String getString(int res) {
        return App.getContext().getString(res);
    }
}
