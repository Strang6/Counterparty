package com.strang6.counterparty;

import android.util.Log;

/**
 * Created by Strang6 on 12.12.2017.
 */

public class Logger {
    private static final String TAG = "MyTag";

    public static void d(String msg) {
        Log.d(TAG, msg);
    }
}
