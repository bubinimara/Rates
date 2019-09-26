package com.github.bubinimara.rates.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.bubinimara.rates.R;
import com.github.bubinimara.rates.app.model.RateModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RatesFragment extends Fragment implements RatesAdapter.RateChangeListener {

    private RatesViewModel mViewModel;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    RatesAdapter adapter;

    public static RatesFragment newInstance() {
        return new RatesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new RatesAdapter(getContext().getApplicationContext());
        adapter.setRateChangeListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rates_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RatesViewModel.class);
        mViewModel.getRatesLiveData().observe(this,this::onDataChanged);
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
