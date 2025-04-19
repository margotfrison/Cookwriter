package me.margotfrison.cookwriter.utils;

public class AndroidResources {
    public static String getString(int res) {
        return android.content.res.Resources.getSystem().getString(res);
    }
}
