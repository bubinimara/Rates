package com.github.bubinimara.rates.app;

import java.util.Objects;

/**
 * Created by davide.
 */
public class RateModel {
    private String code;
    private String desc;
    private String value;
    private String icon;

    public RateModel(String code, String desc, String value, String icon) {
        this.code = code;
        this.desc = desc;
        this.value = value;
        this.icon = icon;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isTheSameAs(RateModel oldItem) {
        return oldItem!=null && oldItem.code.equalsIgnoreCase(code);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RateModel)) return false;
        RateModel rateModel = (RateModel) o;
        return code.equals(rateModel.code) &&
                Objects.equals(desc, rateModel.desc) &&
                value.equals(rateModel.value) &&
                Objects.equals(icon, rateModel.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, desc, value, icon);
    }
}
