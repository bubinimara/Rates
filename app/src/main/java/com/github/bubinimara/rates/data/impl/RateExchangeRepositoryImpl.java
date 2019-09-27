package com.github.bubinimara.rates.data.impl;

import com.github.bubinimara.rates.data.RateExchangeRepository;
import com.github.bubinimara.rates.data.impl.net.RateExchangeApi;
import com.github.bubinimara.rates.data.impl.net.RateExchangeNetModel;
import com.github.bubinimara.rates.data.model.RateExchangeEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by davide.
 */
public class RateExchangeRepositoryImpl implements RateExchangeRepository {

    //https://revolut.duckdns.org/latest?base=EUR

    private static final String BASE_URL = "https://revolut.duckdns.org";

    private RateExchangeApi rateExchangeApi;

    public RateExchangeRepositoryImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        rateExchangeApi = retrofit.create(RateExchangeApi.class);
    }

    @Override
    public Observable<List<RateExchangeEntity>> getExchangeRate(String code) {
        return rateExchangeApi.getRateExchange(code)
                .map(rateExchangeNetModel -> {
                    RateExchangeNetModel r = rateExchangeNetModel;
                    List<RateExchangeEntity> result = new ArrayList<>();
                    result.add(new RateExchangeEntity(code,1));
                    for (String key :r.getRates().keySet()){
                        Double aDouble = r.getRates().get(key);
                        double v = aDouble !=null? aDouble :0;
                        result.add(new RateExchangeEntity(key,v));
                    }
                    return (result);
                });
    }
}
