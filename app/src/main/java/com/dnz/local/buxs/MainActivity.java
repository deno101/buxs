package com.dnz.local.buxs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.dnz.local.buxs.MarketPlace.MarketPlaceActivity;

public class MainActivity extends AppCompatActivity {

    ImageView user,dotsVert;

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = this.getWindow();
//        set status bar for sdk > lollipop
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

//        add onclick listeners for image views
        user = findViewById(R.id.user);
        dotsVert = findViewById(R.id.dots_vert);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        dotsVert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Clicked dots-vert menu");
            }
        });
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    public void toFastFood(View view){

    }

    public void toMarketPlace(View view){
        startActivity(new Intent(MainActivity.this, MarketPlaceActivity.class));
    }
}