package me.margotfrison.cookwriter.android.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import me.margotfrison.cookwriter.R;
import me.margotfrison.cookwriter.android.App;
import me.margotfrison.cookwriter.android.components.EditTextScrollable;
import me.margotfrison.cookwriter.android.components.StepWriteComponent;
import me.margotfrison.cookwriter.dao.DaoUtil;
import me.margotfrison.cookwriter.dao.DurationConverter;
import me.margotfrison.cookwriter.dao.RecipeRepository;
import me.margotfrison.cookwriter.dto.Difficulty;
import me.margotfrison.cookwriter.dto.Price;
import me.margotfrison.cookwriter.dto.Recipe;
import me.margotfrison.cookwriter.dto.Season;
import me.margotfrison.cookwriter.dto.Step;


/**
 * {@link AppCompatActivity} to create, edit or delete a recipe into the database
 */
public class RecipeWriteActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    public static final String NEW_RECIPE_EXTRA_KEY = "newRecipe";

    private Recipe originalRecipe;
    private ImageButton deleteRecipe;
    private EditText cleanName;
    private EditText customName;
    private EditText description;
    private EditText source;
    private EditText totalDuration;
    private Spinner season;
    private Spinner difficulty;
    private Spinner price;
    private LinearLayout steps;
    private Button addStep;
    private TextView error;
    private Button save;

    private RecipeRepository recipeRepository;
    private final List<StepWriteComponent> stepList = new ArrayList<>();
    private boolean disableTextWatch = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_write);
        setSupportActionBar(findViewById(R.id.toolbar));

        String recipePreviewAuthor = getIntent().getStringExtra(Recipe.Fields.author);
        String recipePreviewCustomName = getIntent().getStringExtra(Recipe.Fields.customName);
        boolean isNewRecipe = getIntent().getBooleanExtra(NEW_RECIPE_EXTRA_KEY, false);

        deleteRecipe = findViewById(R.id.delete_recipe);
        cleanName = findViewById(R.id.clean_name);
        customName = findViewById(R.id.custom_name);
        description = findViewById(R.id.description);
        source = findViewById(R.id.source);
        totalDuration = findViewById(R.id.total_duration);
        season = findViewById(R.id.season);
        difficulty = findViewById(R.id.difficulty);
        price = findViewById(R.id.price);
        steps = findViewById(R.id.steps);
        addStep = findViewById(R.id.add_step);
        error = findViewById(R.id.error_text);
        save = findViewById(R.id.save);
        recipeRepository = App.getDatabase().recipeRepository();

        deleteRecipe.setOnClickListener(this);
        cleanName.addTextChangedListener(this);
        customName.addTextChangedListener(this);
        totalDuration.addTextChangedListener(this);
        description.setOnTouchListener(new EditTextScrollable(R.id.description));
        season.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Season.values()));
        difficulty.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Difficulty.values()));
        price.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Price.values()));
        addStep.setOnClickListener(this);
        save.setOnClickListener(this);

        if (!isNewRecipe) {
            customName.setEnabled(false);
        }

        // Populate EditViews if a recipe id was given (we want to edit a recipe)
        if (recipePreviewAuthor != null && recipePreviewCustomName != null) {
            disableTextWatch = true;
            DaoUtil.subscribeUIThread(
                    recipeRepository.get(recipePreviewAuthor, recipePreviewCustomName),
                    (recipe) -> {
                        originalRecipe = recipe;
                        cleanName.setText(recipe.getCleanName());
                        customName.setText(recipe.getCustomName());
                        description.setText(recipe.getDescription());
                        source.setText(recipe.getSource());
                        totalDuration.setText(DurationConverter.getInstance().toString(recipe.getTotalDuration()));
                        season.setSelection(recipe.getSeason().spinnerIndex);
                        difficulty.setSelection(recipe.getDifficulty().spinnerIndex);
                        price.setSelection(recipe.getPrice().spinnerIndex);
                        Optional.ofNullable(recipe.getSteps()).orElse(new ArrayList<>()).forEach(this::addStep);
                        disableTextWatch = false;
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

    private void addStep() {
        addStep(null);
    }

    /**
     * Add a {@link Step} in the stepList and a {@link StepWriteComponent} to the step {@link LinearLayout}
     * @param step the {@link Step} to add to the lists
     */
    private void addStep(@Nullable Step step) {
        final int stepIndex = stepList.size();
        StepWriteComponent stepWriteComponent = new StepWriteComponent(this,
                stepIndex,
                Optional.ofNullable(step).map(Step::getStepDescriptions).orElse(null),
                this::deleteStep);
        stepList.add(stepWriteComponent);
        steps.addView(stepWriteComponent, stepIndex);
    }

    /**
     * Remove a {@link Step} from the stepList and the {@link StepWriteComponent} to the step {@link LinearLayout}
     * @param index the index of the step to remove
     */
    private void deleteStep(int index) {
        StepWriteComponent stepWriteComponent = (StepWriteComponent) steps.getChildAt(index);
        steps.removeView(stepWriteComponent);
        stepList.remove(stepWriteComponent);
        for (int i = 0; i < stepList.size(); i++) {
            stepList.get(i).setIndex(i);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == save) {
            // We check any EditText have errors
            if (cleanName.getError() != null
                    || customName.getError() != null
                    || totalDuration.getError() != null) {
                // We display a message in case of any errors
                error.setText(R.string.recipe_error_missing_field);
                error.setVisibility(View.VISIBLE);
            } else {
                // Save the recipe to the database
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
                        .steps(stepList.stream()
                                .map(StepWriteComponent::getDescription)
                                .map(EditText::getText)
                                .filter(Predicate.not(Objects::isNull))
                                .map(Objects::toString)
                                .filter(Predicate.not(String::isEmpty))
                                .map(Step::new)
                                .collect(Collectors.toList()))
                        .build();
                DaoUtil.subscribe(recipeRepository.insert(recipe), this::finish, this);
            }
        } else if (v == addStep) {
            addStep();
        } else if (v == deleteRecipe) {
            // If it is a recipe creation, originalRecipe == null. Else, it's a recipe edition
            if (originalRecipe != null) {
                // We delete the recipe because it's a recipe edition
                DaoUtil.subscribe(recipeRepository.delete(originalRecipe), this::finish, this);
            } else {
                // We simply close the activity because it's a recipe creation
                finish();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Here we run tests to see if data in EditTexts are valid
        if (disableTextWatch) {
            return;
        }
        error.setVisibility(View.GONE);
        if (cleanName.getText().toString().isEmpty()) {
            cleanName.setError(getString(R.string.recipe_clean_name_error_empty));
        }
        if (customName.getText().toString().isEmpty()) {
            customName.setError(getString(R.string.recipe_custom_name_error_empty));
        }
        if (totalDuration.getText().toString().isEmpty()) {
            totalDuration.setError(getString(R.string.recipe_total_duration_error_empty));
        } else if (!DurationConverter.getInstance().checkString(totalDuration.getText().toString())) {
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
