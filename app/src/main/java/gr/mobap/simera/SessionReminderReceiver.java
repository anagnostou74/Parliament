package gr.mobap.simera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import gr.mobap.R;

public class SessionReminderReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ID = "calendar_session_channel";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_ID = "id";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(EXTRA_TITLE);
        String id = intent.getStringExtra(EXTRA_ID);

        NotificationCompat.Builder b = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_bell_filled) // βάλε υπάρχον small icon
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(title)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat.from(context).notify(id.hashCode(), b.build());
    }
}
