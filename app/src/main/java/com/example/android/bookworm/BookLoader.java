package com.example.android.bookworm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Books>> {
    private static final String LOG_TAG = BookLoader.class.getSimpleName();

    private String mUrl;

    public BookLoader(@NonNull Context context, String Url) {
        super(context);
        mUrl = Url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "TEST: onStartLoading() called");
        forceLoad();
    }

    @Nullable
    @Override
    public List<Books> loadInBackground() {
        Log.i(LOG_TAG, "TEST: loadInBackground() called");
        if (mUrl == null) {
            return null;
        }
        List<Books> books = QueryUtils.fetchBookData(mUrl);
        return books;
    }
}
