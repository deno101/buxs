package com.dnz.local.buxs.marketplace;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dnz.local.buxs.R;
import com.dnz.local.buxs.concurrent.RemoveFromCart;
import com.dnz.local.buxs.utils.Currency;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterCartActivity extends RecyclerView.Adapter<RecyclerViewAdapterCartActivity.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapterCart";
    private CartActivity cartActivity;

    public RecyclerViewAdapterCartActivity(CartActivity cartActivity) {
        this.cartActivity = cartActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_viewholder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // put data to view
        holder.productName.setText(cartActivity.productDataStore.getProductName(position));
        holder.productPrice.setText(Currency.getShilling(cartActivity.productDataStore.getProductPrice(position)));

        Bitmap productImage = cartActivity.productDataStore.getProductImage(position);
        if (productImage != null) {
            holder.productImage.setImageBitmap(productImage);
            holder.progressBar.setVisibility(View.GONE);
        }

        // Set Listeners for click action
        ClickListener clickListener = new ClickListener(holder, position);
        holder.addTotal.setOnClickListener(clickListener);
        holder.reduceTotal.setOnClickListener(clickListener);
        holder.containerRemoveItem.setOnClickListener(clickListener);

        int newTotal = cartActivity.productDataStore.getPrice();
        ((TextView) cartActivity.findViewById(R.id.cart_total)).setText(Currency.getShilling(newTotal));
    }

    @Override
    public int getItemCount() {
        return cartActivity.productDataStore.length();
    }

    public void notifyItemDeleted(int position) {
        notifyItemRemoved(position);

        int newTotal = cartActivity.productDataStore.getPrice();
        ((TextView) cartActivity.findViewById(R.id.cart_total)).setText(Currency.getShilling(newTotal));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productPrice, productName, addTotal, reduceTotal, itemCount;
        public ProgressBar progressBar;
        public LinearLayout containerRemoveItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image_cart);
            productPrice = itemView.findViewById(R.id.product_price_cart);
            productName = itemView.findViewById(R.id.product_name_cart);
            progressBar = itemView.findViewById(R.id.progresbar_img_cart);
            addTotal = itemView.findViewById(R.id.add_to_item_total);
            reduceTotal = itemView.findViewById(R.id.reduce_item_total);
            itemCount = itemView.findViewById(R.id.number_of_items_in_cart);
            containerRemoveItem = itemView.findViewById(R.id.container_remove_cart_item);


        }
    }

    // Listener for click action
    private class ClickListener implements View.OnClickListener {
        private ViewHolder holder;
        private int position;

        private ClickListener(ViewHolder holder, int position) {
            this.holder = holder;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            int count = -1;
            switch (v.getId()) {
                case R.id.add_to_item_total:
                    count = Integer.parseInt((String) holder.itemCount.getText()) + 1;
                    holder.itemCount.setText(String.valueOf(count));
                    holder.reduceTotal.setBackgroundResource(R.drawable.bg_for_cart_count_and_circular_btn_orange);

                    cartActivity.productDataStore.setProductCount(position, count);
                    break;
                case R.id.reduce_item_total:
                    count = Integer.parseInt((String) holder.itemCount.getText());
                    if (count != 1) {
                        count -= 1;
                        holder.itemCount.setText(String.valueOf(count));
                        if (count == 1) {
                            holder.reduceTotal.setBackgroundResource(R.drawable.bg_for_cart_count_and_circular_btn_light_orange);
                        }
                        cartActivity.productDataStore.setProductCount(position, count);
                    }

                    break;
                case R.id.container_remove_cart_item:

                    cartActivity.productDataStore.removeItem(position);
                    notifyItemDeleted(position);
                    new RemoveFromCart().execute(position, cartActivity);
            }

            if (count != -1) {
                int newTotal = cartActivity.productDataStore.getPrice();
                ((TextView) cartActivity.findViewById(R.id.cart_total)).setText(Currency.getShilling(newTotal));
            }
        }
    }
}
