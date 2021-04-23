package com.agx.jetpackmvvm.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

    /**
     * check NetworkAvailable
     *
     * @param context context
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (null == manager) {
            return false;
        }
        NetworkInfo info = manager.getActiveNetworkInfo();
        return null != info && info.isAvailable();
    }
}
