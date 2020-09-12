package com.dnz.local.buxs.marketplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import com.dnz.local.buxs.R;
import com.dnz.local.buxs.marketplace.fragments.CameraCaptureFragment;
import com.dnz.local.buxs.marketplace.fragments.DescriptionFragment;

public class AddProductActivity extends AppCompatActivity {

    private Fragment cameraFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        cameraFragment = new DescriptionFragment();

        if (savedInstanceState == null){
            ft.add(R.id.fragment_container, cameraFragment).commit();
        }
    }
}
