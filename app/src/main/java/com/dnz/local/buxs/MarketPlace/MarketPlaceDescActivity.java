package com.dnz.local.buxs.MarketPlace;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.dnz.local.buxs.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MarketPlaceDescActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ArrayList<Integer> integers =  new ArrayList<>();
    public View[] selectorViews = new View[3];
    Map<String, String> stringMap = new HashMap<>();

    public TextView productDesc, productPrice, productName, productBrand;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place_desc);

        selectorViews[0] = findViewById(R.id.item_1);
        selectorViews[1] = findViewById(R.id.item_2);
        selectorViews[2] = findViewById(R.id.item_3);

        // Change color of status bar
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        viewPager = findViewById(R.id.view_pager);

        initImages();
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(this, integers);
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // Reset all dots to unselected
                for (View x: selectorViews){
                    x.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.image_slider_bg));
                }

                //Set page selected  to onselect bg
                selectorViews[position].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.image_slider_bg_onselect));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        selectorViews[0].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.image_slider_bg_onselect));

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimer(), 2000, 4000);

        initData();

        //init vars
        productPrice = findViewById(R.id.product_price);
        productBrand = findViewById(R.id.product_brand);
        productDesc = findViewById(R.id.product_description);
        productName = findViewById(R.id.product_name);

        productName.setText(stringMap.get("product_name"));
        productDesc.setText(stringMap.get("product_desc"));
        productPrice.setText(stringMap.get("product_price"));
        productBrand.setText(stringMap.get("product_brand"));
    }

    private void initImages(){
        for(int i = 0; i < 3; i++){
            integers.add(R.drawable.ic_keyboard_black_50dp);
        }
    }

    // Timer to handle auto scroll if images
    public class MyTimer extends TimerTask {

        @Override
        public void run() {
            MarketPlaceDescActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() == 0){
                        viewPager.setCurrentItem(1);
                    }else if(viewPager.getCurrentItem() == 1){
                        viewPager.setCurrentItem(2);
                    }else{
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    private void initData(){
        stringMap.put("product_brand", "Nike");
        stringMap.put("product_name", "NikeAirs");

        stringMap.put("product_price", "KSH 250.00");
        stringMap.put("product_desc", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
    }
}
