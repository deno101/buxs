package com.dnz.local.buxs.marketplace.fragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dnz.local.buxs.R;
import com.dnz.local.buxs.marketplace.AddProductActivity;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ViewPagerAdapterPreviewFragment extends PagerAdapter {
    private static final String TAG = "ViewPagerAdapter";

    private PreviewFragment fragment;
    private LayoutInflater inflater;

    public ViewPagerAdapterPreviewFragment(PreviewFragment fragment) {
        this.fragment = fragment;
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
        inflater = (LayoutInflater) fragment.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adapter_layout, null);

        ImageView imageView = view.findViewById(R.id.image_view_adapter);
        try {
            AddProductActivity addProductActivity= ((AddProductActivity) fragment.getActivity());
            imageView.setImageBitmap(addProductActivity.productImages[position]);
        } catch (IndexOutOfBoundsException e) {
            imageView.setImageBitmap(null);
        }catch (Exception e){
            imageView.setImageBitmap(null);
            Log.e(TAG, "instantiateItem: error", e);
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
