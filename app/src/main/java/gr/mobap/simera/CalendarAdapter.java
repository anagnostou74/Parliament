package gr.mobap.simera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import gr.mobap.R;

/**
 * Adapter για το ημερολόγιο με stable IDs για να αποφεύγονται
 * RecyclerView inconsistencies όταν αλλάζουν τα δεδομένα.
 */
public class CalendarAdapter
        extends FirebaseRecyclerAdapter<CalendarData, CalendarViewHolder> {

    private final Context context;
    @NonNull private final LayoutInflater inflater;

    public interface PermissionHost {
        void requestPostNotificationsIfNeeded(@NonNull Runnable onGranted);
    }

    @NonNull private final PermissionHost permissionHost;

    public CalendarAdapter(@NonNull FirebaseRecyclerOptions<CalendarData> options,
                           @NonNull Context ctx,
                           @NonNull PermissionHost host) {
        super(options);
        this.context = ctx.getApplicationContext();
        this.inflater = LayoutInflater.from(ctx);
        this.permissionHost = host;

        // Απαραίτητο για να μη "μπερδεύεται" ο RecyclerView όταν γίνονται updates
        setHasStableIds(true);
    }

    // --- Stable ID βασισμένο στο Firebase key ή (fallback) σε dateEpoch+type
    @Override
    public long getItemId(int position) {
        try {
            String key = getRef(position).getKey();
            if (key != null) return key.hashCode();
        } catch (Exception ignore) { }
        CalendarData d = getItemOrNull(position);
        if (d == null) return RecyclerView.NO_ID;
        long h = d.dateEpoch;
        if (d.type != null) h ^= d.type.hashCode();
        return h;
    }

    private CalendarData getItemOrNull(int position) {
        try { return getItem(position); } catch (Exception ignore) { return null; }
    }

    // --- ViewHolder δημιουργία
    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_calendar, parent, false);
        return new CalendarViewHolder(v);
    }

    // --- Δέσιμο δεδομένων
    @Override
    protected void onBindViewHolder(@NonNull CalendarViewHolder holder,
                                    int position,
                                    @NonNull CalendarData data) {
        holder.bind(data);

        // Καμπανάκι: schedule/cancel ανάλογα με την τρέχουσα κατάσταση
        holder.btnReminder.setOnClickListener(v -> {
            permissionHost.requestPostNotificationsIfNeeded(() -> {
                long triggerAt = DateUtils.combineDateEpochAndTimeMillis(data.dateEpoch, data.time);
                String reminderId = buildReminderId(position, data);
                boolean scheduled = ReminderStore.isScheduled(context, reminderId);

                if (scheduled) {
                    NotificationScheduler.cancelSessionReminder(context, reminderId);
                    ReminderStore.setScheduled(context, reminderId, false);
                } else {
                    NotificationScheduler.scheduleSessionReminder(
                            context, reminderId,
                            data.type != null ? data.type : "Συνεδρίαση",
                            triggerAt
                    );
                    ReminderStore.setScheduled(context, reminderId, true);
                }
                updateBellIcon(holder, ReminderStore.isScheduled(context, reminderId));
                // μετά το scheduleSessionReminder(...)
                if (android.os.Build.VERSION.SDK_INT >= 31) {
                    android.app.AlarmManager am = (android.app.AlarmManager)
                            v.getContext().getSystemService(android.content.Context.ALARM_SERVICE);

                    if (!am.canScheduleExactAlarms()) {
                        com.google.android.material.snackbar.Snackbar.make(
                                        v,
                                        R.string.reminder_scheduled_inexact,
                                        com.google.android.material.snackbar.Snackbar.LENGTH_LONG)
                                .setDuration(10000) // 10 δευτερόλεπτα
                                .setTextMaxLines(6)
                                // Προσθέτουμε κουμπί "Ρυθμίσεις"
                                .setAction("Ρυθμίσεις", click -> {
                                    // Άνοιγμα ρυθμίσεων αδειών exact alarm
                                    NotificationScheduler.openExactAlarmSettings(v.getContext());
                                })
                                .show();
                    }
                }

            });
        });

        // αρχικό εικονίδιο
        updateBellIcon(holder, ReminderStore.isScheduled(context, buildReminderId(position, data)));
    }

    private String buildReminderId(int position, @NonNull CalendarData d) {
        String key = null;
        try { key = getRef(position).getKey(); } catch (Exception ignore) { }
        if (key != null) return key;
        return (d.type == null ? "TYPE" : d.type) + "_" + d.dateEpoch;
    }

    private void updateBellIcon(@NonNull CalendarViewHolder h, boolean scheduled) {
        h.btnReminder.setImageResource(scheduled ? R.drawable.ic_bell_filled
                : R.drawable.ic_bell);
    }
}
