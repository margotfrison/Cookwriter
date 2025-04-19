package me.margotfrison.cookwriter.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import me.margotfrison.cookwriter.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button showRecipes;
    private Button writeRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        showRecipes = findViewById(R.id.button_show_recipes);
        showRecipes.setOnClickListener(this);

        writeRecipe = findViewById(R.id.button_write_recipe);
        writeRecipe.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if ( v == showRecipes ) {
            startActivity(new Intent(this, RecipeListActivity.class));
        } else if ( v == writeRecipe ) {
            startActivity(new Intent(this, RecipeWriteActivity.class));
        }
    }
}
