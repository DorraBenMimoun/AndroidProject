package com.example.ecommerce4you.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce4you.Domain.BannerModel;
import com.example.ecommerce4you.Domain.CategoryModel;
import com.example.ecommerce4you.Domain.ItemsModel;
import com.example.ecommerce4you.Repository.MainRepository;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {
    private final MainRepository repository = new MainRepository();

    public LiveData<ArrayList<CategoryModel>> loadCategory(){
        return  repository.locadCategory();
    }

    public LiveData<ArrayList<BannerModel>> loadBanner(){
        return repository.locadBanner();
    }

    public LiveData<ArrayList<ItemsModel>> loadPopular(){
        return repository.locadItems();
    }

    public LiveData<ArrayList<ItemsModel>> loadItems(){
        return repository.locadItems();
    }
    public LiveData<ArrayList<ItemsModel>> loadWishlist() {
        return repository.loadWishlist();
    }



}
