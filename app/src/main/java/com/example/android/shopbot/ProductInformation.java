package com.example.android.shopbot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProductInformation {
    private String product_name;
    private ArrayList<String> images;

    public ProductInformation()
    {}

    public ProductInformation(String pn, ArrayList<String> i){
        product_name=pn;
        images=i;
    }

    public String getProductName(){return product_name; }
    public ArrayList<String> getImages(){return images;}
}
