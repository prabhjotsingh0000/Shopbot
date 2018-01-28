package com.example.android.shopbot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Prabhjot on 27-01-2018.
 */

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button button=  (Button) findViewById(R.id.show_results);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText productNameField= (EditText) findViewById(R.id.product_name_field);
                String productName= productNameField.getText().toString();

                EditText startingPriceField= (EditText) findViewById(R.id.starting_price);
                int startingPrice= Integer.parseInt(startingPriceField.getText().toString());

                EditText endingPriceField= (EditText) findViewById(R.id.ending_price);
                int endingPrice= Integer.parseInt(endingPriceField.getText().toString());

                Intent searchResultIntent = new Intent(SearchActivity.this, SearchResultActivity.class);
                searchResultIntent.putExtra("productName", productName);
                searchResultIntent.putExtra("startingPrice", startingPrice);
                searchResultIntent.putExtra("endingPrice", endingPrice);

                // Send the intent to launch a new activity
                startActivity(searchResultIntent);

            }
        });

    }


}
