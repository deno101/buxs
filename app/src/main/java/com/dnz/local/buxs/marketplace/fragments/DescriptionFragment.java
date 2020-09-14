package com.dnz.local.buxs.marketplace.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dnz.local.buxs.R;
import com.dnz.local.buxs.marketplace.AddProductActivity;

public class DescriptionFragment extends Fragment {

    private DescriptionFragment(){}

    public AddProductActivity parentActivity;

    public DescriptionFragment(AddProductActivity parentActivity) {
        this.parentActivity = parentActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_description, container, false);
    }
}
