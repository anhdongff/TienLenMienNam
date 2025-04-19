package com.gamebai.tienlenmiennam.hotro;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

public class Internet {
    public static boolean coKetNoi(Context context){
        ConnectivityManager connectivityManager= (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager==null){
            return false;
        }
        Network network=connectivityManager.getActiveNetwork();
        if(network==null) return false;
        NetworkCapabilities networkCapabilities=connectivityManager.getNetworkCapabilities(network);
        return networkCapabilities!=null&&networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }
}
