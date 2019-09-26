package com.github.bubinimara.rates.domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.bubinimara.rates.data.RepositoryImpl;
import com.github.bubinimara.rates.domain.repo.ExchangeRate;
import com.github.bubinimara.rates.domain.repo.Repository;

import org.javamoney.moneta.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by davide.
 * like GetRatesUseCase and calculate
 * Note: A good approach will be to separate the task to fetch and the task to compute the value in two different classes
 *
 * todo: add option ( in repository ) to not update when change only the value, like get from cache and then update
 *
 */
public class RatesInteractor {
    private static final String DEFAULT_CODE = "EUR";
    private static final double DEFAULT_VALUE = 100;
    private final static long TIMER_PERIOD = 1;// express as seconds

    private MutableLiveData<List<Rate>> rates;
    private final Repository repository;
    private Disposable disposable;

    public RatesInteractor(Repository repository) {
        this.repository = repository;
        rates = new MutableLiveData<>();
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
        disposable = Flowable.interval(0, TIMER_PERIOD, TimeUnit.SECONDS)
                .flatMap(t -> repository.getExchangeRate(code).toFlowable())
                .map(exchangeRates -> createRate(exchangeRates,value))
                .subscribeOn(Schedulers.io())
                .subscribe(exchangeRates -> {
                    rates.postValue(exchangeRates);
                }, throwable -> {
                    // todo:treat it
                });

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
            double v = Money.of(er.getCode(), value)
                    .multiply(er.getExchangeRate())
                    .getNumber()
                    .doubleValueExact();
            result.add(new Rate(er.getCode(),er.getDescription(),v,er.getIconUrl()));
        }
        return result;
    }

    public LiveData<List<Rate>> getRatesLiveData(){
        return rates;
    }
}
