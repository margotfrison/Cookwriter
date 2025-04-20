package me.margotfrison.cookwriter.android.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import lombok.Getter;
import me.margotfrison.cookwriter.R;

@SuppressLint("ViewConstructor")
@Getter
public class StepWriteComponent extends LinearLayout implements View.OnClickListener {
    private int index;
    private final TextView number;
    private final EditText description;
    private final ImageButton deleteStep;
    private final StepWriteListener listener;

    public interface StepWriteListener {
        void onDelete(int index);
    }

    public StepWriteComponent(Context context, int index, String stepDescription, StepWriteListener listener) {
        super(context);
        inflate(context, R.layout.component_step_write, this);

        this.listener = listener;

        number = findViewById(R.id.number);
        description = findViewById(R.id.description);
        deleteStep = findViewById(R.id.delete_step);

        setIndex(index);
        description.setText(stepDescription);
        deleteStep.setOnClickListener(this);
    }

    public void setIndex(int index) {
        this.index = index;
        number.setText(getResources().getString(R.string.recipe_step_number, index + 1));
    }

    @Override
    public void onClick(View v) {
        listener.onDelete(index);
    }
}
