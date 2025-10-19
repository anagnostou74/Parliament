package gr.mobap.simera;

import android.content.Context;
import android.content.SharedPreferences;

final class ReminderStore {
    private static final String PREF = "reminders_state";

    static boolean isScheduled(Context ctx, String id) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getBoolean(id, false);
    }

    static void setScheduled(Context ctx, String id, boolean scheduled) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putBoolean(id, scheduled).apply();
    }
}
