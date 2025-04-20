package me.margotfrison.cookwriter.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.margotfrison.cookwriter.R;
import me.margotfrison.cookwriter.android.App;
import me.margotfrison.cookwriter.android.components.OnRecyclerClickListener;
import me.margotfrison.cookwriter.android.components.RecipeListAdapter;
import me.margotfrison.cookwriter.dao.DaoUtil;
import me.margotfrison.cookwriter.dao.RecipeRepository;
import me.margotfrison.cookwriter.dto.Recipe;

/**
 * {@link AppCompatActivity} to list recipes in the database
 */
public class RecipeListActivity extends AppCompatActivity implements OnRecyclerClickListener<Recipe>, View.OnClickListener {
    private ImageButton createRecipe;
    private RecyclerView recyclerView;

    private RecipeRepository recipeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        setSupportActionBar(findViewById(R.id.toolbar));

        createRecipe = findViewById(R.id.create_recipe);
        recyclerView = findViewById(R.id.recycler_view);
        recipeRepository = App.getDatabase().recipeRepository();

        createRecipe.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        DaoUtil.subscribeUIThread(recipeRepository.getAllAsPreview(),
                (recipes) -> recyclerView.setAdapter(new RecipeListAdapter(recipes, this)),
                this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DaoUtil.disposeAll(this);
    }

    @Override
    public void onClick(Recipe recipePreview) {
        Intent intent = new Intent(this, RecipeReadonlyActivity.class);
        intent.putExtra(Recipe.Fields.author, recipePreview.getAuthor());
        intent.putExtra(Recipe.Fields.customName, recipePreview.getCustomName());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v == createRecipe) {
            Intent intent = new Intent(this, RecipeWriteActivity.class);
            intent.putExtra(RecipeWriteActivity.NEW_RECIPE_EXTRA_KEY, true);
            startActivity(intent);
        }
    }
}
