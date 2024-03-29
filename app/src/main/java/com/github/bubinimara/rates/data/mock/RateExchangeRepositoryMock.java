package com.github.bubinimara.rates.data.mock;

import com.github.bubinimara.rates.data.RateExchangeRepository;
import com.github.bubinimara.rates.data.model.RateExchangeEntity;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by davide.
 */
public class RateExchangeRepositoryMock implements RateExchangeRepository {

    @Override
    public Observable<List<RateExchangeEntity>> getExchangeRate(String code) {
        LinkedHashSet<RateExchangeEntity> mockList = new LinkedHashSet<>();
        mockList.add(new RateExchangeEntity(code,1));
        //ThreadUtils.sleep(3);
        for (int i = 0; i < 10; i++) {
            String value = String.valueOf(Math.random());
            value = value.substring(0,6);
            mockList.add(new RateExchangeEntity("code_"+i,Double.valueOf(value)));
        }
        return Observable.just(new ArrayList<>(mockList));
    }
}
