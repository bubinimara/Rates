package com.github.bubinimara.rates.app;

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

    private LiveData<List<RateModel>> ratesLiveData;
    private final RatesInteractor ratesInteractor;

    // this will be removed
    public RatesViewModel() {
        this(new RatesInteractor());

    }

    public RatesViewModel(RatesInteractor ratesInteractor) {
        this.ratesInteractor = ratesInteractor;
        initialize();
    }

    private void initialize() {
        // fetch with default values
        ratesInteractor.fetchDefaultRateAtFixedTime();
        // the data ( can be replaced by inline function )
        ratesLiveData = Transformations.map(ratesInteractor.getRatesLiveData(), RateModelMapper::createRateModel);
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
