package com.dnz.local.buxs.marketplace.fragments;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dnz.local.buxs.R;
import com.dnz.local.buxs.marketplace.AddProductActivity;
import com.dnz.local.buxs.utils.Currency;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PreviewFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "PreviewFragment";

    private PreviewFragment(){}

    public AddProductActivity parentActivity;
    private Timer timer;

    public PreviewFragment(AddProductActivity parentActivity) {
        this.parentActivity = parentActivity;
    }

    private View[] selectorViews = new View[3];
    private View layoutView;
    private ViewPager viewPager;
    private ViewPagerAdapterPreviewFragment pagerAdapter;

    private TextView productDesc, productPrice, productName, productBrand;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_preview, container, false);

        /* init all the view elements */
        selectorViews[0] = layoutView.findViewById(R.id.item_1);
        selectorViews[1] = layoutView.findViewById(R.id.item_2);
        selectorViews[2] = layoutView.findViewById(R.id.item_3);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            initViewPager();
        }

        productPrice = layoutView.findViewById(R.id.product_price);
        productBrand = layoutView.findViewById(R.id.product_brand);
        productDesc = layoutView.findViewById(R.id.product_description);
        productName = layoutView.findViewById(R.id.product_name);

        ((TextView) layoutView.findViewById(R.id.title_toolbar_no_drawer)).setText("Preview");

        productPrice.setText(Currency.getShilling(parentActivity.productPrice));
        productDesc.setText(parentActivity.productDescription);
        productBrand.setText(parentActivity.productBrand);
        productName.setText(parentActivity.productName);

        /*Set onclick listeners*/
        layoutView.findViewById(R.id.submit_for_preview_fragment).setOnClickListener(this);
        layoutView.findViewById(R.id.back_toolbar_no_drawer).setOnClickListener(this);

        return layoutView;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initViewPager() {
        viewPager = layoutView.findViewById(R.id.view_pager);

        pagerAdapter = new ViewPagerAdapterPreviewFragment(this);
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // Reset all dots to unselected
                for (View x : selectorViews) {
                    x.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.image_slider_bg));
                }

                //Set page selected  to on_select bg
                selectorViews[position].setBackground(ContextCompat.getDrawable(getContext(), R.drawable.image_slider_bg_onselect));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

        selectorViews[0].setBackground(ContextCompat.getDrawable(getContext(), R.drawable.image_slider_bg_onselect));

        timer = new Timer();
        timer.scheduleAtFixedRate(new PreviewFragment.MyTimer(), 5000, 8000);
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_for_preview_fragment:
                parentActivity.sendPost();
                break;
            case R.id.back_toolbar_no_drawer:
                getFragmentManager().popBackStack();
                break;
        }
    }


    // Timer to handle auto scroll if images
    public class MyTimer extends TimerTask {

        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else if (viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(2);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }
}
