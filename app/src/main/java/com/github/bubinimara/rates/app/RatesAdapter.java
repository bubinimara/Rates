package com.github.bubinimara.rates.app;

import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.bubinimara.rates.R;
import com.github.bubinimara.rates.app.model.RateModel;
import com.github.bubinimara.rates.app.utils.EmptyTextWatcher;
import com.github.bubinimara.rates.app.utils.RatesDiffUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

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
    private final @NonNull Holder.Listener holderListener;
    // The data
    private final @NonNull List<RateModel> rates;
    // The text value
    private final @NonNull HashMap<RateModel, BehaviorSubject<String>> observableHashMap;
    private RateChangeListener rateChangeListener;

    public RatesAdapter(@NonNull Context context) {
        layoutInflater = LayoutInflater.from(context);
        rates = Collections.synchronizedList(new ArrayList<>());
        observableHashMap = new HashMap<>();
        holderListener = createHolderListener();
    }

    protected Holder.Listener createHolderListener() {
        return new Holder.Listener() {
            @Override
            public void onClick(int adapterPosition) {
                moveItemAtTheTop(adapterPosition);
                notifyRateChanged();
            }

            @Override
            public void onValueChanged(String value) {
                getItem(0).setValue(value);
                notifyRateChanged();
            }
        };
    }

    private void notifyRateChanged() {
        if(rateChangeListener!=null){
            rateChangeListener.onRateChange(getItem(0));
        }
    }

    protected void moveItemAtTheTop(int fromPosition){
            for (int i = fromPosition; i > 0; i--) {
                Collections.swap(rates, i, i - 1);
            }

        notifyItemMoved(fromPosition, 0);
        // to force redraw - ( trick for text focus )
        notifyItemChanged(0, null);
        notifyItemChanged(1, null);

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

    /**
     * Remove not used Observables
     */
    private void removeUnusedObservables() {
        //todo: not implemented yet
    }

    /**
     * Create or return the observable associated to the rate passed as parameter
     * @param rateModel the rate model
     * @return the Observable
     */
    private BehaviorSubject<String> getOrCreateObservable(RateModel rateModel) {
        BehaviorSubject<String> subject = observableHashMap.get(rateModel);
        if(subject==null){
            subject = BehaviorSubject.create();
        }
        observableHashMap.put(rateModel,subject);
        return subject;
    }


    @Override
    public void onViewAttachedToWindow(@NonNull Holder holder) {
        super.onViewAttachedToWindow(holder);
        holder.observerValue(getOrCreateObservable(getItem(holder.getAdapterPosition())));
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull Holder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.dispose();
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
     *
     */
    public void updateRates(@NonNull List<RateModel> rateModels) {
        if(rates.isEmpty()){
            rates.addAll(rateModels);
            notifyItemRangeInserted(0,rates.size());
        }else{

            List<RateModel> newList = new ArrayList<>(rateModels.size());
            // prevent not update the first item
            // NOTE
            // !? the first item should be treated as special ?!
            rateModels.remove(getItem(0));
            newList.add(getItem(0));

            // update current items at the same position
            for (RateModel r: rates) {
                int i = rateModels.indexOf(r);
                if(i>=0){// if contains
                    newList.add(rateModels.get(i));
                    getOrCreateObservable(rateModels.get(i))
                            .onNext(rateModels.get(i).getValue());
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
            removeUnusedObservables();
            diffResult.dispatchUpdatesTo(this);
        }
    }

    static class Holder extends RecyclerView.ViewHolder  {

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
        @BindView(R.id.imageView)
        ImageView imageView;

        private Disposable disposable;

        public void observerValue(Observable<String> rateValue){
            if(rateValue == null)
                return;

            disposable = rateValue.observeOn(AndroidSchedulers.mainThread())
            .subscribe(v->{
                if(getLayoutPosition()!=0)
                value.setText(v);
            });
        }

        public void dispose(){
            if(disposable!=null && !disposable.isDisposed()){
                disposable.dispose();
            }
        }

        @NonNull
        private final Listener listener;

        private final EmptyTextWatcher textWatcher = new EmptyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if(getLayoutPosition()==0)
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

            if(getLayoutPosition()==0){
                value.setText(rate.getValue());
                enableTextChangeListener();
            }else{
                disableTextChangeListener();
                value.setText(rate.getValue());
            }

            Glide.with(itemView).load(rate.getIcon())
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);
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
