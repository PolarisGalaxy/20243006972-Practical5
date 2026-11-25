package com.example.practicalfive;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * The second screen. It shows the name, photograph and summary of the municipality
 * selected on the main screen, customising the colours to match, and offers a
 * "Visit site" button that opens the municipality's official website using an
 * implicit Intent.
 */
public class Municipality extends AppCompatActivity {

    /** Official websites of the four municipalities. */
    private static final String URL_BEIJING = "https://english.beijing.gov.cn/";
    private static final String URL_SHANGHAI = "https://english.shanghai.gov.cn/";
    private static final String URL_TIANJIN = "https://en.tj.gov.cn/";
    private static final String URL_CHONGQING = "https://english.cq.gov.cn/";

    private final TextProcessor textProcessor = new TextProcessor();

    private TextView nameTextView;
    private ImageView municipalityImageView;
    private TextView infoTextView;
    private Button visitSiteButton;

    /** The validated municipality currently displayed, used by the Visit site button. */
    private String currentMunicipality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipality);

        nameTextView = findViewById(R.id.nameTextView);
        municipalityImageView = findViewById(R.id.municipalityImageView);
        infoTextView = findViewById(R.id.infoTextView);
        visitSiteButton = findViewById(R.id.visitSiteButton);

        // Retrieve the name passed by MainActivity and validate it again, so that
        // missing or invalid data is handled safely.
        String passed = getIntent() != null
                ? getIntent().getStringExtra(MainActivity.EXTRA_MUNICIPALITY)
                : null;
        String municipality = textProcessor.processMunicipality(passed);

        if (municipality == null) {
            // No valid municipality was supplied: show a message instead of crashing.
            nameTextView.setText(R.string.error_no_data);
            nameTextView.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.color_text_dark));
            municipalityImageView.setImageResource(R.drawable.chinese_municipalities);
            infoTextView.setText(R.string.error_no_data);
            infoTextView.setTextColor(
                    ContextCompat.getColor(this, R.color.color_text_dark));
            visitSiteButton.setVisibility(Button.GONE);
            return;
        }

        currentMunicipality = municipality;

        // The summaries live in municipalities_array, in the order Beijing,
        // Shanghai, Chongqing, Tianjin.
        String[] summaries = getResources().getStringArray(R.array.municipalities_array);
        String summary = textProcessor.getSummary(municipality, summaries);

        int colourResId = getColourResource(municipality);
        int imageResId = getImageResource(municipality);

        // Name, shown on the municipality's own background colour.
        nameTextView.setText(municipality);
        nameTextView.setBackgroundColor(ContextCompat.getColor(this, colourResId));
        nameTextView.setTextColor(ContextCompat.getColor(this, R.color.color_text_light));

        // Photograph of the municipality.
        municipalityImageView.setImageResource(imageResId);
        municipalityImageView.setContentDescription(
                getString(R.string.municipality_image_description, municipality));

        // Summary text, in the municipality's own colour.
        if (summary != null) {
            infoTextView.setText(summary);
        }
        infoTextView.setTextColor(ContextCompat.getColor(this, colourResId));

        // Visit site button, in the municipality's own colour.
        visitSiteButton.setBackgroundColor(ContextCompat.getColor(this, colourResId));
        visitSiteButton.setTextColor(ContextCompat.getColor(this, R.color.color_text_light));

        visitSiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOfficialWebsite();
            }
        });
    }

    /**
     * Opens the official website of the municipality currently displayed.
     * Uses an implicit Intent with {@link Intent#ACTION_VIEW} so that Android
     * chooses a suitable application, normally a web browser.
     */
    private void openOfficialWebsite() {
        String url = getOfficialWebsite(currentMunicipality);
        if (url == null) {
            Toast.makeText(this, R.string.error_no_data, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // No application is available to handle the URL.
            Toast.makeText(this, R.string.error_no_browser, Toast.LENGTH_LONG).show();
        }
    }

    /** Returns the official website URL for the given municipality. */
    private String getOfficialWebsite(String municipality) {
        if (municipality == null) {
            return null;
        }
        switch (municipality) {
            case "Beijing":
                return URL_BEIJING;
            case "Shanghai":
                return URL_SHANGHAI;
            case "Tianjin":
                return URL_TIANJIN;
            case "Chongqing":
                return URL_CHONGQING;
            default:
                return null;
        }
    }

    /** Returns the named colour resource for the given municipality. */
    private int getColourResource(String municipality) {
        switch (municipality) {
            case "Beijing":
                return R.color.municipality_beijing;
            case "Shanghai":
                return R.color.municipality_shanghai;
            case "Tianjin":
                return R.color.municipality_tianjin;
            case "Chongqing":
                return R.color.municipality_chongqing;
            default:
                return R.color.color_text_dark;
        }
    }

    /** Returns the drawable resource for the given municipality. */
    private int getImageResource(String municipality) {
        switch (municipality) {
            case "Beijing":
                return R.drawable.beijing;
            case "Shanghai":
                return R.drawable.shanghai;
            case "Tianjin":
                return R.drawable.tianjin;
            case "Chongqing":
                return R.drawable.chongqing;
            default:
                return R.drawable.chinese_municipalities;
        }
    }
}
