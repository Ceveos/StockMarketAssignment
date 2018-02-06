package com.fafaffy.stockmarket;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by alex on 2/1/18.
 * This content provider is necessary to provide a recent search history list
 */

public class SearchContentProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.fafaffy.SearchContentProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SearchContentProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
