package com.lyeng.coffeebarista;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

public class NotificationJobService extends JobService {
    public CoffeeDao coffeDao;
    public Coffee c;
    NotificationManager mNotifyManager;
    public static final String PRIMARY_CHANNEL_ID = "notify-coffee-channel";
    public static final int NOTIFICATION_ID = 22;

    public NotificationJobService() {
        CoffeeDatabase coffeedb = CoffeeDatabase.getInstance(this);
        coffeDao = coffeedb.coffeeDao();
        c = getRandomCoffee(coffeDao);
    }

    private Coffee getRandomCoffee(CoffeeDao pCoffeDao) {
        new coffeeInfoAsyncTask(pCoffeDao).execute();
        return c;
    }

    private class coffeeInfoAsyncTask extends AsyncTask<Void, Void, Coffee>{
        private CoffeeDao coffDao;
        private coffeeInfoAsyncTask(CoffeeDao coffDao) {
            this.coffDao = coffDao;
        }
        @Override
        protected void onPostExecute(Coffee pCoffee) {
            c = pCoffee;
            super.onPostExecute(pCoffee);

        }

        @Override
        protected Coffee doInBackground(Void... pVoids) {
            return coffDao.getRandomCoffeeObject();
        }
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        CharSequence name = "Your Daily coffee alert";
        String coffeeName = c.getCoffeeName();

        createNotificationChannel();

        //Set up the notification content intent to launch the app when clicked
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (this, NOTIFICATION_ID, new Intent(this, MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_drink)
                .setContentTitle(name)
                .setContentText("Today, you should try ..." + coffeeName)
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        mNotifyManager.notify(NOTIFICATION_ID, builder.build());

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }

    public void createNotificationChannel() {
        // Create a notification manager object.
        mNotifyManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID, "Coffee notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Notifies every 5 secs to drink coffee");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }
}
