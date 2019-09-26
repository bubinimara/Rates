package com.github.bubinimara.rates;

import android.app.Application;

import com.github.bubinimara.rates.di.component.ApplicationComponent;
import com.github.bubinimara.rates.di.component.DaggerApplicationComponent;
import com.github.bubinimara.rates.di.component.DaggerUiComponent;
import com.github.bubinimara.rates.di.component.UiComponent;
import com.github.bubinimara.rates.di.module.ApplicationModule;

/**
 * Created by davide.
 */
public class RatesApp extends Application {

    private UiComponent uiComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationComponent applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(getAplicationModule())
                .build();

        uiComponent = DaggerUiComponent.builder()
                .applicationComponent(applicationComponent)
                .build();
    }

    protected ApplicationModule getAplicationModule() {
        return new ApplicationModule(this);
    }

    public UiComponent getUiComponent() {
        return uiComponent;
    }
}
