package com.teamhub.utd.hub;

        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.v4.app.NotificationCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;

public class NotificationActivity extends AppCompatActivity {
    NotificationCompat.Builder mBuilder;
    int mNotificationId = 001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void sendNotification(View view) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

//Create the intent that’ll fire when the user taps the notification//
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.androidauthority.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.icon);
        mBuilder.setContentTitle("Remote Bluetooth ");
        mBuilder.setContentText("Battery level below 20%!");

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());
    }

}