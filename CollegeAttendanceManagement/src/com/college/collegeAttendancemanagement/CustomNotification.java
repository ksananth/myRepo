package com.college.collegeAttendancemanagement;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class CustomNotification {
	
	public final static String sample_url = "https://upload.wikimedia.org/wikipedia/en/2/2a/Annamalai_University_logo.png";

	public final static int NORMAL = 0x00;
	public final static int BIG_TEXT_STYLE = 0x01;
	public final static int BIG_PICTURE_STYLE = 0x02;
	public final static int INBOX_STYLE = 0x03;
	public final static int CUSTOM_VIEW = 0x04;
    int i=1;
    Context ctx;
    String title;
    String message;
    String summary;
    String url;

    private static NotificationManager mNotificationManager;
    
    public CustomNotification(Context ctx,String title,String message,String summary,String url){
    	this.ctx=ctx;
    	this.title=title;
    	this.message=message;
    	this.summary=summary;
    	this.url=url;
    	 mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
    }
    
    public void notify(int style){
    	 Notification noti = new Notification();

         switch (style)
         {
             case NORMAL:
                 noti = setNormalNotification();
                 break;

             case BIG_TEXT_STYLE:
                 noti = setBigTextStyleNotification();
                 break;

             case BIG_PICTURE_STYLE:
                 noti = setBigPictureStyleNotification();
                 break;

             case INBOX_STYLE:
                 noti = setInboxStyleNotification();
                 break;

             case CUSTOM_VIEW:
                 noti = setCustomViewNotification();
                 break;

         }

         noti.defaults |= Notification.DEFAULT_LIGHTS;
         noti.defaults |= Notification.DEFAULT_VIBRATE;
         noti.defaults |= Notification.DEFAULT_SOUND;

         noti.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
         i=i+1;
         mNotificationManager.notify(i, noti);
    }
    
    /**
     * Normal Notification
     *
     * @return Notification
     * @see CreateNotification
     */
    private Notification setNormalNotification() {
        Bitmap remote_picture = null;

        try {
            remote_picture = BitmapFactory.decodeStream((InputStream) new URL(sample_url).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Setup an explicit intent for an ResultActivity to receive.
        Intent resultIntent = new Intent(ctx, HomeScreenActivity.class);

        // TaskStackBuilder ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);

        // Adds the back stack for the Intent (but not the Intent itself).
        stackBuilder.addParentStack(HomeScreenActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap icon = BitmapFactory.decodeResource(ctx.getResources(),
                R.drawable.annamalai_logo);
        return new NotificationCompat.Builder(ctx)
                //.setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setLargeIcon(icon)
                .setContentIntent(resultPendingIntent)
                //.addAction(R.drawable.ic_launcher, "One", resultPendingIntent)
               // .addAction(R.drawable.ic_launcher, "Two", resultPendingIntent)
                //.addAction(R.drawable.ic_launcher, "Three", resultPendingIntent)
                .setContentTitle("Normal Notification")
                .setContentText("This is an example of a Normal Style.").build();
    }

    /**
     * Big Text Style Notification
     *
     * @return Notification
     * @see CreateNotification
     */
    private Notification setBigTextStyleNotification() {
        Bitmap remote_picture = null;

        // Create the style object with BigTextStyle subclass.
        NotificationCompat.BigTextStyle notiStyle = new NotificationCompat.BigTextStyle();
        notiStyle.setBigContentTitle(title);
        notiStyle.setSummaryText(summary);

        try {
            remote_picture = BitmapFactory.decodeStream((InputStream) new URL(sample_url).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add the big text to the style.
        CharSequence bigText = message;
        notiStyle.bigText(bigText);

        // Creates an explicit intent for an ResultActivity to receive.
        Intent resultIntent = new Intent(ctx, HomeScreenActivity.class);

        // This ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);

        // Adds the back stack for the Intent (but not the Intent itself).
        stackBuilder.addParentStack(HomeScreenActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(resultIntent);
        i=i+1;
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(i, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.annamalai_logo)
                .setAutoCancel(true)
                .setLargeIcon(remote_picture)
                .setContentIntent(resultPendingIntent)
                //.addAction(R.drawable.ic_launcher, "One", resultPendingIntent)
                //.addAction(R.drawable.ic_launcher, "Two", resultPendingIntent)
                //.addAction(R.drawable.ic_launcher, "Three", resultPendingIntent)
                .setContentTitle(title)
                .setContentText(summary)
                .setStyle(notiStyle).build();
    }

    /**
     * Big Picture Style Notification
     *
     * @return Notification
     * @see CreateNotification
     */
    private Notification setBigPictureStyleNotification() {
        Bitmap remote_picture = null;

        // Create the style object with BigPictureStyle subclass.
        NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
        notiStyle.setBigContentTitle("Big Picture Expanded");
        notiStyle.setSummaryText("Nice big picture.");

        try {
            remote_picture = BitmapFactory.decodeStream((InputStream) new URL(sample_url).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add the big picture to the style.
        notiStyle.bigPicture(remote_picture);

        // Creates an explicit intent for an ResultActivity to receive.
        Intent resultIntent = new Intent(ctx, HomeScreenActivity.class);

        // This ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);

        // Adds the back stack for the Intent (but not the Intent itself).
        stackBuilder.addParentStack(HomeScreenActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setLargeIcon(remote_picture)
                .setContentIntent(resultPendingIntent)
               // .addAction(R.drawable.ic_launcher, "One", resultPendingIntent)
               // .addAction(R.drawable.ic_launcher, "Two", resultPendingIntent)
                //.addAction(R.drawable.ic_launcher, "Three", resultPendingIntent)
                .setContentTitle("Big Picture Normal")
                .setContentText("This is an example of a Big Picture Style.")
                .setStyle(notiStyle).build();
    }

    /**
     * Inbox Style Notification
     *
     * @return Notification
     * @see CreateNotification
     */
    private Notification setInboxStyleNotification() {
        Bitmap remote_picture = null;

        // Create the style object with InboxStyle subclass.
        NotificationCompat.InboxStyle notiStyle = new NotificationCompat.InboxStyle();
        notiStyle.setBigContentTitle("Inbox Style Expanded");

        // Add the multiple lines to the style.
        // This is strictly for providing an example of multiple lines.
        for (int i=0; i < 5; i++) {
            notiStyle.addLine("(" + i + " of 6) Line one here.");
        }
        notiStyle.setSummaryText("+2 more Line Samples");

        try {
            remote_picture = BitmapFactory.decodeStream((InputStream) new URL(sample_url).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Creates an explicit intent for an ResultActivity to receive.
        Intent resultIntent = new Intent(ctx, HomeScreenActivity.class);

        // This ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);

        // Adds the back stack for the Intent (but not the Intent itself).
        stackBuilder.addParentStack(HomeScreenActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setLargeIcon(remote_picture)
                .setContentIntent(resultPendingIntent)
                .addAction(R.drawable.ic_launcher, "One", resultPendingIntent)
                .addAction(R.drawable.ic_launcher, "Two", resultPendingIntent)
                .addAction(R.drawable.ic_launcher, "Three", resultPendingIntent)
                .setContentTitle("Inbox Style Normal")
                .setContentText("This is an example of a Inbox Style.")
                .setStyle(notiStyle).build();
    }

    /**
     * Custom View Notification
     *
     * @return Notification
     * @see CreateNotification
     */
    private Notification setCustomViewNotification() {

        // Creates an explicit intent for an ResultActivity to receive.
        Intent resultIntent = new Intent(ctx, HomeScreenActivity.class);

        // This ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(HomeScreenActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create remote view and set bigContentView.
        RemoteViews expandedView = new RemoteViews(ctx.getPackageName(), R.layout.notification_custom_remote);
        expandedView.setTextViewText(R.id.text_view, "Neat logo!");

        Notification notification = new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle("Custom View").build();

        notification.bigContentView = expandedView;

        return notification;
    }
}
