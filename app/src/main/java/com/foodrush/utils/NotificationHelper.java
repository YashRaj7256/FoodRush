package com.foodrush.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.foodrush.R;


/**
 * Utility class to show local notifications.
 */
public class NotificationHelper {

    private static final String CHANNEL_ID = "foodrush_orders";
    private static final String CHANNEL_NAME = "Order Notifications";
    private static int notificationId = 1;

    /**
     * Creates notification channel (Android 8+)
     */
    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("FoodRush Notifications");

            NotificationManager manager =
                    context.getSystemService(NotificationManager.class);

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * 🔔 Order placed notification
     */
    public static void showOrderPlacedNotification(Context context,
                                                   String orderId,
                                                   double total) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("Order Placed! 🎉")
                        .setContentText("Order #" + orderId + " confirmed! ₹" + String.format("%.0f", total))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Your order #" + orderId + " has been placed successfully!\n"
                                        + "Total: ₹" + String.format("%.2f", total)
                                        + "\nDelivery in 30–45 mins 🚀"))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

        try {
            NotificationManagerCompat.from(context)
                    .notify(notificationId++, builder.build());
        } catch (SecurityException ignored) { }
    }

}
