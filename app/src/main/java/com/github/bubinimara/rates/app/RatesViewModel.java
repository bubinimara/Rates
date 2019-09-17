package com.github.bubinimara.rates.app;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RatesViewModel extends ViewModel {

    private LiveData<List<RateModel>> ratesLiveData;


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
                Log.d(TIMER_TASK_NAME, "run: ");
                List<RateModel> rates = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    double random = Math.random();
                    rates.add(new RateModel("code_"+i,"desc_"+i,"value_"+ random,null));
                }
                liveData.postValue(rates);
            }
        }, ONE_SECOND,ONE_SECOND);
        return liveData;
    }

    public LiveData<List<RateModel>> getRatesLiveData() {
        return ratesLiveData;
    }
}
