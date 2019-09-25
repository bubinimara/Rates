package com.github.bubinimara.rates.domain.repo;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by davide.
 */
public interface Repository {
    Single<List<ExchangeRate>> getExchangeRate(String code);
}
