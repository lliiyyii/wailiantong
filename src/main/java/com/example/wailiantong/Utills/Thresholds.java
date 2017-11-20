package com.example.wailiantong.Utills;

import com.google.gson.annotations.SerializedName;

public class Thresholds {
    @SerializedName("1e-3")
    float thousandth;
    @SerializedName("1e-4")
    float tenThouand;
    @SerializedName("1e-5")
    float hundredThouand;
    @SerializedName("1e-6")
    float thousandth2;

    public float getThousandth() {
        return thousandth;
    }

    public void setThousandth(float thousandth) {
        this.thousandth = thousandth;
    }

    public float getThousandth2() {
        return thousandth2;
    }

    public void setThousandth2(float thousandth2) {
        this.thousandth2 = thousandth2;
    }

    public float getTenThouand() {
        return tenThouand;
    }

    public void setTenThouand(float tenThouand) {
        this.tenThouand = tenThouand;
    }

    public float getHundredThouand() {
        return hundredThouand;
    }

    public void setHundredThouand(float hundredThouand) {
        this.hundredThouand = hundredThouand;
    }
}
