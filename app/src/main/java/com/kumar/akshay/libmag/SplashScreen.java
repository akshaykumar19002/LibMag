package com.kumar.akshay.libmag;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.Service.BooksService;
import com.kumar.akshay.libmag.Service.NotificationService;
import com.kumar.akshay.libmag.Service.RIBService;
import com.kumar.akshay.libmag.Service.RequestService;
import com.kumar.akshay.libmag.Service.StudentsService;

import java.util.Calendar;


public class SplashScreen extends AppCompatActivity {

    NetworkChangeReceiver ncr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNetworkAvailable()) {
            basics();
        }else
        {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    void basics(){
        new LibMagDBHelper(this).recreateTables();
        startService(new Intent(this, NotificationService.class));
        startService(new Intent(this, BooksService.class));
        startService(new Intent(this, StudentsService.class));
        startService(new Intent(this, RIBService.class));
        startService(new Intent(this, RequestService.class));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, NotificationService.class));
        }else
            startService(new Intent(this, NotificationService.class));

        // Set the alarm to start at approximately 2:00 p.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);

        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getService(this, 0, new Intent(this, NotificationService.class), 0);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
        startActivity(new Intent(this, MainScreen.class));
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static class NetworkUtil {
        public int TYPE_WIFI = 1;
        public int TYPE_MOBILE = 2;
        public int TYPE_NOT_CONNECTED = 0;
        public final int NETWORK_STATUS_NOT_CONNECTED=0,NETWORK_STAUS_WIFI=1,NETWORK_STATUS_MOBILE=2;

        public int getConnectivityStatus(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null != activeNetwork) {
                if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                    return TYPE_WIFI;

                if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                    return TYPE_MOBILE;
            }
            return TYPE_NOT_CONNECTED;
        }

        public int getConnectivityStatusString(Context context) {
            int conn = getConnectivityStatus(context);
            int status = 0;
            if (conn == TYPE_WIFI) {
                status = NETWORK_STAUS_WIFI;
            } else if (conn == TYPE_MOBILE) {
                status =NETWORK_STATUS_MOBILE;
            } else if (conn == TYPE_NOT_CONNECTED) {
                status = NETWORK_STATUS_NOT_CONNECTED;
            }
            return status;
        }
    }

    public static class NetworkChangeReceiver extends BroadcastReceiver {

        public NetworkChangeReceiver() {}

        @Override
        public void onReceive(final Context context, final Intent intent) {
            NetworkUtil nw = new NetworkUtil();
            int status = nw.getConnectivityStatusString(context);
            Log.e("network reciever", "Connectivity change found");
            if (!"android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                if(status==nw.NETWORK_STATUS_NOT_CONNECTED){
                   new SplashScreen().basics();
                }else{
                    Toast.makeText(context, "No Internet Connectivity", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ncr = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        this.registerReceiver(ncr, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(ncr);
    }
}
