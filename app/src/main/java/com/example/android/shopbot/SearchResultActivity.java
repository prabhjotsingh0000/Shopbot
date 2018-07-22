package com.example.android.shopbot;

import android.app.ProgressDialog;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


public class SearchResultActivity extends AppCompatActivity  {

    private ArrayList<Product> productsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemDecoration VerticalLines;
    private RecyclerView.ItemDecoration HorizontalLines;
    private ProductAdapter mAdapter;
    private static String REQUEST_URL = "";

    //TextView for Empty State(No results match)
    private TextView mEmptyStateTextView;
    private ImageView mEmptyStateImageView;

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
                "https://price-api.datayuge.com/api/v1/compare/search?api_key=zFvD9oKqrwoDcbPCd1PggAwB8guLcWq8cui&" +
                        "product=" + productNameForUrl;

        if(startingPrice!=-1)
            REQUEST_URL+= "&price_start=" + startingPrice;

        if(endingPrice!=-1)
            REQUEST_URL+=  "&price_end=" + endingPrice;

        REQUEST_URL += "&page=1";

        mEmptyStateTextView= (TextView) findViewById(R.id.empty_view);
        mEmptyStateImageView= (ImageView)findViewById(R.id.sad_face);

        recyclerView= (RecyclerView) findViewById(R.id.recycler_view);
        VerticalLines = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.HORIZONTAL);

        HorizontalLines = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);

        recyclerView.addItemDecoration(VerticalLines);
        recyclerView.addItemDecoration(HorizontalLines);

        // Start the AsyncTask to fetch the data
        ShopbotAsyncTask task = new ShopbotAsyncTask();
        task.execute(REQUEST_URL);

    }

    private class ShopbotAsyncTask extends AsyncTask<String, Void, ArrayList<Product>> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()
        {

            progressDialog = new ProgressDialog(SearchResultActivity.this);
            progressDialog.setMessage("loading...");
            progressDialog.show();

        }

        @Override
        protected ArrayList<Product> doInBackground(String... urls) {
            //Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            ArrayList<Product> result = QueryUtils.fetchProductData(urls[0]);
            return result;
        }


        @Override
        protected void onPostExecute(ArrayList<Product> data) {

            if(data.size()==0)
            {
                mEmptyStateTextView.setVisibility(View.VISIBLE);
                mEmptyStateImageView.setVisibility(View.VISIBLE);
            }

            productsList=data;

            if(progressDialog!= null && progressDialog.isShowing() )
                progressDialog.dismiss();

            mLayoutManager = new GridLayoutManager(SearchResultActivity.this,3);
            recyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new ProductAdapter(productsList,SearchResultActivity.this);
            recyclerView.setAdapter(mAdapter);

        }
    }
}