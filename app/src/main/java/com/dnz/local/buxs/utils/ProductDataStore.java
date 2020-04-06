package com.dnz.local.buxs.utils;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ProductDataStore {
    private ArrayList<String> productName = new ArrayList<>();
    private ArrayList<Integer> productPrice = new ArrayList<>();
    private ArrayList<Bitmap> productImage = new ArrayList<>();

    public synchronized void insertData(String productName, int productPrice, Bitmap productImage, int position){
        // On insert bitmap is always null
        this.productImage.add(position, productImage);
        this.productName.add(position, productName);
        this.productPrice.add(position, productPrice);
    }

    public synchronized void setBitmap(int position, Bitmap bitmap){
        this.productImage.add(position, bitmap);
    }

    public String getProductName(int position){
        return productName.get(position);
    }

    public String getProductPrice(int position){
        return String.valueOf(productPrice.get(position));
    }

    public Bitmap getProductImage(int position){
        return productImage.get(position);
    }

    public int length(){
        return productName.size();
    }
}
