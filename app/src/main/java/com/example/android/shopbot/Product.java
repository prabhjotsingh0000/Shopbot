package com.example.android.shopbot;

/**
 * Created by Komal on 1/2/2018.
 */

public class Product {

    private String name;
    private String price;
    private String url;
    private String imageResourceId;

    public Product(String n, String p, String u, String i){
        name = n;
        price = p;
        url = u;
        imageResourceId = i;
    }

    public String getName(){
        return name;
    }

    public String getPrice(){
        return price;
    }

    public String getUrl(){
        return url;
    }

    public String getImage(){
        return imageResourceId;
    }
}
