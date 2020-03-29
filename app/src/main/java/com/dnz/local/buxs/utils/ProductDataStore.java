package com.dnz.local.buxs.utils;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ProductDataStore {
    private ArrayList<String> productName = new ArrayList<>();
    private ArrayList<Integer> productPrice = new ArrayList<>();
    private ArrayList<Bitmap> productImage = new ArrayList<>();

    protected void insertData(String productName, int productPrice, Bitmap productImage){
        // On insert bitmap is always null
        this.productImage.add(productImage);
        this.productName.add(productName);
        this.productPrice.add(productPrice);
    }

    public synchronized void setBitmap(int position, Bitmap bitmap){
        this.productImage.add(position, bitmap);
    }

    protected String getProductName(int position){
        return productName.get(position);
    }

    protected int getProductPrice(int position){
        return productPrice.get(position);
    }

    protected Bitmap getProductImage(int position){
        return productImage.get(position);
    }
}
