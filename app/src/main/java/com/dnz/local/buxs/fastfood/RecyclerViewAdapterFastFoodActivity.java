package com.dnz.local.buxs.fastfood;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dnz.local.buxs.R;
import com.dnz.local.buxs.utils.Currency;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterFastFoodActivity extends RecyclerView.Adapter<RecyclerViewAdapterFastFoodActivity.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private FastFoodActivity fastFoodActivity;

    public RecyclerViewAdapterFastFoodActivity(FastFoodActivity fastFoodActivity) {
      this.fastFoodActivity = fastFoodActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.marketplace_viewholder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Bitmap bitmap = fastFoodActivity.dataStore.getProductImage(position);
        if (bitmap != null){
            holder.progressBar.setVisibility(View.GONE);
        }
        holder.thumbnailImage.setImageBitmap(bitmap);
        holder.price.setText(Currency.getShilling(fastFoodActivity.dataStore.getProductPrice(position)));
        holder.itemName.setText(fastFoodActivity.dataStore.getProductName(position));

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fastFoodActivity, FastFoodDescActivity.class);
                intent.putExtra("PRODUCT_ID",String.valueOf(fastFoodActivity.dataStore.getProductID(position)));
                fastFoodActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fastFoodActivity.dataStore.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnailImage;
        TextView price, itemName;
        RelativeLayout container;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnailImage = itemView.findViewById(R.id.thumbnail_image);
            price = itemView.findViewById(R.id.price);
            itemName = itemView.findViewById(R.id.item_name);
            container = itemView.findViewById(R.id.container);
            progressBar = itemView.findViewById(R.id.progress_bar_marketplace_recycler_view);
        }
    }
}
