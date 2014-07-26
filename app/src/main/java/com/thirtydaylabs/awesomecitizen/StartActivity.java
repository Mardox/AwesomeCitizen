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
import android.view.View;
import android.widget.Button;

import com.thirtydaylabs.awesomecitizen.R;

public class StartActivity extends Activity {

    int notificationId = 0;

    Bitmap icon,restaurant,food,qr;

    Button startBT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        init();


    }


    private void init(){

        icon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_launcher);

        restaurant = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.restaurant);

        food = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.crispy_fish);

        qr = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_qr_code);


        startBT = (Button) findViewById(R.id.start_bt);

        startBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitiateRestaurantSearch();
            }
        });



    }



    public void InitiateRestaurantSearch(){

        // Build intent for restaurant action
        Intent restaurantIntent = new Intent(this, PaypalActivity.class);
        PendingIntent restaurantPendingIntent =
                PendingIntent.getActivity(this, 0, restaurantIntent, 0);

        // Create the action (Paypal)
        NotificationCompat.Action restaurantDetails =
                new NotificationCompat.Action.Builder(R.drawable.ic_paypal,
                        getString(R.string.buy_paypal),restaurantPendingIntent)
                        .build();



        // Build intent for add to wallet  content
        Intent rewardIntent = new Intent(this, RewardActivity.class);
        PendingIntent rewardPendingIntent =
                PendingIntent.getActivity(this, 0, rewardIntent, 0);

        // Create the action (add the reward)
        NotificationCompat.Action addRewardAction =
                new NotificationCompat.Action.Builder(R.drawable.ic_launcher,
                        getString(R.string.add_reward), rewardPendingIntent)
                        .build();


        // Build intent for paypal action
        Intent paypalIntent = new Intent(this, PaypalActivity.class);
        PendingIntent paypalPendingIntent =
                PendingIntent.getActivity(this, 0, paypalIntent, 0);

        // Create the action (Paypal)
        NotificationCompat.Action buyWithPayPal =
                new NotificationCompat.Action.Builder(R.drawable.ic_paypal,
                        getString(R.string.buy_paypal),paypalPendingIntent)
                        .build();




        // Create a WearableExtender to add functionality for wearables
        NotificationCompat.WearableExtender offerWearableExtender =
                new NotificationCompat.WearableExtender();
        offerWearableExtender.setHintHideIcon(true);

        // Create a big text style for the second page
        NotificationCompat.BigTextStyle offerPageStyle = new NotificationCompat.BigTextStyle();
        offerPageStyle.bigText("Crispy Fish: $5");


        // Create second page notification
        Notification offerPageNotification =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Your Today's Offer")
                        .setStyle(offerPageStyle)
                        .extend(offerWearableExtender)
                        .build();

        NotificationCompat.BigPictureStyle foodImagePageStyle = new NotificationCompat.BigPictureStyle();
        foodImagePageStyle.bigPicture(food);

        // Create a WearableExtender to add functionality for wearables
        NotificationCompat.WearableExtender foodWearableExtender =
                new NotificationCompat.WearableExtender();
        foodWearableExtender.setHintHideIcon(true)
                .setHintShowBackgroundOnly(true);

        // Create second page notification
        Notification thirdPageNotification =
                new NotificationCompat.Builder(this)
                        .setStyle(foodImagePageStyle)
                        .extend(foodWearableExtender)
                        .build();


        // Create a WearableExtender to add functionality for wearables
        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender();
        wearableExtender.setHintHideIcon(false);
        wearableExtender.setBackground(restaurant);
        wearableExtender.addPage(offerPageNotification);
        wearableExtender.addPage(thirdPageNotification);
        wearableExtender.addAction(buyWithPayPal);
        wearableExtender.addAction(addRewardAction);


        // Create a NotificationCompat.Builder to build a standard notification
        // then extend it with the WearableExtender
        Notification notif = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Awesome Place!")
                .setContentText("You've passed this awesome place 3 times but you haven't tried it yet!")
                .setContentIntent(restaurantPendingIntent)
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
        getMenuInflater().inflate(R.menu.start, menu);
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
