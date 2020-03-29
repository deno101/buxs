package com.dnz.local.buxs.marketplace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dnz.local.buxs.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterCartActivity extends RecyclerView.Adapter{

    public RecyclerViewAdapterCartActivity() {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_viewholder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView productImage;
        public TextView productPrice, productName;
        public ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image_cart);
            productPrice = itemView.findViewById(R.id.product_price_cart);
            productName = itemView.findViewById(R.id.product_name_cart);
            progressBar = itemView.findViewById(R.id.progresbar_img_cart);
        }
    }
}
