package com.example.projectgabi.Utils;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class PieDataParceble implements Parcelable {




    protected PieDataParceble(Parcel in) {
    }

    public static final Creator<PieDataParceble> CREATOR = new Creator<PieDataParceble>() {
        @Override
        public PieDataParceble createFromParcel(Parcel in) {
            return new PieDataParceble(in);
        }

        @Override
        public PieDataParceble[] newArray(int size) {
            return new PieDataParceble[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
    }
}
