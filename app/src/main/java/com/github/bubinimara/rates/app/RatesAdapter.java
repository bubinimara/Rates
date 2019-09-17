package com.github.bubinimara.rates.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.github.bubinimara.rates.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by davide.
 */
public class RatesAdapter extends RecyclerView.Adapter<RatesAdapter.Holder> {

    private LayoutInflater layoutInflater;
    private final @NonNull  List<RateModel> rates;

    public RatesAdapter(@NonNull Context context) {
        layoutInflater = LayoutInflater.from(context);
        rates = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.rates_list_item, parent, false);
        return new Holder(view);
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
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return rates.size();
                }

                @Override
                public int getNewListSize() {
                    return rateModels.size();
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
                    return newItem.equals(oldItem);
                }

                private RateModel getOldItem(int oldItemPosition) {
                    return rates.get(oldItemPosition);
                }

                private RateModel getNewItem(int newItemPosition) {
                    return rateModels.get(newItemPosition);
                }
            });

            rates.clear();
            rates.addAll(rateModels);
            diffResult.dispatchUpdatesTo(this);
        }
        rates.clear();
        rates.addAll(rateModels);
        notifyDataSetChanged();
    }

    static class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_code)
        TextView code;

        @BindView(R.id.tv_value)
        TextView value;

        Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        void set(RateModel rate){
            code.setText(rate.getCode());
            value.setText(rate.getValue());
        }
    }
}
