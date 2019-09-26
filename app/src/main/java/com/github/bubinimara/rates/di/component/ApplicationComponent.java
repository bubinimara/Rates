package com.github.bubinimara.rates.di.component;

import android.content.Context;

import com.github.bubinimara.rates.RatesApp;
import com.github.bubinimara.rates.di.module.ApplicationModule;
import com.github.bubinimara.rates.domain.repo.Repository;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by davide.
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(RatesApp ratesApp);

    //expose sub-graph
    Context provideContext();
    Repository provideRepository();

}
