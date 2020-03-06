package com.dnz.local.buxs.MarketPlace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.dnz.local.buxs.R;

import java.util.ArrayList;

public class MarketPlaceDescActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ArrayList<Integer> integers =  new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place_desc);

        viewPager = findViewById(R.id.view_pager);

        initImages();
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(this, integers);
        viewPager.setAdapter(pagerAdapter);
    }

    private void initImages(){
        for(int i = 0; i < 3; i++){
            integers.add(R.drawable.ic_keyboard_black_50dp);
        }
    }
}
