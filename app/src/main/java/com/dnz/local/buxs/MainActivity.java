package com.dnz.local.buxs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.dnz.local.buxs.marketplace.MarketPlaceActivity;
import com.dnz.local.buxs.net.MyCookieStore;
import com.dnz.local.buxs.utils.MyDrawerLayout;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static MyCookieStore cookieStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = this.getWindow();
        // set status bar for sdk > lollipop
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        new MyDrawerLayout(this).initDrawerLayout();
        findViewById(R.id.cart_toolbar_container).setVisibility(View.INVISIBLE);

        cookieStore = new MyCookieStore(this, "cookieStore");
        CookieManager cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);


//        // check if a 'username' cookie exists the show username
//        if (cookieStore.getAuthenticator().isAuthenticated()) {
//            username.setText(cookieStore.getAuthenticator().getUsername());
//            username.setVisibility(View.VISIBLE);
//
//            user.setImageResource(R.drawable.ic_person_authenticated);
//            user.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //Todo: show account information
//
//                }
//            });
//        }else {
//            user.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                }
//            });
//        }
//
//
//        dotsVert.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO: SHOW A HELPER MENU
//                showToast("Clicked dots-vert menu");
//            }
//        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    public void toFastFood(View view) {

    }

    public void toMarketPlace(View view) {
        startActivity(new Intent(MainActivity.this, MarketPlaceActivity.class));
    }

    public static MyCookieStore getCookieStore() {
        return cookieStore;
    }

}
