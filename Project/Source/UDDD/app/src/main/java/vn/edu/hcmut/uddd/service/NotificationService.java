package vn.edu.hcmut.uddd.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v7.app.NotificationCompat;

import vn.edu.hcmut.uddd.R;
import vn.edu.hcmut.uddd.common.ConstCommon;
import vn.edu.hcmut.uddd.common.Constants;
import vn.edu.hcmut.uddd.view.ScreenNotification;

/**
 * Created by TRAN VAN HEN on 4/5/2016.
 */
public class NotificationService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int topic = intent.getExtras().getInt(Constants.PR_TOPIC_ID);
        boolean isSound = intent.getExtras().getBoolean(Constants.PR_SOUND);
        Intent noti = new Intent(context, ScreenNotification.class);
        noti.putExtra(Constants.PR_TOPIC_ID, topic);
        noti.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(ConstCommon.NOTIFICATION_TITLE);
        builder.setContentText(ConstCommon.NOTIFICATION_MESSAGE);
        builder.setSmallIcon(R.drawable.action_bar);
        if (isSound) {
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }
        builder.setAutoCancel(true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, ConstCommon.NOTIFICATION_ID, noti, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }
}
