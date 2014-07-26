package com.thirtydaylabs.awesomecitizen;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.thirtydaylabs.awesomecitizen.R;

import org.json.JSONException;

import java.math.BigDecimal;

public class PaypalActivity extends Activity {


    int notificationId = 1;

    Bitmap icon,restaurant,food,qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);

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


        paypalInitPayment();



    }



    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

            .clientId("AcJBSRAIdPsTuQj79ykpjN4WwA6ceFfsVsrGXexxRZFckiYVh953Dv5360fO");


    public void paypalInitPayment(){
        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);



        PayPalPayment payment = new PayPalPayment(new BigDecimal("1.75"), "USD", "hipster jeans",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent paymentIntent = new Intent(this, PaymentActivity.class);

        paymentIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(paymentIntent, 0);
    }




    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.
                    PaymentConfirmationNotification();

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    private void PaymentConfirmationNotification() {

        int notificationId = 1;
        // Build intent for notification content
        Intent paypalIntent = new Intent(this, PaypalActivity.class);
        PendingIntent paypalPendingIntent =
                PendingIntent.getActivity(this, 0, paypalIntent, 0);


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
        wearableExtender.addPage(qrPageNotification);


        // Create a NotificationCompat.Builder to build a standard notification
        // then extend it with the WearableExtender
        Notification notif = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Good choice!")
                .setContentText("Your meal will be ready in 6 Minutes...")
                .setContentIntent(paypalPendingIntent)
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
    protected void onStop() {
        stopService(new Intent(this, PayPalService.class));
        super.onStop();
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
