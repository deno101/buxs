package com.dnz.local.buxs.marketplace;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dnz.local.buxs.R;
import com.dnz.local.buxs.utils.Currency;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterMarketPlaceActivity extends RecyclerView.Adapter<RecyclerViewAdapterMarketPlaceActivity.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private MarketPlaceActivity marketPlaceActivity;

    public RecyclerViewAdapterMarketPlaceActivity(MarketPlaceActivity marketPlaceActivity) {
      this.marketPlaceActivity = marketPlaceActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.marketplace_viewholder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.thumbnailImage.setImageBitmap(marketPlaceActivity.dataStore.getProductImage(position));
        holder.price.setText(Currency.getShilling(marketPlaceActivity.dataStore.getProductPrice(position)));
        holder.itemName.setText(marketPlaceActivity.dataStore.getProductName(position));

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(marketPlaceActivity, MarketPlaceDescActivity.class);
                intent.putExtra("PRODUCT_ID",String.valueOf(marketPlaceActivity.dataStore.getProductID(position)));
                marketPlaceActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return marketPlaceActivity.dataStore.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnailImage;
        TextView price, itemName;
        RelativeLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnailImage = itemView.findViewById(R.id.thumbnail_image);
            price = itemView.findViewById(R.id.price);
            itemName = itemView.findViewById(R.id.item_name);
            container = itemView.findViewById(R.id.container);
        }
    }
}
