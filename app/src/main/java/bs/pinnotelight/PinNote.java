package bs.pinnotelight;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;


public class PinNote {

    public int pendingIntentFlags(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        } else {
            return PendingIntent.FLAG_UPDATE_CURRENT;
        }
    }

    public PendingIntent editPendingIntent(Context context){
        return PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), pendingIntentFlags());
    }

    public PendingIntent closePendingIntent(Context context){
        return PendingIntent.getBroadcast(context, 0, new Intent(context, BroadcastReceiver.class).setAction("UNPIN"), pendingIntentFlags());
    }

    public void createNotificationChannel(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.pin_note);
            NotificationChannel channel = new NotificationChannel("PIN", name, NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(null, null);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public Notification buildNotification(Context context){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "PIN");

        notificationBuilder
                .setSmallIcon(R.drawable.ic_baseline_push_pin_24)
                .setColor(context.getResources().getColor(R.color.yellow_800))
                .setStyle(new NotificationCompat.BigTextStyle())
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setAutoCancel(false)
                .setSound(null);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        notificationBuilder
                .setContentTitle(sharedPreferences.getString("title", ""))
                .setContentText(sharedPreferences.getString("note", ""))
                .setSubText(sharedPreferences.getString("subtext", ""));

        notificationBuilder
                .addAction(R.drawable.ic_baseline_edit_note_24, context.getString(R.string.edit), editPendingIntent(context))
                .addAction(R.drawable.ic_baseline_close_24, context.getString(R.string.close), closePendingIntent(context));

        return notificationBuilder.build();
    }

}
