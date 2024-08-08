package fr.corentin.pendu.manager;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import fr.corentin.pendu.R;

/**
 * Class {@link NotificationManager}
 * @author Corentin Dupont
 * @version For project Info0306
 */

public class NotificationManager extends Application {

    public static final String CHANNEL_1_ID = "sal_1";
    public static final String CHANNEL_2_ID = "sal_2";

    @Override
    public void onCreate() {
        super.onCreate();
        this.createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "sal_1", android.app.NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("sal_1");

            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID, "sal_2", android.app.NotificationManager.IMPORTANCE_LOW);
            channel1.setDescription("sal_2");

            android.app.NotificationManager manager = this.getSystemService(android.app.NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }

    public static void sendNotification(Context context, String title, String msg)  {
       NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, NotificationManager.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(title)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManagerCompat.notify(1, notification);
    }
}
