package com.github.bubinimara.rates.app;

import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.github.bubinimara.rates.R;
import com.github.bubinimara.rates.app.utils.EmptyTextWatcher;
import com.github.bubinimara.rates.app.utils.RatesDiffUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by davide.
 */
public class RatesAdapter extends RecyclerView.Adapter<RatesAdapter.Holder> {

    interface RateChangeListener{
        /**
         * Called whenever current rate change
         * @param rateModel the current rate change
         */
        void onRateChange(RateModel rateModel);
    }
    private final @NonNull LayoutInflater layoutInflater;
    private final @NonNull  List<RateModel> rates;
    private final @NonNull Holder.Listener holderListener;
    private RateChangeListener rateChangeListener;

    public RatesAdapter(@NonNull Context context) {
        layoutInflater = LayoutInflater.from(context);
        rates = new ArrayList<>();
        holderListener = createHolderListener();
    }

    protected Holder.Listener createHolderListener() {
        return new Holder.Listener() {
            @Override
            public void onClick(int adapterPosition) {
                moveItem(adapterPosition,0);
                notifyRateChanged();
            }

            @Override
            public void onValueChanged(String value) {
                notifyRateChanged();
            }
        };
    }

    private void notifyRateChanged() {
        if(rateChangeListener!=null){
            rateChangeListener.onRateChange(getItem(0));
        }
    }

    protected void moveItem(int fromPosition,int toPosition){
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(rates, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(rates, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.rates_list_item, parent, false);
        return new Holder(view,holderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.set(getItem(position));
    }

    protected RateModel getItem(int position){
        return rates.get(position);
    }

    @Override
    public int getItemCount() {
        return rates.size();
    }

    /**
     * Called every time the current currency is changed by the user
     * @param rateChangeListener the new exchange rate
     */
    public void setRateChangeListener(RateChangeListener rateChangeListener) {
        this.rateChangeListener = rateChangeListener;
    }

    /**
     * Update the list
     * @param rateModels the rates to show
     */
    public void updateRates(@NonNull List<RateModel> rateModels) {
        if(rates.isEmpty()){
            rates.addAll(rateModels);
            notifyItemRangeInserted(0,rates.size());
        }else{
            List<RateModel> newList = new ArrayList<>(rateModels.size());
            // update current items at the same position
            for (RateModel r: rates) {
                int i = rateModels.indexOf(r);
                if(i>=0){// if contains
                    newList.add(rateModels.get(i));
                    rateModels.remove(i);
                }
            }
            // add remain new items
            if(!rateModels.isEmpty()) {
                newList.addAll(rateModels);
            }

            DiffUtil.DiffResult diffResult =
                    DiffUtil.calculateDiff(new RatesDiffUtils(rates,newList));

            rates.clear();
            rates.addAll(newList);
            diffResult.dispatchUpdatesTo(this);
        }
    }

    static class Holder extends RecyclerView.ViewHolder {

        interface Listener{
            void onClick(int adapterPosition);
            void onValueChanged(String value);
        }

        @BindView(R.id.tv_code)
        TextView code;
        @BindView(R.id.tv_desc)
        TextView desc;
        @BindView(R.id.et_value)
        EditText value;

        @NonNull
        private final Listener listener;

        private final EmptyTextWatcher textWatcher = new EmptyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                listener.onValueChanged(s.toString());
            }

        };

        Holder(@NonNull View itemView,@NonNull Listener listener) {
            super(itemView);
            this.listener = listener;
            ButterKnife.bind(this,itemView);

        }

        void set(RateModel rate){
            code.setText(rate.getCode());
            desc.setText(rate.getDesc());

            if(getAdapterPosition()==0){
                enableTextChangeListener();
            }else{
                disableTextChangeListener();
                value.setText(rate.getValue());
            }
        }

        private void disableTextChangeListener() {
            value.removeTextChangedListener(textWatcher);
            value.setFocusable(false);
            value.setFocusableInTouchMode(false);
            value.clearFocus();
        }

        private void enableTextChangeListener(){
            value.setFocusable(true);
            value.setFocusableInTouchMode(true);
            value.addTextChangedListener(textWatcher);
            value.requestFocus();
        }

        @OnClick(R.id.ll_rates)
        void onClick(){
            if(getLayoutPosition()>0){
                listener.onClick(getAdapterPosition());
                enableTextChangeListener();
            }
        }
    }
}
