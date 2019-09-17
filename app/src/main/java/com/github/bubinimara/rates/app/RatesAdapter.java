package com.github.bubinimara.rates.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.github.bubinimara.rates.R;

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

    private final @NonNull LayoutInflater layoutInflater;
    private final @NonNull  List<RateModel> rates;
    private final @NonNull Holder.Listener holderListener;

    public RatesAdapter(@NonNull Context context) {
        layoutInflater = LayoutInflater.from(context);
        rates = new ArrayList<>();
        holderListener = createHolderListener();
    }

    protected Holder.Listener createHolderListener() {
        return adapterPosition -> moveItem(adapterPosition,0);
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

    public static class RatesDiffUtils extends DiffUtil.Callback{
        private final List<RateModel> oldRates;
        private final List<RateModel> newRates;

        public RatesDiffUtils(List<RateModel> oldRates, List<RateModel> newRates) {
            this.oldRates = oldRates;
            this.newRates = newRates;
        }

        @Override
        public int getOldListSize() {
            return oldRates.size();
        }

        @Override
        public int getNewListSize() {
            return newRates.size();
        }

        private RateModel getOldItem(int oldItemPosition) {
            return oldRates.get(oldItemPosition);
        }

        private RateModel getNewItem(int newItemPosition) {
            return newRates.get(newItemPosition);
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            RateModel newItem = getNewItem(newItemPosition);
            RateModel oldItem = getOldItem(oldItemPosition);
            return newItem.isTheSameAs(oldItem);
        }


        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            RateModel newItem = getNewItem(newItemPosition);
            RateModel oldItem = getOldItem(oldItemPosition);
            return newItem.areContentsTheSame(oldItem);
        }

    }
    static class Holder extends RecyclerView.ViewHolder {
        interface Listener{
            void onClick(int adapterPosition);
        }

        @BindView(R.id.tv_code)
        TextView code;
        @BindView(R.id.tv_value)
        TextView value;

        @NonNull
        private final Listener listener;

        Holder(@NonNull View itemView,@NonNull Listener listener) {
            super(itemView);
            this.listener = listener;
            ButterKnife.bind(this,itemView);

        }

        void set(RateModel rate){
            code.setText(rate.getCode());
            value.setText(rate.getValue());
        }

        @OnClick(R.id.ll_rates)
        void onClick(){
            if(getLayoutPosition()>0){
                listener.onClick(getAdapterPosition());
            }
        }
    }
}
