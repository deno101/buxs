package com.dnz.local.buxs.utils;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;

import com.dnz.local.buxs.R;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class MyDrawerLayout implements NavigationView.OnNavigationItemSelectedListener {
    private AppCompatActivity context;

    private DrawerLayout drawerLayout;
    private View content;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;

    public MyDrawerLayout(AppCompatActivity context) {
        this.context = context;
    }

    public void initDrawerLayout(){
        toolbar = context.findViewById(R.id.toolbar);
        drawerLayout = context.findViewById(R.id.drawer_layout);

        context.setSupportActionBar(toolbar);

        ActionBar actionBar = context.getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = context.findViewById(R.id.drawer_layout);
        navigationView = context.findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(this);

        drawerToggle = new ActionBarDrawerToggle(context, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
