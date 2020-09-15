package com.dnz.local.buxs.marketplace.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dnz.local.buxs.R;
import com.dnz.local.buxs.marketplace.AddProductActivity;

public class DescriptionFragment extends Fragment implements View.OnClickListener{

    private AddProductActivity parentActivity;
    private EditText productBrand, productDescription;


    private DescriptionFragment(){}

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
        return inflater.inflate(R.layout.fragment_description, container, false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next_btn_for_add_product:
                next();
                break;
            case R.id.preview_btn:
                preview();
                break;
            case R.id.back_toolbar_no_drawer:
                getFragmentManager().popBackStack();
                break;
        }
    }

    private void next(){
        if (!hasErrors()){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container, parentActivity.previewFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void preview(){
        next();
    }

    private boolean hasErrors(){
        boolean error = false;
        if (productBrand.getText().toString().trim().equalsIgnoreCase("")){
            error = true;
            productBrand.setError("Required");
        }

        if (productDescription.getText().toString().trim().equalsIgnoreCase("")){
            error = true;
            productBrand.setError("Required");
        }

        return error;
    }

    private void populateData(){
        parentActivity.productBrand = productBrand.getText().toString().trim();
        parentActivity.productDescription = productDescription.getText().toString().trim();
    }

}
