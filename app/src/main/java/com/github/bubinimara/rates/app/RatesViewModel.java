package com.github.bubinimara.rates.app;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.github.bubinimara.rates.app.model.ErrorModel;
import com.github.bubinimara.rates.app.model.RateModel;
import com.github.bubinimara.rates.app.model.RateModelMapper;
import com.github.bubinimara.rates.domain.RatesInteractor;

import java.util.List;

public class RatesViewModel extends ViewModel {

    private final RatesInteractor ratesInteractor;
    private MediatorLiveData<ErrorModel> errorLiveData;

    public RatesViewModel(RatesInteractor ratesInteractor) {
        this.ratesInteractor = ratesInteractor;
        this.errorLiveData = new MediatorLiveData<>();
        initialize();
    }

    private void initialize() {
        // fetch with default values
        ratesInteractor.fetchDefaultRateAtFixedTime();
        // errors
        errorLiveData.addSource(ratesInteractor.getErrorLiveData(),(t)-> {
            switch (t.getError()){
                case NETWORK:
                    errorLiveData.postValue(new ErrorModel(ErrorModel.Error.NETWORK));
                    break;
                case INVALID_USER_INPUT_VALUE:
                    errorLiveData.postValue(new ErrorModel(ErrorModel.Error.INVALID_USER_INPUT_VALUE));
                    break;
            }
        });
    }

    public LiveData<List<RateModel>> getRatesLiveData() {
        return Transformations.map(ratesInteractor.getRatesLiveData(), RateModelMapper::createRateModel);
    }

    public LiveData<Boolean> isLoadingLiveData(){
        return ratesInteractor.isLoadingLiveData();
    }

    public LiveData<ErrorModel> getErrorLiveData() {
        return errorLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        ratesInteractor.clear();
    }

    public void onRetryCurrentRate() {
        ratesInteractor.retryLastValue();
    }

    /**
     * user inputs
     * @param rateModel - the new rate value
     */
    public void onRateChanged(@NonNull RateModel rateModel) {
        ratesInteractor.fetchRatesAtFixedTime(rateModel.getCode(), rateModel.getValue());
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
