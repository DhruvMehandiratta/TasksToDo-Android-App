package com.example.taskstodo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import static android.app.Notification.DEFAULT_SOUND;

/**
 * Created by Dhruv on 16-03-2017.
 */
public class AlarmReceiver extends BroadcastReceiver{
   static int i = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context,"Alarm Received",Toast.LENGTH_SHORT).show();
        Log.i("Alarm Received","Alarm Received");
        String title = intent.getStringExtra("Title");
        String description = intent.getStringExtra("Description");
        Log.d("title",title);
        Log.d("descriptionAlarm",description);

        if(title.length() == 0 ){
            title = "Reminder!";
        }
        if(description.length() == 0){
            description = "Touch to edit!";
        }
        //creating custom view for notification
      //  RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget);
      //  remoteViews.setTextViewText(R.id.notification_text,"Alarm"+i);
        //TextView notificationText = (TextView) remoteViews.findViewById(R.id.notification_text);

        //creating notification
        Intent resultIntent = new Intent(context,EditListItem.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,i,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(" " + description)
        //        .setContent(remoteViews)
                .setContentIntent(resultPendingIntent)
                .setDefaults(DEFAULT_SOUND);

//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
  //      stackBuilder.addParentStack(AddTask.class);

    //remoteViews.setOnClickPendingIntent(R.id.notification_radio_button,resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(i,mBuilder.build());
        i++;

        //TODO custom ringtones
    }
}
