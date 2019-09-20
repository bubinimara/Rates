package com.github.bubinimara.rates.domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.bubinimara.rates.domain.repo.ExchangeRate;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by davide.
 * like GetRatesUseCase and calculate
 */
public class RatesInteractor {
    final String TIMER_TASK_NAME = "RateUpdateScheduleTask";
    final long TIMER_PERIOD = 3000;

    private MutableLiveData<List<ExchangeRate>> rates;
    private Timer timerToUpdateRate;

    public RatesInteractor() {
        rates = new MutableLiveData<>();
    }

    public void fetchRatesAtFixedTime(final String code){
        cancelTimer();
        timerToUpdateRate = new Timer(TIMER_TASK_NAME);
        timerToUpdateRate.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchRates(code);
            }
        }, 0, TIMER_PERIOD);
    }

    private void cancelTimer() {
        if(timerToUpdateRate!=null){
            timerToUpdateRate.cancel();
            timerToUpdateRate.purge();
            timerToUpdateRate = null;
        }
    }

    protected void fetchRates(String code){
        LinkedHashSet<ExchangeRate> ratesList = new LinkedHashSet<>();
        if(code!=null)
            ratesList.add(new ExchangeRate(code,0));
        for (int i = 0; i < 10; i++) {
            String value = String.valueOf(Math.random());
            value = value.substring(0,6);
            ratesList.add(new ExchangeRate("code_"+i,Double.valueOf(value)));
        }
        rates.postValue(new ArrayList<>(ratesList));
    }

    public LiveData<List<ExchangeRate>> getRatesLiveData(){
        return rates;
    }
}
