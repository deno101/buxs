package com.dnz.local.buxs.utils;

import android.graphics.Bitmap;
import android.util.Log;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductDataStore {
    private static final String TAG = "ProductDataStore";

    private Map<Integer, String> productName = new HashMap<>();
    private Map<Integer, Integer> productPrice = new HashMap<>();
    private Map<Integer, Bitmap> productImage = new HashMap<>();
    private Map<Integer, Integer> productID = new HashMap<>();

    public synchronized void insertData(String productName, int productPrice, Bitmap productImage, int position) {
        this.insertData(-1, productName, productPrice, productImage, position);
    }

    public synchronized void insertData(int id, String productName, int productPrice, Bitmap productImage, int position) {
        // On insert bitmap is always null
        this.productImage.put(position, productImage);
        this.productName.put(position, productName);
        this.productPrice.put(position, productPrice);

        this.productID.put(position, id);
    }

    public synchronized void setBitmap(int position, Bitmap bitmap) {
        this.productImage.put(position, bitmap);
    }

    public String getProductName(int position) {
        return productName.get(position);
    }

    public String getProductPrice(int position) {
        return String.valueOf(productPrice.get(position));
    }

    public  int getProductPriceInt(int postion){
        return productPrice.get(postion);
    }

    public Bitmap getProductImage(int position) {
        return productImage.get(position);
    }

    public int length() {
        return productName.size();
    }

    public Integer getProductID(int position){
        return this.productID.get(position);
    }

    public synchronized void removeItem(int position){
        productID.remove(position);
        productName.remove(position);
        productImage.remove(position);
        productPrice.remove(position);
    }
}
