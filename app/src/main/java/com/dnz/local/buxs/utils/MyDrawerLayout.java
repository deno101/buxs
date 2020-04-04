package com.dnz.local.buxs.utils;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dnz.local.buxs.R;
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

public class MyDrawerLayout implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    private AppCompatActivity context;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;

    public MyDrawerLayout(AppCompatActivity context) {
        this.context = context;
        this.setToolbarListeners();
    }

    public void initDrawerLayout(){
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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Todo: set listeners for all the menu items
        return false;
    }

    private void setToolbarListeners(){
        context.findViewById(R.id.cart_toolbar_container).setOnClickListener(this);
        context.findViewById(R.id.cart_toolbar_image).setOnClickListener(this);
        context.findViewById(R.id.menu_more_vert).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu_more_vert){
            Toast.makeText(context, "Clicked dots vertical", Toast.LENGTH_SHORT).show();
        }else{
            if (context instanceof MarketPlaceDescActivity){
                ((MarketPlaceDescActivity) context).startCartActivity();
            }else if (context instanceof MarketPlaceActivity){
                ((MarketPlaceActivity) context).startCartActivity();
            }
        }
    }
}
