package com.github.bubinimara.rates.app.model;

import com.github.bubinimara.rates.domain.Rate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by davide.
 */
public class RateModelMapper {
    public static List<RateModel> createRateModel(Collection<Rate> rate) {
        List<RateModel> result = new ArrayList<>(rate.size());
        for (Rate r : rate) {
            result.add(createRateModel(r));
        }
        return result;
    }

    public static RateModel createRateModel(Rate rate) {
        return new RateModel(rate.getCurrency(), rate.getDescription(), String.valueOf(rate.getValue()), rate.getIconUrl());
    }
}
