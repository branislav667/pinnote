package bs.pinnotelight;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

public class BroadcastReceiver extends android.content.BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action!=null){
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            switch (action){
                case "PIN":
                    notificationManager.notify(1, new PinNote().buildNotification(context));
                    break;
                case "UNPIN":
                    notificationManager.cancel(1);
                    break;
            }
        }
    }
}