package com.github.bubinimara.rates.app;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.github.bubinimara.rates.app.model.RateModel;
import com.github.bubinimara.rates.app.model.RateModelMapper;
import com.github.bubinimara.rates.domain.RatesInteractor;

import java.util.List;

public class RatesViewModel extends ViewModel {

    private final RatesInteractor ratesInteractor;

    public RatesViewModel(RatesInteractor ratesInteractor) {
        this.ratesInteractor = ratesInteractor;
        initialize();
    }

    private void initialize() {
        // fetch with default values
        ratesInteractor.fetchDefaultRateAtFixedTime();
    }

    public LiveData<List<RateModel>> getRatesLiveData() {
        return Transformations.map(ratesInteractor.getRatesLiveData(), RateModelMapper::createRateModel);
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
        Double aDouble = null;
        try {
            aDouble = Double.valueOf(rateModel.getValue());
        } catch (NumberFormatException e) {
            aDouble = 1.0;
            // todo: show error in ui
        }
        ratesInteractor.fetchRatesAtFixedTime(rateModel.getCode(), aDouble);
    }

    public static class RatesViewModelProvider implements ViewModelProvider.Factory {

        private RatesInteractor interactor;

        public RatesViewModelProvider(RatesInteractor interactor) {
            this.interactor = interactor;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new RatesViewModel(interactor);
        }
    }

}
