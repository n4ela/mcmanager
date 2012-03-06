package mcmanager.android.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import mcmanager.android.R;

public class NotificationHelper {
    private Context mContext;
//    private int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private static int MOOD_NOTIFICATIONS = R.layout.notification_update_db;
    private int max;
    private int count;
//    private PendingIntent mContentIntent;
//    private CharSequence mContentTitle;

    public NotificationHelper(Context context) {
        mContext = context;
    }

    public void createNotification(int max) {
        this.max = max;
        count = 0;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotification = new Notification(android.R.drawable.stat_notify_sync,
//                                         "Обновление базы данных",
//                                         System.currentTimeMillis());
//        mNotification.flags = mNotification.flags | Notification.FLAG_ONGOING_EVENT;
//        mNotification.contentView = new RemoteViews(mContext.getPackageName(), R.layout.notification_update_db);
//        mNotification.contentView.setProgressBar(count++, max, 0, false);
//        mNotification.contentView.setTextViewText(R.id.notificationText, "ТЕСТ");
//
//        Intent notificationIntent = new Intent();
//        mContentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
//        mContentTitle = "Full title of the notification in the pull down";
//        CharSequence contentText = "0% complete";
//        mNotification.setLatestEventInfo(mContext, mContentTitle, "Обновление базы данных", mContentIntent);
//        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
//

    }

    public void progressUpdate() {
        Notification notif = new Notification();
        notif.contentIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, NotificationHelper.class), 0);
        RemoteViews contentView = new RemoteViews(mContext.getPackageName(), MOOD_NOTIFICATIONS);
        contentView.setProgressBar(count++, max, 0, false);
        contentView.setTextViewText(R.id.notificationText, "ТЕСТ");
        notif.contentView = contentView;
        mNotificationManager.notify(MOOD_NOTIFICATIONS, notif);
    }

    public void completed()    {
        mNotificationManager.cancel(MOOD_NOTIFICATIONS);
    }
}