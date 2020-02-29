package com.dnz.local.buxs.MarketPlace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.dnz.local.buxs.R;

import java.util.ArrayList;

public class MarketPlaceActivity extends AppCompatActivity {

    private static final String TAG = "MarketPlaceActivity";
    private ArrayList<Integer> thumbnail = new ArrayList<>();
    private ArrayList<String> itemName = new ArrayList<>();
    private ArrayList<Integer> price = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place);

        initList();
        initRecyclerView();
    }

    private void initList(){
        for (int i = 0; i < 10; i++){
            this.thumbnail.add(R.drawable.ic_keyboard_black_50dp);
        }

        for (int i = 0; i < 10; i++){
            this.itemName.add("Test: "+i);
        }

        for (int i = 0; i < 10; i++){
            this.price.add((int) (Math.random()*100));
        }
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: ...called");
        recyclerView = findViewById(R.id.recyclerview_market_place);
        RecyclerViewAdapter viewAdapter = new RecyclerViewAdapter(this,thumbnail,itemName,price);
        recyclerView.setAdapter(viewAdapter);

        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }
}
