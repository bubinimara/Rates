package com.github.bubinimara.rates.app.utils;

import androidx.recyclerview.widget.DiffUtil;

import com.github.bubinimara.rates.app.model.RateModel;

import java.util.List;

/**
 * Created by davide.
 */
public class RatesDiffUtils extends DiffUtil.Callback {
    private final List<RateModel> oldRates;
    private final List<RateModel> newRates;

    public RatesDiffUtils(List<RateModel> oldRates, List<RateModel> newRates) {
        this.oldRates = oldRates;
        this.newRates = newRates;
    }

    @Override
    public int getOldListSize() {
        return oldRates.size();
    }

    @Override
    public int getNewListSize() {
        return newRates.size();
    }

    private RateModel getOldItem(int oldItemPosition) {
        return oldRates.get(oldItemPosition);
    }

    private RateModel getNewItem(int newItemPosition) {
        return newRates.get(newItemPosition);
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        RateModel newItem = getNewItem(newItemPosition);
        RateModel oldItem = getOldItem(oldItemPosition);
        return newItem.isTheSameAs(oldItem);
    }


    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        RateModel newItem = getNewItem(newItemPosition);
        RateModel oldItem = getOldItem(oldItemPosition);
        return newItem.areContentsTheSame(oldItem);
    }

}
