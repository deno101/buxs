package com.dnz.local.buxs.utils;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dnz.local.buxs.LoginActivity;
import com.dnz.local.buxs.R;
import com.dnz.local.buxs.fastfood.FastFoodActivity;
import com.dnz.local.buxs.marketplace.AddProductActivity;
import com.dnz.local.buxs.marketplace.CartActivity;
import com.dnz.local.buxs.marketplace.MarketPlaceActivity;
import com.dnz.local.buxs.marketplace.MarketPlaceDescActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class MyDrawerLayout implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private AppCompatActivity context;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;

    public MyDrawerLayout(AppCompatActivity context) {
        this.context = context;
        this.setToolbarListeners();
    }

    public void initDrawerLayout() {
        toolbar = context.findViewById(R.id.toolbar);
        drawerLayout = context.findViewById(R.id.drawer_layout);

        context.setSupportActionBar(toolbar);

        ActionBar actionBar = context.getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);

        drawerLayout = context.findViewById(R.id.drawer_layout);
        navigationView = context.findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(this);

        drawerToggle = new ActionBarDrawerToggle(context, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();

        navigationView.getHeaderView(0).findViewById(R.id.login_button_drawer_layout).setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upload_item:
                context.startActivity(new Intent(context, AddProductActivity.class));
                drawerLayout.closeDrawers();
                break;

            case R.id.to_fast_food:
                context.startActivity(new Intent(context, FastFoodActivity.class));
                drawerLayout.closeDrawers();
                break;

            case R.id.to_market_place:
                context.startActivity(new Intent(context, MarketPlaceActivity.class));
                drawerLayout.closeDrawers();
                break;

            default:
                Toast.makeText(context, "To-be Implemented.", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    private void setToolbarListeners() {
        context.findViewById(R.id.cart_toolbar_container).setOnClickListener(this);
        context.findViewById(R.id.cart_toolbar_image).setOnClickListener(this);
        context.findViewById(R.id.menu_more_vert).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_more_vert:
                Toast.makeText(context, "Clicked dots vertical", Toast.LENGTH_SHORT).show();
                break;

            case R.id.login_button_drawer_layout:
                context.startActivity(new Intent(context, LoginActivity.class));
                break;

            default:
                if (context instanceof MarketPlaceDescActivity) {
                    ((MarketPlaceDescActivity) context).startCartActivity();
                } else if (context instanceof MarketPlaceActivity) {
                    ((MarketPlaceActivity) context).startCartActivity();
                }
        }
    }
}
