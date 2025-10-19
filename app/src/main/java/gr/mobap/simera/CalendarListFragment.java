package gr.mobap.simera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import gr.mobap.R;

public class CalendarListFragment extends Fragment {

    private RecyclerView mRecycler;
    private View progress, empty, error;
    private CalendarAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_all_calendar, container, false);

        mRecycler = root.findViewById(R.id.calendarList);
        progress  = root.findViewById(R.id.progress);
        empty     = root.findViewById(R.id.empty);
        error     = root.findViewById(R.id.error);

        mRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Απενεργοποίηση change animations – αποφεύγει “Inconsistency detected”
        RecyclerView.ItemAnimator anim = mRecycler.getItemAnimator();
        if (anim instanceof androidx.recyclerview.widget.SimpleItemAnimator) {
            ((androidx.recyclerview.widget.SimpleItemAnimator) anim)
                    .setSupportsChangeAnimations(false);
        }

        // Firebase options
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("calendar");
        FirebaseRecyclerOptions<CalendarData> opts =
                new FirebaseRecyclerOptions.Builder<CalendarData>()
                        .setQuery(ref.orderByChild("dateEpoch"), CalendarData.class)
                        .build();

        // Adapter (δίνουμε host για το permission του καμπανακιού)
        mAdapter = new CalendarAdapter(
                opts,
                requireContext(),
                (CalendarAdapter.PermissionHost) requireActivity()
        );
        mRecycler.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) mAdapter.stopListening();
    }

    // Αν χρησιμοποιείς SwipeRefreshLayout, κάνε refresh με post()
    public void refreshDataSafely() {
        if (mRecycler != null) {
            mRecycler.post(() -> {
                if (mAdapter != null) {
                    mAdapter.stopListening();
                    mAdapter.startListening();
                }
            });
        }
    }
}
