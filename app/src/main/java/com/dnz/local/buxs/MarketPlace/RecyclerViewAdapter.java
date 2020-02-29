package com.dnz.local.buxs.MarketPlace;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dnz.local.buxs.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<Bitmap> thumbnail= new ArrayList<>();
    private ArrayList<String> itemName = new ArrayList<>();
    private  ArrayList<Integer> price = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapter(Context context, ArrayList<Bitmap> thumbnail, ArrayList<String> itemName, ArrayList<Integer> price) {
        this.thumbnail = thumbnail;
        this.itemName = itemName;
        this.price = price;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.marketplace_viewholder,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        try {
            holder.thumbnailImage.setImageBitmap(this.thumbnail.get(position));
        }catch (Exception e){
            Log.d(TAG, "onBindViewHolder: "+e.getMessage());
            holder.thumbnailImage.setImageBitmap(null);
        }
        holder.itemName.setText(this.itemName.get(position));

        Log.d(TAG, "onBindViewHolder: success");
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance("KSH"));
        holder.price.setText(format.format((double) this.price.get(position)));

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+ position);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+thumbnail.size());
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnailImage;
        TextView price,itemName;
        RelativeLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnailImage = itemView.findViewById(R.id.thumbnail_image);
            price = itemView.findViewById(R.id.price);
            itemName = itemView.findViewById(R.id.item_name);
            container = itemView.findViewById(R.id.container);
            if(container != null) {
                Log.d(TAG, "ViewHolder: ++++container");
            }
        }
    }

    public void refresh(ArrayList<Bitmap> thumbnail){
        this.thumbnail = thumbnail;

        notifyDataSetChanged();
    }
}
