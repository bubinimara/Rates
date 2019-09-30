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
    private final static long TIMER_PERIOD = 1;// express as seconds

    private MutableLiveData<List<Rate>> rates;
    private MutableLiveData<Boolean> loading;
    private MutableLiveData<Throwable> error;

    private final Repository repository;
    private Disposable disposable;
    private List<ExchangeRate> oldExchangeRates;

    public RatesInteractor(Repository repository) {
        this.repository = repository;
        rates = new MutableLiveData<>();
        loading = new MutableLiveData<>();
        error = new MutableLiveData<>();
    }

    public void fetchRatesAtFixedTime(final String code,final double value){
        clear();
        fetchRates(code,value);
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
     * built in cold observable and backpressure
     * @param code the currency code
     * @param value the currency value
     */
    protected void fetchRates(String code,double value){
        sendLastCodeIfPossible(code, value);
        disposable = Flowable.interval(0, TIMER_PERIOD, TimeUnit.SECONDS)
                .flatMap(t -> repository.getExchangeRate(code)
                        .toFlowable()
                        .doOnComplete(()->loading.postValue(false)))
                .map(exchangeRates -> {
                    this.oldExchangeRates = exchangeRates;
                    return createRate(exchangeRates, value);
                })
                .subscribeOn(Schedulers.io())
                .subscribe(exchangeRates -> {
                    rates.postValue(exchangeRates);
                }, throwable -> {
                    error.postValue(throwable);
                    clear();
                });

    }

    private void sendLastCodeIfPossible(String code, double value) {
        if(oldExchangeRates!=null && oldExchangeRates.get(0)!=null && oldExchangeRates.get(0).getCode().equals(code)){
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

    public MutableLiveData<Throwable> getErrorLiveData() {
        return error;
    }

}
