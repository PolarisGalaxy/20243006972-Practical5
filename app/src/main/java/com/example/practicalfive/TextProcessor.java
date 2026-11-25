package com.example.practicalfive;

/**
 * A plain Java class (it does not extend Activity and has no XML layout) that
 * validates and normalises the municipality name typed by the user.
 *
 * <p>The single entry point is {@link #processMunicipality(String)}, which returns
 * the correctly capitalised name of a supported municipality, or {@code null} when
 * the input is null, blank or not one of the four supported municipalities.</p>
 */
public class TextProcessor {

    /**
     * The supported municipalities, in the exact order in which their summaries are
     * stored in {@code res/values/arrays.xml} (Beijing, Shanghai, Chongqing, Tianjin).
     * The index of a name in this array is the index of its summary in the string array.
     */
    public static final String[] MUNICIPALITIES = {
            "Beijing",
            "Shanghai",
            "Chongqing",
            "Tianjin"
    };

    /**
     * Validates and normalises the supplied municipality name.
     *
     * @param municipality the raw text entered by the user, may be {@code null}
     * @return the correctly capitalised municipality name, or {@code null} if the
     *         input is null, empty, whitespace only, or names an unsupported municipality
     */
    public String processMunicipality(String municipality) {
        if (municipality == null) {
            return null;
        }

        String trimmed = municipality.trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        // Compare without regard to case so that "beijing", "BEIJING" and
        // "Beijing" are all accepted, then return the canonical capitalisation.
        for (String supported : MUNICIPALITIES) {
            if (supported.equalsIgnoreCase(trimmed)) {
                return supported;
            }
        }

        // Not one of the four supported municipalities, e.g. "Guangzhou".
        return null;
    }

    /**
     * Returns the index of the supplied municipality inside {@link #MUNICIPALITIES},
     * which is also the index of its summary in {@code municipalities_array}.
     *
     * @param municipality a municipality name, expected to be already normalised
     * @return the matching index, or {@code -1} if the name is not recognised
     */
    public int getMunicipalityIndex(String municipality) {
        if (municipality == null) {
            return -1;
        }
        for (int i = 0; i < MUNICIPALITIES.length; i++) {
            if (MUNICIPALITIES[i].equalsIgnoreCase(municipality.trim())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the summary of the supplied municipality from the
     * {@code municipalities_array} string array.
     *
     * @param municipality  a municipality name, expected to be already normalised
     * @param summaries     the contents of the {@code municipalities_array} resource
     * @return the matching summary, or {@code null} if there is no match
     */
    public String getSummary(String municipality, String[] summaries) {
        if (summaries == null) {
            return null;
        }
        int index = getMunicipalityIndex(municipality);
        if (index < 0 || index >= summaries.length) {
            return null;
        }
        return summaries[index];
    }
}
