package me.margotfrison.cookwriter.android.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lombok.AllArgsConstructor;
import me.margotfrison.cookwriter.R;
import me.margotfrison.cookwriter.dto.Recipe;

/**
 * An {@link RecyclerView.Adapter} to hold and display a list of {@link Recipe}
 */
@AllArgsConstructor
public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {
    private final List<Recipe> recipes;
    private final OnRecyclerClickListener<Recipe> listener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView cleanName;
        private final TextView author;
        private final OnRecyclerClickListener<Recipe> listener;
        private Recipe recipe;

        public ViewHolder(@NonNull View itemView, OnRecyclerClickListener<Recipe> listener) {
            super(itemView);
            this.listener = listener;

            cleanName = itemView.findViewById(R.id.clean_name);
            author = itemView.findViewById(R.id.author);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null && recipe != null)
                listener.onClick(recipe);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cleanName.setText(recipes.get(position).getCleanName());
        holder.author.setText(recipes.get(position).getAuthor());
        holder.recipe = recipes.get(position);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
