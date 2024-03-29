package com.github.bubinimara.rates.domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.bubinimara.rates.domain.repo.ExchangeRate;
import com.github.bubinimara.rates.domain.repo.Repository;

import org.javamoney.moneta.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.money.Monetary;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by davide.
 * like GetRatesUseCase and calculate
 * Note: A good approach will be to separate the task to fetch and the task to compute the value in two different classes
 */
public class RatesInteractor {
    private static final String DEFAULT_CODE = "EUR";
    private static final double DEFAULT_VALUE = 100;
    private static final long TIMER_PERIOD = 1;// express as seconds
    private static final double MAX_ALLOWED_LENGTH_VALUE = 7;


    private MutableLiveData<List<Rate>> rates;
    private MutableLiveData<Boolean> loading;
    private MutableLiveData<RateInteractorException> error;

    private final Repository repository;
    private Disposable disposable;

    private List<ExchangeRate> oldExchangeRates;
    private String currentCode;
    private double currentValue;

    public RatesInteractor(Repository repository) {
        this.repository = repository;
        this.currentCode = DEFAULT_CODE;
        this.currentValue = DEFAULT_VALUE;

        this.rates = new MutableLiveData<>();
        this.loading = new MutableLiveData<>();
        this.error = new MutableLiveData<>();
    }

    public void retryLastValue() {
        clear();
        fetchRates(currentCode,currentValue);
    }

    /**
     * Fetch and calculate the the exchange rate at fixed time
     * @param code the currency code
     * @param value the double value
     */
    public void fetchRatesAtFixedTime(final String code,final String value){
        clear();
        try {
            String mCode = convertCodeFromString(code);
            double mValue = convertValueFromString(value);
            fetchRates(mCode,mValue);
        } catch (RateInteractorException e) {
            e.printStackTrace();
            error.postValue(e);
            rates.postValue(createRate(oldExchangeRates,0));
        }
    }

    public void fetchRatesAtFixedTime(final String code,final double value){
        fetchRatesAtFixedTime(code,String.valueOf(value));
    }

    public void fetchDefaultRateAtFixedTime(){
        fetchRates(DEFAULT_CODE,DEFAULT_VALUE);
    }

    public void clear(){
        if(disposable!=null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /**
     * fetch rates
     * built in cold observable and backpressure
     *
     * @param code the currency code
     * @param value the currency value
     */
    protected synchronized void fetchRates(String code,double value){
        this.currentCode = code;
        this.currentValue = value;

        sendLastCodeIfPossible(code, value);
        disposable = Flowable.interval(0, TIMER_PERIOD, TimeUnit.SECONDS)
                .flatMap(t -> repository.getExchangeRate(code)
                        .toFlowable()
                        )
                .doOnEach((e)->loading.postValue(false))
                .map(exchangeRates -> {
                    this.oldExchangeRates = exchangeRates;
                    return createRate(exchangeRates, value);
                })
                .subscribeOn(Schedulers.io())
                .subscribe(exchangeRates -> {
                    rates.postValue(exchangeRates);
                }, throwable -> {
                    clear(); // stop current
                    error.postValue(new RateInteractorException(RateInteractorException.ErrorType.NETWORK,throwable));
                });

    }

    private void sendLastCodeIfPossible(String code, double value) {
        if(oldExchangeRates!=null && oldExchangeRates.get(0)!=null && oldExchangeRates.get(0).getCode().equals(code)){
            loading.postValue(false);
            rates.setValue(createRate(oldExchangeRates,value));
        }else {
            loading.postValue(true);
        }
    }

    /**
     * Calculate the value and map the result
     *
     * @param exchangeRates
     * @param value
     * @return
     */
    private List<Rate> createRate(List<ExchangeRate> exchangeRates,double value) {
        List<Rate> result = new ArrayList<>(exchangeRates.size());
        for (ExchangeRate er : exchangeRates) {
            double v = Money.of(value,er.getCode())
                    .multiply(er.getExchangeRate())
                    .with(Monetary.getDefaultRounding())
                    .getNumber()
                    .doubleValueExact();
            
            result.add(new Rate(er.getCode(),er.getDescription(),v,er.getIconUrl()));
        }
        return result;
    }

    public LiveData<List<Rate>> getRatesLiveData(){
        return rates;
    }

    public MutableLiveData<Boolean> isLoadingLiveData() {
        return loading;
    }

    public MutableLiveData<RateInteractorException> getErrorLiveData() {
        return error;
    }


    /**
     * Validate user code
     * @param code the code to validate
     * @return the normalized code
     * @throws RateInteractorException if the code passed as parameter is not a valid code
     */
    private String convertCodeFromString(String code) throws RateInteractorException {
        if(code.isEmpty()){
            throw new RateInteractorException("The currency code cannot be empty",RateInteractorException.ErrorType.INVALID_USER_INPUT_VALUE);
        }
        return code;
    }

    /**
     * Validate the input value passed as parameter and
     * Convert the string passed as parameter into a double value
     *
     * @param value the value to be converted
     * @return the double value or -1 on error
     * @throws RateInteractorException if the value passed is not a valid value
     */
    private double convertValueFromString(String value) throws RateInteractorException{
        try {
            if(value.length() > MAX_ALLOWED_LENGTH_VALUE){
                throw new RateInteractorException("The value exceed the maximum allowed size [ "+value+" ] ",RateInteractorException.ErrorType.INVALID_USER_INPUT_VALUE);
            }
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            throw new RateInteractorException("the value is no a double [ "+value+ " ]",RateInteractorException.ErrorType.INVALID_USER_INPUT_VALUE);
        }
    }
}
