package com.fafaffy.stockmarket;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.fafaffy.stockmarket.Adapters.StockDataRecyclerAdapter;
import com.fafaffy.stockmarket.Models.StockData;
import com.fafaffy.stockmarket.Tasks.DownloadStockTask;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Brian and Alex
 * The main activity class which sets the main logic of the prorgam.
 * This will set the recyclerview's adapter so the UI knows how to render our stock table
 * Additionally, this class checks the intent to see if the user has inputted a search query
 * to search for a desired stock company. If a search has occured, this class will dispatch
 * an ASync task to download the desired stock company data, or display an error if it does
 * not exist.
 *
 * There is a floating action bar button which is shown that allows the user to perform
 * a search query for a new stock company
 */
public class StocksDisplayActivity extends AppCompatActivity {

    private DownloadStockTask downloadTask;
    private View progressOverlayView;
    private List<StockData> mData;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    // Define our array adapter
    StockDataRecyclerAdapter recyclerAdapter;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocksdisplay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Define our error label (shown if no results are found)
        final TextView errorLabel = (TextView) findViewById(R.id.error_label);
        final TextView timeoutLabel = (TextView) findViewById(R.id.timeout_label);
        // Define our recycleview to show the tabled data
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Create an empty array list in order to define our recycler view adapter
        mData = new ArrayList<>();
        recyclerAdapter = new StockDataRecyclerAdapter(mData);

        // Set the properties of the recyclerview (the layout, and animations)
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        // Set our listview's adapter
        recyclerView.setAdapter(recyclerAdapter);


        // Floating action button to search different stock company
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearchRequested();
            }
        });

        // Logic on when to hide or show the FAB button
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 && fab.isShown())
                    fab.hide();
                if (dy < 0 && !fab.isShown())
                    fab.show();
            }
        });

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // Query of the searched string
            String query = intent.getStringExtra(SearchManager.QUERY).toUpperCase();

            // Save the query to the recent history
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SearchContentProvider.AUTHORITY, SearchContentProvider.MODE);
            suggestions.saveRecentQuery(query, null);

            // If we have a search intent, replace our current "Stock Market"
            // title to the desired stock
            setTitle(query);
            if (downloadTask != null) {
                downloadTask.cancel(true);
            }

            // Show loading screen
            fab.setVisibility(View.GONE);
            progressOverlayView = findViewById(R.id.progress_overlay);
            progressOverlayView.setVisibility(View.VISIBLE);

            // Create our download task
            downloadTask = new DownloadStockTask() {
                @Override
                protected void onPostExecute(ArrayList<StockData> data) {
                    // Remove the loading screen
                    progressOverlayView.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                    if (!isCancelled() && data.size() > 0) {
                        mData.addAll(data);
                        recyclerAdapter.notifyDataSetChanged();
                    } else if (errorDownloading) {
                        timeoutLabel.setVisibility(View.VISIBLE);
                    } else {
                        // Set our error label to be visible
                        errorLabel.setVisibility(View.VISIBLE);
                    }
                }
            };

            // Execute the task
            downloadTask.execute(query);

        }
    }

    // Shows the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    // When a user selects an option menu (do nothing for now)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspetion SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
