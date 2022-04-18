package com.example.android.bookworm;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Books>> {

    public static final String LOG_TAG = BookActivity.class.getName();

    private BookAdapter mAdapter;

    private static final int BOOK_LOADER_ID = 1;

    private TextView mEmptyStateTextView;

    private ProgressBar mLoadingIndicator;

    private static final String GOOGLE_BOOKS_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    static String mBOOKSURL = "";
    private ListView bookListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST: onCreate() method called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);

        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameEditText = findViewById(R.id.book_name);
                mEmptyStateTextView = findViewById(R.id.empty_view);
                mEmptyStateTextView.setVisibility(View.INVISIBLE);
                ConnectivityManager cm =
                        (ConnectivityManager)BookActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected){
                    mLoadingIndicator = findViewById(R.id.loading_indicator);
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    mBOOKSURL = "";
                    String bookName = nameEditText.getText().toString();
                    if (bookName.isEmpty()){
                        mLoadingIndicator.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Please enter a valid book name", Toast.LENGTH_SHORT).show();
                    }else {
                        mBOOKSURL = GOOGLE_BOOKS_URL + bookName + "&maxResults=40";

                        LoaderManager loaderManager = getLoaderManager();
                        Log.i(LOG_TAG, "TEST: initLoader() called");
                        loaderManager.restartLoader(BOOK_LOADER_ID, null, BookActivity.this);

                        bookListView = findViewById(R.id.book_list);
                        mAdapter = new BookAdapter(BookActivity.this, new ArrayList<Books>());
                        bookListView.setAdapter(mAdapter);

                        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Books books = mAdapter.getItem(position);
                                Uri uri = Uri.parse(books.getURL());

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(browserIntent);
                            }
                        });
                    }

                }else {
                    bookListView = findViewById(R.id.book_list);
                    bookListView.setAdapter(null);
                    mEmptyStateTextView.setText(R.string.no_internet);
                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @NonNull
    @Override
    public Loader<List<Books>> onCreateLoader(int i, @Nullable Bundle bundle) {
        Log.i(LOG_TAG, "TEST: onCreateLoader() called");
        return new BookLoader(this, mBOOKSURL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Books>> loader, List<Books> books) {
        Log.i(LOG_TAG, "TEST: onLoadFinished() called");
        mLoadingIndicator.setVisibility(View.GONE);
        mAdapter.clear();
        if (books != null & !books.isEmpty()) {
            mAdapter.addAll(books);
        } else {
            mEmptyStateTextView.setText(R.string.no_books);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Books>> loader) {
        Log.i(LOG_TAG, "TEST: onLoaderReset() called");
        mAdapter.clear();
    }
}
