package com.github.bubinimara.rates.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    // max number of allowed character for input value
    private final double MAX_ALLOWED_LENGTH_VALUE = 7;

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
        errorLiveData.addSource(ratesInteractor.getErrorLiveData(),(t)->errorLiveData.postValue(new ErrorModel(ErrorModel.Error.NETWORK)));
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
        double value = convertValueFromString(rateModel.getValue());
        String code = convertCodeFromString(rateModel.getCode());
        if(value<0){
            ratesInteractor.clear();
            errorLiveData.setValue(new ErrorModel(ErrorModel.Error.INVALID_USER_INPUT_VALUE));
        }else{
            ratesInteractor.fetchRatesAtFixedTime(code, value);
        }
    }

    /**
     * Validate user code
     * @param code the code to validate
     * @return the normalized code
     */
    private String convertCodeFromString(String code){
        return code;
    }

    /**
     * Convert the string passed as parameter into a double value
     * @param value the value to be converted
     * @return the double value or -1 on error
     */
    private double convertValueFromString(String value) throws IllegalArgumentException{
        try {
            if(value.length()<= MAX_ALLOWED_LENGTH_VALUE){
                return Double.valueOf(value);
            }
        } catch (NumberFormatException e) {
        }
        return -1;
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
