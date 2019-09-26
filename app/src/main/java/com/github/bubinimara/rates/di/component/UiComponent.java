package com.github.bubinimara.rates.di.component;

import com.github.bubinimara.rates.app.RatesFragment;
import com.github.bubinimara.rates.di.UIScope;
import com.github.bubinimara.rates.di.module.UiModule;

import dagger.Component;

/**
 * Created by davide.
 */
@UIScope
@Component(modules = {UiModule.class},dependencies = ApplicationComponent.class)
public interface UiComponent {
    void inject(RatesFragment ratesFragment);
}
