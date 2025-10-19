package gr.mobap.simera;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.text.HtmlCompat;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import gr.mobap.R;

/**
 * ViewHolder για το item_calendar.xml
 */
public class CalendarViewHolder extends RecyclerView.ViewHolder {

    public final TextView typeTxtView;
    public final TextView placeTxtView;
    public final TextView dateTxtView;
    public final TextView timeTxtView;
    public final TextView detailsTxtView;
    public final ImageView cal_imageTxtView;
    public final ImageButton btnReminder;

    public CalendarViewHolder(@NonNull View itemView) {
        super(itemView);

        typeTxtView = itemView.findViewById(R.id.typeTxtView);
        placeTxtView = itemView.findViewById(R.id.placeTxtView);
        dateTxtView = itemView.findViewById(R.id.dateTxtView);
        timeTxtView = itemView.findViewById(R.id.timeTxtView);
        detailsTxtView = itemView.findViewById(R.id.detailsTxtView);
        cal_imageTxtView = itemView.findViewById(R.id.cal_imageTxtView);
        btnReminder = itemView.findViewById(R.id.btnReminder);
    }

    /**
     * Σύνδεση δεδομένων CalendarData με τα Views του holder.
     */
    public void bind(@NonNull CalendarData data) {
        typeTxtView.setText(data.type != null ? data.type : "");
        placeTxtView.setText(data.place != null ? data.place : "");
        dateTxtView.setText(data.date != null ? data.date : "");
        timeTxtView.setText(data.time != null ? data.time : "");
        detailsTxtView.setText(HtmlCompat.fromHtml(data.details, HtmlCompat.FROM_HTML_MODE_LEGACY));

        // αν έχεις εικόνα ή εικονίδιο για τον τύπο συνεδρίασης, βάλε εδώ
        cal_imageTxtView.setImageResource(R.drawable.ic_splash);
    }
}
