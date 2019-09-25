package com.github.bubinimara.rates.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.github.bubinimara.rates.domain.Rate;
import com.github.bubinimara.rates.domain.RatesInteractor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RatesViewModel extends ViewModel {

    private LiveData<List<RateModel>> ratesLiveData;

    private RatesInteractor ratesInteractor = new RatesInteractor();


    public RatesViewModel() {
        ratesLiveData = getLiveData();
        initialFecthOfRates();
    }

    private void initialFecthOfRates() {
        // fetch .. etc
        RateModel r = new RateModel("EUR", "", "1", null);
        onRateChanged(r);
    }


    private LiveData<List<RateModel>> getLiveData() {
        return Transformations.map(ratesInteractor.getRatesLiveData(), RateModelMapper::createRateModel);
    }

    public static class RateModelMapper{
        public static List<RateModel> createRateModel(Collection<Rate> rate){
            List<RateModel> result = new ArrayList<>(rate.size());
            for (Rate r :
                    rate) {
                result.add(createRateModel(r));
            }
            return result;
        }
        public static RateModel createRateModel(Rate rate){
            return new RateModel(rate.getCurrency(),rate.getDescription(),String.valueOf(rate.getValue()),rate.getIconUrl());
        }
    }

    public LiveData<List<RateModel>> getRatesLiveData() {
        return ratesLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        ratesInteractor.clear();
    }

    /**
     * user inputs
     * @param rateModel - the new rate value
     */
    public void onRateChanged(RateModel rateModel) {
        // todo: validate - check change
        ratesInteractor.fetchRatesAtFixedTime(rateModel.getCode(),Double.valueOf(rateModel.getValue()));
        // todo: invalidate ui - show info that the data is loading
    }
}
