package com.example.android.shopbot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private ArrayList<Product> productsList;
    private Context mContext;


    public ProductAdapter(ArrayList<Product> productsList, Context context) {
        this.productsList = productsList;
        this.mContext=context;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView imageView;
        private TextView nameTextView;
        private TextView priceTextView;

        public ProductViewHolder (View view)
        {
            super(view);
            view.setOnClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.product_image);
            nameTextView = (TextView) view.findViewById(R.id.product_name);
            priceTextView = (TextView) view.findViewById(R.id.product_price);
        }

        @Override
        public void onClick(View v) {
            Product currentItem = productsList.get(getAdapterPosition());
            //Get currentItem's url
            String productUrl= currentItem.getUrl();
            // Create a new intent to open the {@link ProductActivity}
            Intent productIntent = new Intent(mContext, ProductActivity.class);
            productIntent.putExtra("productUrl", productUrl);

            // Send the intent to launch a new activity
            mContext.startActivity(productIntent);
        }
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_grid_item, null);

        return new ProductViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productsList.get(position);

        String productImage = product.getImage();
        Picasso.with(mContext).load(productImage).into(holder.imageView);
        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText(product.getPrice());
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

}