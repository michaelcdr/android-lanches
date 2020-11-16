package br.ucs.androidlanches.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class NetworkHelper
{
    public static boolean temInternet(Context context)
    {
        ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected() || connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected())
        {
            Log.i("LOG_INTERNET", "CONECTADO ");
            return true;
        }
        else{
            Log.i("LOG_INTERNET", "NOM CONECTADO ");
            return false;
        }
    }
}
