package com.github.bubinimara.rates.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class RatesViewModel extends ViewModel {
    private LiveData<List<RateModel>> ratesLiveData;

    public RatesViewModel() {
        ratesLiveData = getLiveData();
    }

    private LiveData<List<RateModel>> getLiveData() {
        MutableLiveData<List<RateModel>> liveData = new MutableLiveData<>();
        List<RateModel> rates = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            rates.add(new RateModel("code_"+i,"desc_"+i,"value_"+i,null));
        }
        liveData.postValue(rates);
        return liveData;
    }

    public LiveData<List<RateModel>> getRatesLiveData() {
        return ratesLiveData;
    }
}
