package gr.mobap.youtube;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import gr.mobap.R;
import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.VH> {

    public interface OnVideoClick {
        void onVideoClick(YoutubeVideo v);
    }

    private final List<YoutubeVideo> data = new ArrayList<>();
    private final OnVideoClick listener;

    public YoutubeAdapter(OnVideoClick listener) {
        this.listener = listener;
    }

    public void submit(List<YoutubeVideo> items) {
        data.clear();
        data.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_youtube, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        YoutubeVideo v = data.get(pos);
        h.title.setText(v.title);
        h.date.setText(v.published);
        Glide.with(h.thumb.getContext()).load(v.thumbUrl).into(h.thumb);

        // Tap → αναπαραγωγή μέσα στην εφαρμογή
        h.itemView.setOnClickListener(view -> {
            if (listener != null) listener.onVideoClick(v);
        });

        // Long press → άνοιγμα στο YouTube app ή browser
        h.itemView.setOnLongClickListener(vw -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + v.videoId));
            i.setPackage("com.google.android.youtube");
            try {
                vw.getContext().startActivity(i);
            } catch (ActivityNotFoundException ex) {
                vw.getContext().startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + v.videoId))
                );
            }
            return true;
        });
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView thumb;
        TextView title, date;
        VH(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
        }
    }
}

