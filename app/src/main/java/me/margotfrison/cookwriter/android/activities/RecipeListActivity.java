package me.margotfrison.cookwriter.android.activities;

import android.content.Intent;
import android.os.Bundle;

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

public class RecipeListActivity extends AppCompatActivity implements OnRecyclerClickListener<Recipe> {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        setSupportActionBar(findViewById(R.id.toolbar));

        recyclerView = findViewById(R.id.recycler_view);
        RecipeRepository recipeRepository = App.getDatabase().recipeRepository();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        Intent intent = new Intent(this, RecipeWriteActivity.class);
        intent.putExtra(Recipe.Fields.author, recipePreview.getAuthor());
        intent.putExtra(Recipe.Fields.customName, recipePreview.getCustomName());
        startActivity(intent);
    }
}
