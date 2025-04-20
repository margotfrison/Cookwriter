package me.margotfrison.cookwriter.android.components;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

import lombok.AllArgsConstructor;

/**
 * {@link View.OnTouchListener} designed to allow an EditText to be scrollable even inside a scrollable layout
 */
@AllArgsConstructor
public class EditTextScrollable implements View.OnTouchListener {
    private final int id;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == id) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }
}
