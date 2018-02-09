package com.example.android.shopbot;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SearchResultActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Product>> {
    public static final String LOG_TAG = SearchResultActivity.class.getName();


    private static String REQUEST_URL = "";

    private ProductAdapter mAdapter;

    // Constant value for the Shopbot loader ID. We can choose any integer.This really only comes into play if you're using multiple loaders.
    private static final int SHOPBOT_LOADER_ID = 1;

    //TextView for Empty State(No results match)
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Bundle bundle = getIntent().getExtras();
        String productName = bundle.getString("productName");
        int startingPrice= bundle.getInt("startingPrice");
        int endingPrice=bundle.getInt("endingPrice");

        String productNameForUrl="";

        for(int i=0;i<productName.length();i++)
        {
            if(productName.charAt(i)== ' ')
                productNameForUrl+="%20";
            productNameForUrl+=productName.charAt(i);
        }

        REQUEST_URL =
                "https://price-api.datayuge.com/api/v1/compare/search?api_key=Uj3KahNgL3owF7EtbGMy57926uJttmHFBU0&" +
                        "product=" + productNameForUrl;

        if(startingPrice!=-1)
            REQUEST_URL+= "&price_start=" + startingPrice;

        if(endingPrice!=-1)
            REQUEST_URL+=  "&price_end=" + endingPrice;

        REQUEST_URL += "&page=1";

        mAdapter = new ProductAdapter(this, new ArrayList<Product>());

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for the bundle.
        // Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(SHOPBOT_LOADER_ID, null, this);

        // Get a reference to the ListView, and attach the adapter to the listView.
        GridView gridView = (GridView) findViewById(R.id.list);
        gridView.setAdapter(mAdapter);

        // Start the AsyncTask to fetch the data
        ShopbotAsyncTask task = new ShopbotAsyncTask();
        task.execute(REQUEST_URL);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        gridView.setEmptyView(mEmptyStateTextView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Product currentItem = mAdapter.getItem(position);

                //Get currentItem's url
                String productUrl= currentItem.getUrl();

                // Create a new intent to open the {@link ProductActivity}
                Intent productIntent = new Intent(SearchResultActivity.this, ProductActivity.class);
                productIntent.putExtra("productUrl", productUrl);

                // Send the intent to launch a new activity
                startActivity(productIntent);
            }
        });
    }

    private class ShopbotAsyncTask extends AsyncTask<String, Void, List<Product>> {

        @Override
        protected List<Product> doInBackground(String... urls) {
            //Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Product> result = QueryUtils.fetchProductData(urls[0]);
            return result;
        }


        @Override
        protected void onPostExecute(List<Product> data) {
            mAdapter.clear();

            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
    }

    @Override
    public Loader<List<Product>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new ShopbotLoader(this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Product>> loader, List<Product> products) {
        mAdapter.clear();

        if (products != null && !products.isEmpty()) {
            mAdapter.addAll(products);
        }

        // Set empty state text to display "No Search Results Found"
        mEmptyStateTextView.setText("No Search Results Found");
    }

    @Override
    public void onLoaderReset(Loader<List<Product>> loader) {
        //  Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}