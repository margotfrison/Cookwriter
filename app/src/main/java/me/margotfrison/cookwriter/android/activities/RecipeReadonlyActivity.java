package me.margotfrison.cookwriter.android.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Optional;

import me.margotfrison.cookwriter.R;
import me.margotfrison.cookwriter.android.App;
import me.margotfrison.cookwriter.android.components.StepReadonlyComponent;
import me.margotfrison.cookwriter.dao.DaoUtil;
import me.margotfrison.cookwriter.dao.DurationConverter;
import me.margotfrison.cookwriter.dao.RecipeRepository;
import me.margotfrison.cookwriter.dto.Recipe;
import me.margotfrison.cookwriter.dto.Step;

public class RecipeReadonlyActivity extends AppCompatActivity implements View.OnClickListener {
    private Recipe recipe;
    private ImageButton editRecipe;
    private TextView cleanName;
    private TextView customName;
    private TextView description;
    private TextView source;
    private TextView totalDuration;
    private TextView season;
    private TextView difficulty;
    private TextView price;
    private LinearLayout steps;

    private RecipeRepository recipeRepository;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_readonly);
        setSupportActionBar(findViewById(R.id.toolbar));

        editRecipe = findViewById(R.id.delete_recipe);
        cleanName = findViewById(R.id.clean_name);
        customName = findViewById(R.id.custom_name);
        description = findViewById(R.id.description);
        source = findViewById(R.id.source);
        totalDuration = findViewById(R.id.total_duration);
        season = findViewById(R.id.season);
        difficulty = findViewById(R.id.difficulty);
        price = findViewById(R.id.price);
        steps = findViewById(R.id.steps);
        recipeRepository = App.getDatabase().recipeRepository();

        editRecipe.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String recipePreviewAuthor = getIntent().getStringExtra(Recipe.Fields.author);
        String recipePreviewCustomName = getIntent().getStringExtra(Recipe.Fields.customName);

        if (recipePreviewAuthor != null && recipePreviewCustomName != null) {
            DaoUtil.subscribeUIThread(
                    recipeRepository.exists(recipePreviewAuthor, recipePreviewCustomName),
                    (exists) -> {
                        if (exists) {
                            DaoUtil.subscribeUIThread(
                                    recipeRepository.get(recipePreviewAuthor, recipePreviewCustomName),
                                    (recipe) -> {
                                        this.recipe = recipe;
                                        cleanName.setText(recipe.getCleanName());
                                        customName.setText(recipe.getCustomName());
                                        description.setText(recipe.getDescription());
                                        source.setText(recipe.getSource());
                                        totalDuration.setText(DurationConverter.getInstance().toString(recipe.getTotalDuration()));
                                        season.setText(recipe.getSeason().toString());
                                        difficulty.setText(recipe.getDifficulty().toString());
                                        price.setText(recipe.getPrice().toString());
                                        addSteps(recipe.getSteps());
                                    },
                                    this);
                        } else {
                            finish();
                        }
                    },
                    this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DaoUtil.disposeAll(this);
    }

    private void addSteps(@Nullable List<Step> stepList) {
        if (stepList == null) {
            return;
        }
        for (int i = 0; i < stepList.size(); i++) {
            StepReadonlyComponent stepWriteComponent = new StepReadonlyComponent(this,
                    i,
                    Optional.ofNullable(stepList.get(i)).map(Step::getStepDescriptions).orElse(null));
            steps.addView(stepWriteComponent);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == editRecipe) {
            Intent intent = new Intent(this, RecipeWriteActivity.class);
            intent.putExtra(Recipe.Fields.author, recipe.getAuthor());
            intent.putExtra(Recipe.Fields.customName, recipe.getCustomName());
            intent.putExtra(RecipeWriteActivity.NEW_RECIPE_EXTRA_KEY, false);
            startActivity(intent);
        }
    }
}
