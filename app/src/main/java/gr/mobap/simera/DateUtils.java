package gr.mobap.simera;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class DateUtils {
    static final Locale LOCALE_GR = new Locale("el", "GR");
    static final ZoneId ZONE_ATHENS = ZoneId.of("Europe/Athens");

    private DateUtils() {}

    /** Πλήρης ημερομηνία: "Πέμπτη, 16 Οκτωβρίου 2025" */
    public static String formatFull(long epochMillis) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", LOCALE_GR)
                .withZone(ZONE_ATHENS);
        return fmt.format(Instant.ofEpochMilli(epochMillis));
    }

    /** Fallback parser για μερικά κοινά formats */
    public static long parseToEpochOrZero(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) return 0L;
        String s = dateString.trim();
        try { // yyyy-MM-dd
            LocalDate d = LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return d.atStartOfDay(ZONE_ATHENS).toInstant().toEpochMilli();
        } catch (Exception ignore) {}
        try { // dd/MM/yyyy
            LocalDate d = LocalDate.parse(s, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return d.atStartOfDay(ZONE_ATHENS).toInstant().toEpochMilli();
        } catch (Exception ignore) {}
        try { // d/M/yyyy
            LocalDate d = LocalDate.parse(s, DateTimeFormatter.ofPattern("d/M/yyyy"));
            return d.atStartOfDay(ZONE_ATHENS).toInstant().toEpochMilli();
        } catch (Exception ignore) {}
        return 0L;
    }

    /** Συνδυασμός ημερομηνίας (epoch) + ώρας "HH:mm" σε epoch millis */
    public static long combineDateEpochAndTimeMillis(long dateEpoch, String timeHHmm) {
        if (dateEpoch <= 0) return 0L;
        LocalDate date = Instant.ofEpochMilli(dateEpoch).atZone(ZONE_ATHENS).toLocalDate();
        int h = 0, m = 0;
        if (timeHHmm != null && !timeHHmm.trim().isEmpty()) {
            String[] p = timeHHmm.trim().split(":");
            try {
                h = Integer.parseInt(p[0]);
                if (p.length > 1) m = Integer.parseInt(p[1]);
            } catch (Exception ignore) {}
        }
        return date.atTime(h, m).atZone(ZONE_ATHENS).toInstant().toEpochMilli();
    }

    /** με 1′ grace ώστε να μη θεωρείται “παρελθόν” στο όριο */
    public static boolean isInFuture(long triggerAtMillis) {
        return triggerAtMillis >= (System.currentTimeMillis() + 60_000L);
    }
}
