package com.dnz.local.buxs.marketplace;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dnz.local.buxs.R;
import com.dnz.local.buxs.utils.Currency;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterCartActivity extends RecyclerView.Adapter<RecyclerViewAdapterCartActivity.ViewHolder>{
    private CartActivity cartActivity;
    public RecyclerViewAdapterCartActivity(CartActivity cartActivity) {
        this.cartActivity = cartActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_viewholder, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // put data to view
        ViewHolder mHolder = (ViewHolder) holder;
        mHolder.productName.setText(cartActivity.productDataStore.getProductName(position));
        mHolder.productPrice.setText(Currency.getShilling(cartActivity.productDataStore.getProductPrice(position)));

        Bitmap productImage = cartActivity.productDataStore.getProductImage(position);
        if (productImage != null){
            mHolder.productImage.setImageBitmap(productImage);
            mHolder.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return cartActivity.productDataStore.length();
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
