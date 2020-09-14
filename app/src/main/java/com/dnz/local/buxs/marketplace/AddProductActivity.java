package com.dnz.local.buxs.marketplace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.dnz.local.buxs.R;
import com.dnz.local.buxs.marketplace.fragments.CameraCaptureFragment;
import com.dnz.local.buxs.marketplace.fragments.DescriptionFragment;
import com.dnz.local.buxs.marketplace.fragments.PreviewFragment;

import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {

    private static final String TAG = "AddProductActivity";

    public CameraCaptureFragment cameraFragment;
    public PreviewFragment previewFragment;
    public DescriptionFragment descriptionFragment;
    public String productName, productPrice, productBrand, productDescription;

    public Bitmap[] productImages = new Bitmap[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        Window window = this.getWindow();

        // set status bar for sdk > lollipop
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        cameraFragment = new CameraCaptureFragment(AddProductActivity.this);
        previewFragment = new PreviewFragment(AddProductActivity.this);
        descriptionFragment = new DescriptionFragment(AddProductActivity.this);

        if (savedInstanceState == null) {
            ft.add(R.id.fragment_container, cameraFragment).commit();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Log.e(TAG, "onActivityResult: error invalid result code", null);
            return;
        }

        Bitmap image = (Bitmap) data.getExtras().get("data");

        switch (requestCode) {
            case CameraCaptureFragment.IMAGE_ONE:
                productImages[0] = image;
                cameraFragment.setImage(image);
                cameraFragment.setImageToCheck(1);
                break;
            case CameraCaptureFragment.IMAGE_TWO:
                productImages[1] = image;
                cameraFragment.setImage(image);
                cameraFragment.setImageToCheck(2);
                break;
            case CameraCaptureFragment.IMAGE_THREE:
                productImages[2] = image;
                cameraFragment.setImage(image);
                cameraFragment.setImageToCheck(3);
                break;
        }
    }

}
