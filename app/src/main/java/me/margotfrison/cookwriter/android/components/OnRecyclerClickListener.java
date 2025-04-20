package me.margotfrison.cookwriter.android.components;

/**
 * Listener that return the item clicked in a custom {@link androidx.recyclerview.widget.RecyclerView.Adapter}
 * @param <T> the type of objects that are contained in the {@link androidx.recyclerview.widget.RecyclerView.Adapter}
 */
public interface OnRecyclerClickListener<T> {
    void onClick(T item);
}
