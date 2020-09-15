package com.dnz.local.buxs.marketplace.fragments;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dnz.local.buxs.R;
import com.dnz.local.buxs.marketplace.AddProductActivity;
import com.dnz.local.buxs.utils.Const;
import com.dnz.local.buxs.utils.Currency;
import com.dnz.local.buxs.utils.Permissions;
import com.dnz.local.buxs.utils.Random;

public class CameraCaptureFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "CameraCaptureFragment";
    public static final int IMAGE_ONE = 101;
    public static final int IMAGE_TWO = 102;
    public static final int IMAGE_THREE = 103;

    private View layoutContainer;
    private EditText productName, productPrice;
    private Bitmap productImage[] = new Bitmap[3];
    public AddProductActivity parentActivity;
    private CameraCaptureFragment instance = null;
    private ImageView imageView, image_1, image_2, image_3;

    private CameraCaptureFragment() {
    }

    public CameraCaptureFragment(AddProductActivity parentActivity) {
        this.parentActivity = parentActivity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutContainer = inflater.inflate(R.layout.fragment_camera_capture, container, false);

        /* Get views from layout*/
        productName = layoutContainer.findViewById(R.id.product_name_for_add_product);
        productPrice = layoutContainer.findViewById(R.id.price_for_add_product);
        imageView = layoutContainer.findViewById(R.id.imageview_for_add_product);
        image_2 = layoutContainer.findViewById(R.id.image_2_for_add_product);
        image_1 = layoutContainer.findViewById(R.id.image_1_for_add_product);
        image_3 = layoutContainer.findViewById(R.id.image_3_for_add_product);

        /* Add OnClickListeners for image add Btn's*/
        image_1.setOnClickListener(this);
        image_2.setOnClickListener(this);
        image_3.setOnClickListener(this);
        layoutContainer.findViewById(R.id.next_btn_for_add_product).setOnClickListener(this);
        layoutContainer.findViewById(R.id.preview_btn_for_add_product).setOnClickListener(this);

        instance = this;

        return layoutContainer;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_1_for_add_product:
                captureImage(IMAGE_ONE);
                break;
            case R.id.image_2_for_add_product:
                captureImage(IMAGE_TWO);
                break;
            case R.id.image_3_for_add_product:
                captureImage(IMAGE_THREE);
                break;
            case R.id.preview_btn_for_add_product:
                preview();
                break;
            case R.id.next_btn_for_add_product:
                next();
                break;
            case R.id.back_toolbar_no_drawer:
                parentActivity.finish();
        }
    }

    private void captureImage(int resultCode) {
        // TODO: 9/12/20 create callbacks for user accept perms and user reject perms
        if (Permissions.checkPermission(Manifest.permission.CAMERA, parentActivity)) {
            Permissions.getPermissions(parentActivity, Manifest.permission.CAMERA, Const.CAMERA_PERMISSION);
        }
        if (Permissions.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, parentActivity)) {
            Permissions.getPermissions(parentActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE, Const.WRITE_EXTERNAL_PERMISSION);
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        parentActivity.startActivityForResult(takePictureIntent, resultCode);
    }

    private void next() {
        boolean hasError = checkForm();
        if (!hasError) {
            populateData();
            FragmentTransaction ft = parentActivity.getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container, parentActivity.descriptionFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void preview() {
        populateData();
        FragmentTransaction ft = parentActivity.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, parentActivity.previewFragment)
                .addToBackStack(null)
                .commit();
    }

    public void setImage(Bitmap bitmap) {

        if (instance == null) {
            Log.e(TAG, "setImage: instance == null", null);
            return;
        }

        imageView.setImageBitmap(bitmap);
    }

    public void setImageToCheck(int i) {
        ImageView x = null;
        if (i == 1) {
            x = image_1;
        } else if (i == 2) {
            x = image_2;
        } else if (i == 3) {
            x = image_3;
        } else {
            Log.e(TAG, "setImageToCheck: error invalid value", null);
            return;
        }
        x.setImageResource(R.drawable.check_orange);
    }

    private boolean checkForm() {
        boolean error = false;
        if (productName.getText().toString().trim().equalsIgnoreCase("")) {
            productName.setError("Required");
            error = true;
        }

        if (productPrice.getText().toString().trim().equalsIgnoreCase("")) {
            productPrice.setError("Required");
            error = true;
        }

        for (Bitmap x : parentActivity.productImages) {
            if (x == null) {
                error = true;
                Toast.makeText(parentActivity, "Three Images Required!!", Toast.LENGTH_LONG).show();
                break;
            }
        }

        return error;
    }

    private void populateData() {
        parentActivity.productName = productName.getText().toString().trim().toUpperCase();
        parentActivity.productPrice = productPrice.getText().toString().trim();
    }
}
