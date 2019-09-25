package com.github.bubinimara.rates.data.impl.net;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by davide.
 */
public interface RateExchangeApi {
    @GET("latest")
    Single<RateExchangeNetModel> getRateExchange(@Query("base") String base);
}
