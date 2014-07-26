package com.thirtydaylabs.awesomecitizen;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.Menu;
import android.view.MenuItem;
import com.thirtydaylabs.awesomecitizen.R;

public class PaypalActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);

        NotificationManagerCompat.from(this).cancel(0);


        Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_launcher);

        Bitmap restaurant = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.restaurant);

        Bitmap food = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.crispy_fish);

        Bitmap qr = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_qr_code);

        int notificationId = 1;
        // Build intent for notification content
        Intent viewIntent = new Intent(this, RewardActivity.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);



        // Create builder for the main notification
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setLargeIcon(restaurant)
                        .setContentTitle("Good choice!")
                        .setContentText("Your meal will be ready in 6 Minutes...")
                        .setContentIntent(viewPendingIntent)
                        .extend(new NotificationCompat.WearableExtender().setBackground(restaurant));



        NotificationCompat.BigPictureStyle thirdPageStyle = new NotificationCompat.BigPictureStyle();
        thirdPageStyle.bigLargeIcon(qr);
        thirdPageStyle.bigPicture(qr);


        // Build an intent for an action to view a map
        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        PendingIntent mapPendingIntent =
                PendingIntent.getActivity(this, 0, mapIntent, 0);

        // Create second page notification
        Notification thirdPageNotification =
                new NotificationCompat.Builder(this)
                        .setStyle(thirdPageStyle)
                        .build();

        // Add second page with wearable extender and extend the main notification
        Notification twoPageNotification =
                new NotificationCompat.WearableExtender()
                        .addPage(thirdPageNotification)
                        .extend(notificationBuilder)
                        .build();


        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);


        // Issue the notification
        notificationManager =
                NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, twoPageNotification);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.paypal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
