package gr.mobap.simera;

import androidx.annotation.Keep;
import com.google.firebase.database.IgnoreExtraProperties;
import java.io.Serializable;

@Keep
@IgnoreExtraProperties
public class CalendarData implements Serializable {

    /** Π.χ. "20/10/2025" (προαιρετικό – για εμφάνιση/diagnostics). */
    public String date;

    /** Μεσάνυχτα της ημέρας σε milliseconds (Unix epoch). */
    public long dateEpoch;

    /** Ώρα "HH:mm" (24h). */
    public String time;

    /** Αίθουσα / Τοποθεσία. */
    public String place;

    /** Τύπος/Κατηγορία συνεδρίασης. */
    public String type;

    /** Λεπτομέρειες (HTML επιτρέπεται). */
    public String details;

    /** Απαιτείται από το Firebase για deserialization. */
    public CalendarData() {
        // no-arg constructor
    }

    public CalendarData(String date,
                        long dateEpoch,
                        String time,
                        String place,
                        String type,
                        String details) {
        this.date = date;
        this.dateEpoch = dateEpoch;
        this.time = time;
        this.place = place;
        this.type = type;
        this.details = details;
    }

    /**
     * Δημιουργεί ένα μοναδικό ID για υπενθυμίσεις και stable IDs του RecyclerView.
     * Συνδυάζει dateEpoch + type ή place ώστε να είναι μοναδικό ανά συνεδρίαση.
     */
    public static String buildReminderId(CalendarData d) {
        if (d == null) return "null";
        String base = (d.type != null && !d.type.isEmpty())
                ? d.type
                : (d.place != null ? d.place : "session");
        return base + "_" + d.dateEpoch;
    }

    @Override
    public String toString() {
        return "CalendarData{" +
                "date='" + date + '\'' +
                ", dateEpoch=" + dateEpoch +
                ", time='" + time + '\'' +
                ", place='" + place + '\'' +
                ", type='" + type + '\'' +
                ", details(len)=" + (details == null ? 0 : details.length()) +
                '}';
    }
}