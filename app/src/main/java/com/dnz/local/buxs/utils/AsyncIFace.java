package com.dnz.local.buxs.utils;

import java.util.ArrayList;

public class AsyncIFace {
    public interface IFGetCartCount{
        void onPostExecuteThread(int count, ArrayList<Integer> data);
    }


    public interface IFAddToCart{
        /*
        * To be used to make ui changes from AsyncTask AddToCart
        */
        void onPostExecuteThread(boolean alreadyInCart, ArrayList<Integer> productsInCart);
    }
}
