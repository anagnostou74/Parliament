package gr.mobap.simera;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

public final class NotificationScheduler {

    private NotificationScheduler() {}

    public static void scheduleSessionReminder(Context ctx, String id, String title, long triggerAt) {
        Intent fire = new Intent(ctx, SessionReminderReceiver.class)
                .putExtra(SessionReminderReceiver.EXTRA_ID, id)
                .putExtra(SessionReminderReceiver.EXTRA_TITLE, title);

        PendingIntent firePi = PendingIntent.getBroadcast(
                ctx, id.hashCode(), fire,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

        boolean canExact = canUseExactAlarms(am);
        Log.d("BELL", "schedule id=" + id + " at=" + triggerAt + " canExact=" + canExact);

        // 1) Αν επιτρέπεται exact → setExactAndAllowWhileIdle
        if (canExact) {
            if (Build.VERSION.SDK_INT >= 31) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, firePi);
            } else if (Build.VERSION.SDK_INT >= 23) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, firePi);
            } else {
                am.setExact(AlarmManager.RTC_WAKEUP, triggerAt, firePi);
            }
            return;
        }

        // 2) ΑΛΛΙΩΣ: ασφαλές fallback ΧΩΡΙΣ exact (δεν κρασάρει)
        //    Χρησιμοποίησε ένα μικρό window για αξιοπιστία.
        if (Build.VERSION.SDK_INT >= 19) {
            long window = 2 * 60 * 1000L; // 2 λεπτά παράθυρο
            am.setWindow(AlarmManager.RTC_WAKEUP, triggerAt, window, firePi);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, triggerAt, firePi);
        }
    }

    public static void cancelSessionReminder(Context ctx, String id) {
        Intent fire = new Intent(ctx, SessionReminderReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(
                ctx, id.hashCode(), fire,
                PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE);
        if (pi != null) {
            AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
            am.cancel(pi);
            Log.d("BELL", "cancelled id=" + id);
        }
    }

    /**
     * Επιστρέφει αν η εφαρμογή ΜΠΟΡΕΙ να προγραμματίζει exact alarms.
     */
    private static boolean canUseExactAlarms(AlarmManager am) {
        if (Build.VERSION.SDK_INT >= 31) {
            return am.canScheduleExactAlarms();
        }
        return true; // σε παλιότερα APIs επιτρέπεται
    }

    /**
     * (Προαιρετικό) Άνοιγμα ρυθμίσεων για ενεργοποίηση exact alarms.
     */
    public static void openExactAlarmSettings(Context ctx) {
        if (Build.VERSION.SDK_INT >= 31) {
            try {
                Intent i = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                i.setData(android.net.Uri.parse("package:" + ctx.getPackageName()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(i);
            } catch (Exception ignore) {}
        }
    }
}
