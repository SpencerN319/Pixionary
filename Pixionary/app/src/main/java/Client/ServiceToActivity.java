package Client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by fastn on 2/8/2018.
 */

public class ServiceToActivity extends BroadcastReceiver {

    private String activityName;
    private String data;

    public ServiceToActivity(String activityName) {
        this.activityName = activityName;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle notificationData = intent.getExtras();
        data = notificationData.getString(activityName);
        Log.i("ServiceToActivityorVV", data);
    }

    public String getData() { return data; }
}

