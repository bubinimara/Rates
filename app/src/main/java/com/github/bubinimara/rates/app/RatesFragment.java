package com.github.bubinimara.rates.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.bubinimara.rates.R;
import com.github.bubinimara.rates.RatesApp;
import com.github.bubinimara.rates.app.model.ErrorModel;
import com.github.bubinimara.rates.app.model.RateModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RatesFragment extends Fragment implements RatesAdapter.RateChangeListener {

    private RatesViewModel mViewModel;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    RatesAdapter adapter;

    @Inject
    RatesViewModel.RatesViewModelProvider viewModelProvider;

    public static RatesFragment newInstance() {
        return new RatesFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        RatesApp.getUiComponent(context)
                .inject(this);

        adapter = new RatesAdapter(context.getApplicationContext());
        adapter.setRateChangeListener(this);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rates_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this,viewModelProvider).get(RatesViewModel.class);
        mViewModel.getRatesLiveData().observe(this,this::onDataChanged);
        mViewModel.isLoadingLiveData().observe(this,this::isLoading);
        mViewModel.getErrorLiveData().observe(this,this::onError);
    }

    private void onError(ErrorModel errorModel) {
        if(errorModel.isHandled()){
            // already handled
            return;
        }
        switch (errorModel.getError()){
            case NETWORK:
                getString(R.string.app_name);
                Snackbar.make(progressBar,R.string.error_network,Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.btn_retry, v -> {
                            mViewModel.onRetryCurrentRate();
                            errorModel.markAsHandled();
                        })
                        .show();
                break;
            case INVALID_USER_INPUT_VALUE:
                errorModel.markAsHandled();
                Snackbar.make(progressBar,R.string.error_invalid_user_input_value,Snackbar.LENGTH_SHORT)
                        .show();
                break;
        }
    }

    private void isLoading(Boolean isLoading) {
        if(isLoading == null) {
            return;
        }
        //(adapter.setIsLoading(isLoading)) // can display items in gray for example
        progressBar.setVisibility(isLoading?View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

    }

    private void onDataChanged(List<RateModel> rateModels) {
        adapter.updateRates(rateModels);
    }

    @Override
    public void onRateChange(RateModel rateModel) {
        mViewModel.onRateChanged(rateModel);
        recyclerView.scrollToPosition(0);
    }
}
