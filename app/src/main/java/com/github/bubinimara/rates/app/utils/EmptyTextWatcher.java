package com.github.bubinimara.rates.app.utils;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by davide.
 */
public class EmptyTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
