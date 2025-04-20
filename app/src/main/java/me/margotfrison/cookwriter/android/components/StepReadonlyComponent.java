package me.margotfrison.cookwriter.android.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import lombok.Getter;
import me.margotfrison.cookwriter.R;

@SuppressLint("ViewConstructor")
@Getter
public class StepReadonlyComponent extends LinearLayout {
    public StepReadonlyComponent(Context context, int index, String stepDescription) {
        super(context);
        inflate(context, R.layout.component_step_readonly, this);

        TextView number = findViewById(R.id.number);
        TextView description = findViewById(R.id.description);

        number.setText(getResources().getString(R.string.recipe_step_number, index + 1));
        description.setText(stepDescription);
    }
}
