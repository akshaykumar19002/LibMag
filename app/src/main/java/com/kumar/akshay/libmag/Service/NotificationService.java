package com.kumar.akshay.libmag.Service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.R;
import com.kumar.akshay.libmag.Student.StudentActivity;
import com.kumar.akshay.libmag.ObjectClasses.BookMessage;
import com.kumar.akshay.libmag.librarian.BooksAdapter;
import com.kumar.akshay.libmag.ObjectClasses.UsersObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NotificationService extends IntentService {

    public static String TAG = "NotificationService";
    FirebaseUser firebaseUser;
    String email;
    int i = 0;
    LibMagDBHelper libMagDB;
    BookMessage bookMessage;
    UsersObject usersObject;
    String issued_books[];
    Calendar calendar;
    NotificationManager notificationManager = null;

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            // The id of the channel.
            String id = "my_channel_01";
            // The user-visible name of the channel.
            CharSequence name = "LibMag";
            // The user-visible description of the channel.
            String description = "Creating a notification for the books to be returned";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher_round)
                            .setContentTitle("LibMag")
                            .setContentText("Checking the books to ber returned...")
                            .setPriority(Notification.PRIORITY_HIGH);
            startForeground(1996, mBuilder.build());
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
        libMagDB = new LibMagDBHelper(getApplicationContext());
        calendar = GregorianCalendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String today = format.format(calendar.getTime());
        Date today_date = null;
        try {
            today_date = format.parse(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Log.v(TAG, "Got user");
            email = firebaseUser.getEmail();
            usersObject = libMagDB.getAStudentUsinEmail(email);
            if (usersObject != null)
                if (usersObject.getType() != 0) {
                    issued_books = usersObject.getIssuedBooks().split(",");
                    for (String book_id : issued_books) {
                        bookMessage = libMagDB.getABook(book_id);
                        String return_ = BooksAdapter.getReturnDate(bookMessage.getBookIssueDate(), bookMessage.getBookIssuedTo());
                        Date return_date = null;
                        try {
                            return_date = format.parse(return_);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        int compared = return_date.compareTo(today_date);
                        if (compared == -1) {
                            showNotification();
                        }
                    }
                }
        } else
            Log.v(TAG, "Haven't got one");
    }

    private void showNotification() {
        Log.v(TAG, "Notification created");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentText("Return this book today")
                .setContentText(bookMessage.getBookId() + " : " + bookMessage.getBookName());
        Intent intent = new Intent(this, StudentActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, i, intent, i);
        builder.setContentIntent(pendingIntent);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(i, builder.build());
        i++;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // The id of the channel.
            String id = "my_channel_01";
            mNotificationManager.deleteNotificationChannel(id);
        }
    }
}
