package com.dnz.local.buxs.utils;

import java.text.DecimalFormat;

public class Currency {
    private final static DecimalFormat KSHFORMAT = new DecimalFormat("KSH #,###.00");
    public static String getShilling(int price){
        return KSHFORMAT.format(price);
    }

    public static String getShilling(String price){
        float floatPrice = Float.parseFloat(price);
        return KSHFORMAT.format(floatPrice);
    }
}
