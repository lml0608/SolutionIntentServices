package com.example.android.background.utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Action;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.example.android.background.MainActivity;
import com.example.android.background.R;
import com.example.android.background.sync.ReminderTasks;
import com.example.android.background.sync.WaterReminderIntentService;

/**
 * Created by lfs-ios on 2017/5/3.
 */

public class NotificationUtils {

    private static final int WATER_PEMINDER_NOTIFICATION_ID = 1138;

    private static final int WATER_PEMINDER_PENDING_INTENT_ID = 3417;

    private static final int ACTION_DRINK_PENDING_INTENT_ID = 1;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 14;


    public static void clearAllNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancelAll();
    }


    /**
     * 定义通知
     * @param context
     */
    public static void remindUserBecauseCharging(Context context) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_drink_notification)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.charging_reminder_notification_title))
                .setContentText(context.getString(R.string.charging_reminder_notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.charging_reminder_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                //添加2个action
                .addAction(drinkWaterAction(context))
                .addAction(ignoreReminderAction(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = notificationBuilder.build();

        //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(WATER_PEMINDER_NOTIFICATION_ID, notification);
    }

    /**
     * Action
     * @param context
     * @return
     */
    private static Action ignoreReminderAction(Context context) {

        Intent ignoreReminderIntent = new Intent(context, WaterReminderIntentService.class);
        ignoreReminderIntent.setAction(ReminderTasks.ACTION_DISMISS_NOTIFICATION);

        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreReminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Action ignoreReminderAction = new Action(
                R.drawable.ic_cancel_black_24px,
                "No, thanks.",
                ignoreReminderPendingIntent);
        return ignoreReminderAction;

    }

    private static Action drinkWaterAction(Context context) {

        // COMPLETED (12) Create an Intent to launch WaterReminderIntentService
        Intent incrementWaterCountIntent = new Intent(context, WaterReminderIntentService.class);
        // COMPLETED (13) Set the action of the intent to designate you want to increment the water count
        incrementWaterCountIntent.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);
        // COMPLETED (14) Create a PendingIntent from the intent to launch WaterReminderIntentService
        PendingIntent incrementWaterPendingIntent = PendingIntent.getService(
                context,
                ACTION_DRINK_PENDING_INTENT_ID,
                incrementWaterCountIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        // COMPLETED (15) Create an Action for the user to tell us they've had a glass of water
        Action drinkWaterAction = new Action(R.drawable.ic_local_drink_black_24px,
                "I did it!",
                incrementWaterPendingIntent);
        // COMPLETED (16) Return the action
        return drinkWaterAction;
        
    }

    /**
     * 获取PendingIntent对象
     * @param context
     * @return
     */
    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);

        return PendingIntent.getActivity(
                context,
                WATER_PEMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 从资源获取Bitmap
     * @param context
     * @return
     */
    private static Bitmap largeIcon(Context context) {

        Resources res = context.getResources();

        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_local_drink_black_24px);

        return largeIcon;
    }
}
