package com.github.bubinimara.rates.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.github.bubinimara.rates.domain.RatesInteractor;
import com.github.bubinimara.rates.domain.repo.ExchangeRate;

import java.util.ArrayList;
import java.util.List;

public class RatesViewModel extends ViewModel {

    private LiveData<List<RateModel>> ratesLiveData;

    private RatesInteractor ratesInteractor = new RatesInteractor();
    private RateModel currentRate;


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
        return Transformations.map(ratesInteractor.getRatesLiveData(), input -> {
            List<RateModel> rates = new ArrayList<>(input.size());
            for (ExchangeRate er : input) {
                rates.add(convertExchangeRateToRateModel(er));
            }
            return rates;
        });

    }

    private RateModel convertExchangeRateToRateModel(ExchangeRate er) {
        return new RateModel(er.getCode(),er.getDescription(), getRateValue(er),null);
    }

    private String getRateValue(ExchangeRate er) {
        return (er.getExchangeRate())+" x " + currentRate.getValue();
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
        ratesInteractor.fetchRatesAtFixedTime(rateModel.getCode());
        currentRate = rateModel;
    }
}
