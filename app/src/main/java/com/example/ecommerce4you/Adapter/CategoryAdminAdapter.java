package com.example.ecommerce4you.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce4you.Domain.CategoryModel;
import com.example.ecommerce4you.databinding.ViewholderCategoryBinding;

import java.util.ArrayList;

public class CategoryAdminAdapter extends RecyclerView.Adapter<CategoryAdminAdapter.ViewHolder> {
    private ArrayList<CategoryModel> categoryList;
    private Context context;

    public CategoryAdminAdapter(ArrayList<CategoryModel> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull

    @Override
    public CategoryAdminAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderCategoryBinding binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdminAdapter.ViewHolder holder, int position) {
        CategoryModel category = categoryList.get(position);
        holder.binding.titleText.setText(category.getTitle());

        // Appliquer le style sans sélection
        holder.binding.titleText.setBackgroundResource(com.example.ecommerce4you.R.drawable.stroke_bg);
        holder.binding.titleText.setTextColor(context.getResources().getColor(com.example.ecommerce4you.R.color.black));
    }
    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderCategoryBinding binding;

        public ViewHolder(ViewholderCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
