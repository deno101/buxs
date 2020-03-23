package com.dnz.local.buxs.marketplace;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dnz.local.buxs.R;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ViewPagerAdapter extends PagerAdapter {
    private static final String TAG = "ViewPagerAdapter";

    private MarketPlaceDescActivity context;
    private LayoutInflater inflater;

    public ViewPagerAdapter(MarketPlaceDescActivity context) {
        this.context = context;
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
        try {
            imageView.setImageBitmap(context.bitmaps.get(position));
            Log.d(TAG, "instantiateItem: instantiate item not null"+position);
        } catch (IndexOutOfBoundsException e) {
            imageView.setImageBitmap(null);
            Log.d(TAG, "instantiateItem: instantiate item null"+position);
        }

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

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
