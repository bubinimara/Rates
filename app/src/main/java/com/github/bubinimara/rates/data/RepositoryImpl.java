package com.github.bubinimara.rates.data;

import com.github.bubinimara.rates.data.mock.RateExchangeApiMock;
import com.github.bubinimara.rates.data.mock.RateInfoApiMock;
import com.github.bubinimara.rates.domain.repo.ExchangeRate;
import com.github.bubinimara.rates.domain.repo.Repository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by davide.
 */
public class RepositoryImpl implements Repository {

    public interface RateExchangeApi {
        Observable<List<RateExchangeEntity>> getExchangeRate(String code);
    }

    public interface RateInfoApi {
        Observable<RateInfoEntity> getRateInfo(String code);
    }

    private RateExchangeApi rateExchangeApi;
    private RateInfoApi rateinfoApi;

    public RepositoryImpl(RateExchangeApi rateExchangeApi, RateInfoApi rateinfoApi) {
        this.rateExchangeApi = rateExchangeApi;
        this.rateinfoApi = rateinfoApi;
    }

    public RepositoryImpl() {
        this.rateExchangeApi = new RateExchangeApiMock();
        this.rateinfoApi = new RateInfoApiMock();
    }

    public Single<List<ExchangeRate>> getExchangeRate(String code){
        return rateExchangeApi.getExchangeRate(code)
                .flatMapIterable(m->m)
                .switchMap(rateExchangeEntity -> rateinfoApi.getRateInfo(rateExchangeEntity.getCurrency())
                        .map(rateInfoEntity -> new ExchangeRate(rateExchangeEntity.getCurrency(),rateInfoEntity.getDescription(),rateExchangeEntity.getValue(),rateInfoEntity.getIconUrl())))
                .toList();
    }


}
