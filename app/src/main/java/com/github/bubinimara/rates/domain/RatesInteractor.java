package com.github.bubinimara.rates.domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.bubinimara.rates.data.RepositoryImpl;
import com.github.bubinimara.rates.domain.repo.ExchangeRate;
import com.github.bubinimara.rates.domain.repo.Repository;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by davide.
 * like GetRatesUseCase and calculate
 */
public class RatesInteractor {
    private final static long TIMER_PERIOD = 1;

    private MutableLiveData<List<ExchangeRate>> rates;
    private final Repository repository;
    private Disposable disposable;

    public RatesInteractor() {
        rates = new MutableLiveData<>();
        this.repository = RepositoryImpl.createMockRepository();
    }

    public void fetchRatesAtFixedTime(final String code){
        clear();
        fetchRates(code);
    }

    public void clear(){
        if(disposable!=null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
    /**
     * built in cold observable and backpressure
     * @param code
     */
    protected void fetchRates(String code){
        disposable = Flowable.interval(0, TIMER_PERIOD, TimeUnit.SECONDS)
                .flatMap(t -> repository.getExchangeRate(code).toFlowable())
                .subscribeOn(Schedulers.io())
                .subscribe(exchangeRates -> {
                    rates.postValue(exchangeRates);
                }, throwable -> {
                    // todo:treat it
                });

    }

    public LiveData<List<ExchangeRate>> getRatesLiveData(){
        return rates;
    }
}
