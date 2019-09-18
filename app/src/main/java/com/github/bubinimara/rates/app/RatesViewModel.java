package com.github.bubinimara.rates.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RatesViewModel extends ViewModel {

    private LiveData<List<RateModel>> ratesLiveData;
    private RateModel currentRate;


    public RatesViewModel() {
        ratesLiveData = getLiveData();
    }

    // this will be replaced with "domain task"
    private LiveData<List<RateModel>> getLiveData() {
        final String TIMER_TASK_NAME = "RateUpdateScheduleTask";
        final long ONE_SECOND = 1000;


        Timer timerToUpdateRate = new Timer();
        MutableLiveData<List<RateModel>> liveData = new MutableLiveData<>();
        timerToUpdateRate.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LinkedHashSet<RateModel> rates = new LinkedHashSet<>();
                if(currentRate!=null)
                    rates.add(currentRate);
                for (int i = 0; i < 10; i++) {
                    String value = String.valueOf(Math.random());
                    value = value.substring(0,6);
                    rates.add(new RateModel("code_"+i,"desc_"+i,value,null));
                }
                liveData.postValue(new ArrayList<>(rates));
            }
        }, 0,ONE_SECOND);
        return liveData;
    }

    public LiveData<List<RateModel>> getRatesLiveData() {
        return ratesLiveData;
    }

    /**
     * user inputs
     * @param rateModel - the new rate value
     */
    public void onRateChanged(RateModel rateModel) {
        // todo: validate
        currentRate = rateModel;
    }
}
