package com.example.socialization;

import android.content.Context;

import com.example.socialization.utils.CallLogUtils;

public class Biases {
    private static final String TAG = "Biases";
    public Context context;

    private Biases(Context context) {
        this.context = context;
    }

    public static Biases getInstance(Context context) {
        if (instance == null)
            instance = new Biases(context);
        return instance;
    }
}
