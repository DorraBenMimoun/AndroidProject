package com.example.ecommerce4you.Domain;

public class CategoryModel {
    private String title;

    public CategoryModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public CategoryModel(String newCategory) {
        this.title =newCategory;
    }
    @Override
    public String toString() {
        return title; // Pour afficher uniquement le titre dans le spinner
    }

}
