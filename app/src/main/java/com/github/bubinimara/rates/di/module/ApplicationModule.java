package com.github.bubinimara.rates.di.module;

import android.content.Context;

import com.github.bubinimara.rates.RatesApp;
import com.github.bubinimara.rates.data.CurrencyInfoRepository;
import com.github.bubinimara.rates.data.RateExchangeRepository;
import com.github.bubinimara.rates.data.RepositoryImpl;
import com.github.bubinimara.rates.data.impl.CurrencyInfoRepositoryImpl;
import com.github.bubinimara.rates.data.impl.RateExchangeRepositoryImpl;
import com.github.bubinimara.rates.domain.repo.Repository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by davide.
 */
@Module
public class ApplicationModule {
    private final RatesApp application;

    public ApplicationModule(RatesApp application) {
        this.application = application;
    }

    @Singleton
    @Provides
    public RatesApp provideApplication() {
        return application;
    }

    @Singleton
    @Provides
    public Context provideContext() {
        return application;
    }

    @Singleton
    @Provides
    public RateExchangeRepository provideRateExchangeRepository(){
        return new RateExchangeRepositoryImpl();
    }

    @Singleton
    @Provides
    public CurrencyInfoRepository provideCurrencyInfoRepository(){
        return new CurrencyInfoRepositoryImpl();
    }

    @Singleton
    @Provides
    public Repository provideRepository(RateExchangeRepository rateExchangeRepository, CurrencyInfoRepository currencyInfoRepository){
        return new RepositoryImpl(rateExchangeRepository,currencyInfoRepository);
    }
}
