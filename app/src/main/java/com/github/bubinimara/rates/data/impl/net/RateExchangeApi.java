package com.github.bubinimara.rates.data.impl.net;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by davide.
 */
public interface RateExchangeApi {
    @GET("latest")
    Observable<RateExchangeNetModel> getRateExchange(@Query("base") String base);
}
