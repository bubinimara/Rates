package com.github.bubinimara.rates.di.module;

import com.github.bubinimara.rates.app.RatesViewModel;
import com.github.bubinimara.rates.di.UIScope;
import com.github.bubinimara.rates.domain.RatesInteractor;
import com.github.bubinimara.rates.domain.repo.Repository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by davide.
 */
@Module
public class UiModule {

    @UIScope
    @Provides
    public RatesInteractor provideRatesInteractor(Repository repository){
        return new RatesInteractor(repository);
    }
    @Provides
    @UIScope
    public RatesViewModel.RatesViewModelProvider providerRatesViewModelProvider(RatesInteractor interactor){
        return new RatesViewModel.RatesViewModelProvider(interactor);
    }
}
