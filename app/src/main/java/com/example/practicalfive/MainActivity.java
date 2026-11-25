package com.example.practicalfive;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The main screen. The user types the name of one of China's four municipalities
 * and presses the Go key on the keyboard to open the information screen.
 */
public class MainActivity extends AppCompatActivity {

    /** Key used to pass the normalised municipality name between the two Activities. */
    public static final String EXTRA_MUNICIPALITY = "com.example.practicalfive.extra.MUNICIPALITY";

    private EditText municipalityEditText;
    private TextView errorTextView;
    private final TextProcessor textProcessor = new TextProcessor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        municipalityEditText = findViewById(R.id.municipalityEditText);
        errorTextView = findViewById(R.id.errorTextView);

        // Register the editor action so that pressing "Go" on the keyboard is handled
        // in the same way as clicking the confirmation button on the soft keyboard.
        municipalityEditText.setOnEditorActionListener((textView, actionId, event) -> {
            boolean isGoAction = actionId == EditorInfo.IME_ACTION_GO
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || actionId == EditorInfo.IME_ACTION_NEXT
                    || actionId == EditorInfo.IME_ACTION_SEARCH;
            boolean isEnterKey = event != null
                    && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                    && event.getAction() == KeyEvent.ACTION_DOWN;

            if (isGoAction || isEnterKey) {
                handleMunicipalityInput();
                return true;
            }
            return false;
        });
    }

    /**
     * Validates the text currently held by the EditText. If it names a supported
     * municipality the normalised name is passed to {@link Municipality} using an
     * explicit Intent; otherwise an error is displayed and the user stays here.
     */
    private void handleMunicipalityInput() {
        String rawInput = municipalityEditText.getText() != null
                ? municipalityEditText.getText().toString()
                : "";

        String municipality = textProcessor.processMunicipality(rawInput);

        if (municipality == null) {
            // Empty, whitespace only, or an unsupported name such as "Guangzhou".
            showError(rawInput.trim().isEmpty()
                    ? R.string.error_empty_municipality
                    : R.string.error_invalid_municipality);
            return;
        }

        errorTextView.setVisibility(TextView.GONE);

        // Explicit Intent targeting the Municipality Activity of this application.
        Intent intent = new Intent(this, Municipality.class);
        intent.putExtra(EXTRA_MUNICIPALITY, municipality);
        startActivity(intent);
    }

    /** Shows the given error message below the input field. */
    private void showError(int messageResId) {
        errorTextView.setText(messageResId);
        errorTextView.setVisibility(TextView.VISIBLE);
        municipalityEditText.requestFocus();
    }
}
