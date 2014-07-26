package com.thirtydaylabs.awesomecitizen;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class RewardActivity extends ActionBarActivity {


    int notificationId = 1;

    Bitmap icon,restaurant,food,qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }


        //Cancel the previous notification
        NotificationManagerCompat.from(this).cancel(0);

        icon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_launcher);

        restaurant = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.restaurant);

        food = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.crispy_fish);

        qr = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_qr_code);

        int notificationId = 1;
        // Build intent for notification content
        Intent rewardIntent = new Intent(this, RewardActivity.class);
        PendingIntent rewardPendingIntent =
                PendingIntent.getActivity(this, 0, rewardIntent, 0);


        NotificationCompat.BigPictureStyle thirdPageStyle = new NotificationCompat.BigPictureStyle();
        thirdPageStyle.bigLargeIcon(qr);
        thirdPageStyle.bigPicture(qr);


        NotificationCompat.BigPictureStyle qrImagePageStyle = new NotificationCompat.BigPictureStyle();
        qrImagePageStyle.bigPicture(qr);

        // Create a WearableExtender to add functionality for wearables
        NotificationCompat.WearableExtender foodWearableExtender =
                new NotificationCompat.WearableExtender();
        foodWearableExtender.setHintHideIcon(true)
                .setHintShowBackgroundOnly(true);

        // Create second page notification
        Notification qrPageNotification =
                new NotificationCompat.Builder(this)
                        .setStyle(qrImagePageStyle)
                        .extend(foodWearableExtender)
                        .build();


        // Create a WearableExtender to add functionality for wearables
        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender();
        wearableExtender.setHintHideIcon(false);
        wearableExtender.setBackground(food);


        // Create a NotificationCompat.Builder to build a standard notification
        // then extend it with the WearableExtender
        Notification notif = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Offer Added")
                .setContentText("You have 6 hours to redeem your 5 time passing offer!")
                .setContentIntent(rewardPendingIntent)
                .extend(wearableExtender)
                .build();

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager;

        // Issue the notification
        notificationManager =
                NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, notif);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reward, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_reward, container, false);
            return rootView;
        }
    }
}
