package com.example.ecommerce4you.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.request.RequestOptions;
import com.example.ecommerce4you.Activity.DetailActivity;
import com.example.ecommerce4you.Domain.ItemsModel;
import com.example.ecommerce4you.databinding.ViewholderPopularBinding;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {
    ArrayList<ItemsModel> items;
    Context context;


    public PopularAdapter(ArrayList<ItemsModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public PopularAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        ViewholderPopularBinding binding = ViewholderPopularBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.ViewHolder holder, int position) {
        holder.binding.priceTxt.setText("$" + items.get(position).getPrice());
        holder.binding.ratingTxt.setText("(" + items.get(position).getRating() + ")");
        holder.binding.offPercentTxt.setText(items.get(position).getOffPercent());
        holder.binding.oldPriceTxt.setText("$" + items.get(position).getOldPrice());
        holder.binding.oldPriceTxt.setPaintFlags(holder.binding.oldPriceTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        RequestOptions options = new RequestOptions();
        options = options.transform(new CenterInside());

        Glide.with(context)
                .load(items.get(position).getPicUrl().get(0))
                .apply(options)
                .into(holder.binding.pic);

        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object",items.get(position));
            context.startActivity(intent);
        });
        holder.binding.titleTxt.setText(items.get(position).getTitle());


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderPopularBinding binding;
        public ViewHolder(ViewholderPopularBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
