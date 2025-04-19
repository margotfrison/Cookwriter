package me.margotfrison.cookwriter.android.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.vavr.control.Try;
import me.margotfrison.cookwriter.R;
import me.margotfrison.cookwriter.android.App;
import me.margotfrison.cookwriter.android.components.EditTextScrollable;
import me.margotfrison.cookwriter.dao.DaoUtil;
import me.margotfrison.cookwriter.dao.DurationConverter;
import me.margotfrison.cookwriter.dao.RecipeRepository;
import me.margotfrison.cookwriter.dto.Difficulty;
import me.margotfrison.cookwriter.dto.Price;
import me.margotfrison.cookwriter.dto.Recipe;
import me.margotfrison.cookwriter.dto.Season;

public class RecipeWriteActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private EditText cleanName;
    private EditText customName;
    private EditText description;
    private EditText source;
    private EditText totalDuration;
    private Spinner season;
    private Spinner difficulty;
    private Spinner price;
    private TextView error;
    private Button save;
    private RecipeRepository recipeRepository;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_write);
        setSupportActionBar(findViewById(R.id.toolbar));

        String recipePreviewAuthor = getIntent().getStringExtra(Recipe.Fields.author);
        String recipePreviewCustomName = getIntent().getStringExtra(Recipe.Fields.customName);

        cleanName = findViewById(R.id.clean_name);
        customName = findViewById(R.id.custom_name);
        description = findViewById(R.id.description);
        source = findViewById(R.id.source);
        totalDuration = findViewById(R.id.total_duration);
        season = findViewById(R.id.season);
        difficulty = findViewById(R.id.difficulty);
        price = findViewById(R.id.price);
        error = findViewById(R.id.error_text);
        save = findViewById(R.id.save);
        recipeRepository = App.getDatabase().recipeRepository();

        cleanName.addTextChangedListener(this);
        customName.addTextChangedListener(this);
        totalDuration.addTextChangedListener(this);

        description.setOnTouchListener(new EditTextScrollable(R.id.description));
        season.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Season.values()));
        difficulty.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Difficulty.values()));
        price.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Price.values()));
        save.setOnClickListener(this);

        if (recipePreviewAuthor != null && recipePreviewCustomName != null) {
            DaoUtil.subscribeUIThread(
                    recipeRepository.get(recipePreviewAuthor, recipePreviewCustomName),
                    (recipe) -> {
                        cleanName.setText(recipe.getCleanName());
                        customName.setText(recipe.getCustomName());
                        description.setText(recipe.getDescription());
                        source.setText(recipe.getSource());
                        totalDuration.setText(DurationConverter.getInstance().toString(recipe.getTotalDuration()));
                        season.setSelection(recipe.getSeason().spinnerIndex);
                        difficulty.setSelection(recipe.getDifficulty().spinnerIndex);
                        price.setSelection(recipe.getPrice().spinnerIndex);
                    },
                    this);
        }

        // Force missing field at start
        afterTextChanged(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DaoUtil.disposeAll(this);
    }

    @Override
    public void onClick(View v) {
        if (v == save) {
            if (cleanName.getError() != null
                    || customName.getError() != null
                    || totalDuration.getError() != null) {
                error.setText(R.string.recipe_error_missing_field);
                error.setVisibility(View.VISIBLE);
            } else {
                Recipe recipe = Recipe.builder()
                        .author("TBD")
                        .cleanName(cleanName.getText().toString())
                        .customName(customName.getText().toString())
                        .description(description.getText().toString())
                        .source(source.getText().toString())
                        .totalDuration(DurationConverter.getInstance().fromString(totalDuration.getText().toString()))
                        .season((Season) season.getSelectedItem())
                        .difficulty((Difficulty) difficulty.getSelectedItem())
                        .price((Price) price.getSelectedItem())
                        .build();
                DaoUtil.subscribe(recipeRepository.insert(recipe), this::finish, this);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        error.setVisibility(View.GONE);
        if (cleanName.getText().toString().isEmpty()) {
            cleanName.setError(getString(R.string.recipe_clean_name_error_empty));
        }
        if (customName.getText().toString().isEmpty()) {
            customName.setError(getString(R.string.recipe_custom_name_error_empty));
        }
        if (totalDuration.getText().toString().isEmpty()) {
            totalDuration.setError(getString(R.string.recipe_total_duration_error_empty));
        } else if (DurationConverter.getInstance().checkString(totalDuration.getText().toString())) {
            totalDuration.setError(getString(R.string.recipe_total_duration_error_format));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
}
