package com.dnz.local.buxs.MarketPlace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dnz.local.buxs.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ViewPagerAdapter extends PagerAdapter {

    private MarketPlaceDescActivity context;
    private ArrayList<Integer> bitmaps;
    private LayoutInflater inflater;

    public ViewPagerAdapter(MarketPlaceDescActivity context, ArrayList<Integer> bitmaps) {
        this.context = context;
        this.bitmaps = bitmaps;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adapter_layout, null);

        ImageView imageView = view.findViewById(R.id.image_view_adapter);
        imageView.setImageResource(bitmaps.get(position));

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;

        viewPager.removeView(view);
    }
}
