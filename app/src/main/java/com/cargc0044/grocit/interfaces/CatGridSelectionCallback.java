package com.cargc0044.grocit.interfaces;

/**
 * Created by Nguza Yikona on 4/21/2016.
 */
//! callback interface listen by CategoryActivity to detect user click on category
public interface CatGridSelectionCallback{
    void onCatGridSelected(String catID, String title);
}