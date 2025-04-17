package com.example.ecommerce4you.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.ecommerce4you.Domain.BannerModel;
import com.example.ecommerce4you.R;

import java.util.ArrayList;

public class SliderAdpater extends RecyclerView.Adapter<SliderAdpater.SliderViewHolder> {
    private ArrayList<BannerModel> sliderItems;
    private ViewPager2 viewPager2;
    private Context context;
    private Runnable runnable = new Runnable(){
        @Override
        public void run (){
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };

    public SliderAdpater( ArrayList<BannerModel> sliderItems,ViewPager2 viewPager2) {
        this.viewPager2 = viewPager2;
        this.sliderItems = sliderItems;
    }

    @NonNull
    @Override
    public SliderAdpater.SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new SliderViewHolder(LayoutInflater.from(context).inflate(R.layout.slider_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdpater.SliderViewHolder holder, int position) {
        holder.setImage(sliderItems.get(position));
        if (position== sliderItems.size()-2)
        {
            viewPager2.post(runnable);
        }

    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }

        void setImage(BannerModel bannerModel) {
            Glide.with(context)
                    .load(bannerModel.getUrl())
                    .into(imageView);
        }
    }
}
