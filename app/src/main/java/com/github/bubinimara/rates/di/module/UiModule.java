package com.github.bubinimara.rates.di.module;

import com.github.bubinimara.rates.app.RatesViewModel;
import com.github.bubinimara.rates.di.UIScope;
import com.github.bubinimara.rates.domain.RatesInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * Created by davide.
 */
@Module
public class UiModule {

    @Provides
    @UIScope
    public RatesViewModel.RatesViewModelProvider providerRatesViewModelProvider(RatesInteractor interactor){
        return new RatesViewModel.RatesViewModelProvider(interactor);
    }
}
