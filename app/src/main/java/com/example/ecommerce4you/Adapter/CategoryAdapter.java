package com.example.ecommerce4you.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce4you.Domain.CategoryModel;
import com.example.ecommerce4you.R;
import com.example.ecommerce4you.databinding.ViewholderCategoryBinding;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private ArrayList<CategoryModel> items;
    private Context context;
    private int selectedPosition=-1;
    private int lastSelectedPosition=-1;

    public CategoryAdapter(ArrayList<CategoryModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context =parent.getContext();
        ViewholderCategoryBinding binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        holder.binding.titleText.setText(items.get(position).getTitle());
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                lastSelectedPosition = selectedPosition;
                selectedPosition = position;
                notifyItemChanged(lastSelectedPosition);
                notifyItemChanged(selectedPosition);
            }
        });

        if (selectedPosition == position)
        {
            holder.binding.titleText.setBackgroundResource(R.drawable.orange_bg);
            holder.binding.titleText.setTextColor(context.getResources().getColor(R.color.white));
        }
        else
        {
            holder.binding.titleText.setBackgroundResource(R.drawable.stroke_bg);
            holder.binding.titleText.setTextColor(context.getResources().getColor(R.color.black));
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderCategoryBinding binding;
        public ViewHolder(ViewholderCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
