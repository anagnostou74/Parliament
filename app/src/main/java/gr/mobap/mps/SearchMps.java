package gr.mobap.mps;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import gr.mobap.R;

public class SearchMps extends Fragment {
    private FirebaseRecyclerAdapter<MpsData, MpsViewHolder> mAdapter;
    Query mpsSearchQuery;
    private FirebaseAnalytics mFirebaseAnalytics;
    private AlertDialog.Builder builder;
    private Button perifereiaButton;
    private Button kommaButton;
    private Button searchButton;
    private Button clearButton;
    private DatabaseReference mDatabase;
    public String TAG = getClass().getSimpleName();
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    FirebaseRecyclerOptions options;

    public SearchMps() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.search_mps, container, false);

        mRecycler = rootView.findViewById(R.id.mpsList);
        mRecycler.setHasFixedSize(true);
        // Initialize Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("mps");

        perifereiaButton = (Button) rootView.findViewById(R.id.perifereiaButton);
        perifereiaButton.setText(R.string.perif_label);
        perifereiaButton.setOnClickListener(this::perifereiaChoiceDialog);

        kommaButton = (Button) rootView.findViewById(R.id.kommaButton);
        kommaButton.setText(R.string.kommatxt);
        kommaButton.setOnClickListener(this::kommaChoiceDialog);

        searchButton = (Button) rootView.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this::searchMps);

        clearButton = (Button) rootView.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(this::clear);

        return rootView;
    }

    private void perifereiaChoiceDialog(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("perifereies");
        Log.d(TAG, "myRef: " + myRef);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> perifereies = new ArrayList<String>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String perifereia = String.valueOf(ds.child("perifereia").getValue());
                    perifereies.add(perifereia);
                }
                Log.d(TAG, "perifereia: " + perifereies);
                String[] txt = {String.valueOf(perifereies)};
                Log.d(TAG, "txt: " + Arrays.toString(txt));
                String[] perifereia = perifereies.toArray(new String[0]);
                builder = new AlertDialog.Builder(getContext());
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle(R.string.perif_label);
                builder.setSingleChoiceItems(perifereia, 0, (dialogInterface, i) -> {
                    perifereiaButton.setText(perifereia[i]);
                    dialogInterface.dismiss();
                });
                AlertDialog dialog = builder
                        .create();
                dialog.show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private void kommaChoiceDialog(View view) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("kommata");
        Log.d(TAG, "myRef: " + myRef);
        //Query kommaQuery = myRef.orderByChild("komma");
        //Log.d(TAG, "Value is: " + kommaQuery);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> kommata = new ArrayList<String>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String komma = String.valueOf(ds.child("komma").getValue());
                    kommata.add(komma);
                }
                Log.d(TAG, "kommata: " + kommata);
                String[] txt = {String.valueOf(kommata)};
                Log.d(TAG, "txt: " + Arrays.toString(txt));

                String[] komma = kommata.toArray(new String[0]);
                if (getActivity() != null) {
                    builder = new AlertDialog.Builder(getContext());
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setTitle(R.string.komma);
                    builder.setSingleChoiceItems(komma, 0, (dialogInterface, i) -> {
                        kommaButton.setText(komma[i]);
                        dialogInterface.dismiss();
                    });
                    AlertDialog dialog = builder
                            .create();
                    dialog.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void searchMps(View view) {
        String perifereia = getString(R.string.perif_label);
        perifereiaButton = (Button) getView().findViewById(R.id.perifereiaButton);
        String perifereiaText = perifereiaButton.getText().toString();
        String komma = getString(R.string.kommatxt);
        kommaButton = (Button) getView().findViewById(R.id.kommaButton);
        String kommaText = kommaButton.getText().toString();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("mps");

        if (!perifereia.equals(perifereiaText) && (komma.equals(kommaText))) {

            // Set up Layout Manager, reverse layout
            mManager = new LinearLayoutManager(getActivity());
            mRecycler.setLayoutManager(mManager);
            Log.d(TAG, "Value is: " + perifereiaText);

            Query perifereiaQuery = mDatabase.orderByChild("perifereia").equalTo(perifereiaText);

            FirebaseRecyclerOptions<MpsData> options = new FirebaseRecyclerOptions.Builder<MpsData>()
                    .setQuery(perifereiaQuery, MpsData.class)
                    .build();

            mAdapter = new MpsAdapter(options, getContext());
            mRecycler.setAdapter(mAdapter);
            mAdapter.startListening();
        } else if (perifereia.equals(perifereiaText) && !komma.equals(kommaText)) {

            // Set up Layout Manager, reverse layout
            mManager = new LinearLayoutManager(getActivity());
            mRecycler.setLayoutManager(mManager);
            Log.d(TAG, "Value is: " + kommaText);

            Query kommaQuery = mDatabase.orderByChild("komma").equalTo(kommaText);

            FirebaseRecyclerOptions<MpsData> options = new FirebaseRecyclerOptions.Builder<MpsData>()
                    .setQuery(kommaQuery, MpsData.class)
                    .build();

            mAdapter = new MpsAdapter(options, getContext());
            mRecycler.setAdapter(mAdapter);
            mAdapter.startListening();
        } else if (!perifereia.equals(perifereiaText) && (!komma.equals(kommaText))) {

            // Set up Layout Manager, reverse layout
            mManager = new LinearLayoutManager(getActivity());
            mRecycler.setLayoutManager(mManager);
            Log.d(TAG, "Value is: " + perifereiaText + kommaText);

            Query bothQuery = mDatabase.orderByChild("perifereia_komma").equalTo(perifereiaText + " " + kommaText);

            FirebaseRecyclerOptions<MpsData> options = new FirebaseRecyclerOptions.Builder<MpsData>()
                    .setQuery(bothQuery, MpsData.class)
                    .build();

            mAdapter = new MpsAdapter(options, getContext());
            mRecycler.setAdapter(mAdapter);
            mAdapter.startListening();
        } else if (perifereia.equals(perifereiaText) && (komma.equals(kommaText))) {
            Toast.makeText(getContext(), "Παρακαλώ κάνετε μία επιλογή", Toast.LENGTH_SHORT).show();
        }

    }

    private void clear(View view) {
        perifereiaButton.setText(R.string.perif_label);
        kommaButton.setText(R.string.kommatxt);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("mps");

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query mpsQuery = mDatabase;

        FirebaseRecyclerOptions<MpsData> options = new FirebaseRecyclerOptions.Builder<MpsData>()
                .setQuery(mpsQuery, MpsData.class)
                .build();

        mAdapter = new MpsAdapter(options, getContext());
        mRecycler.setAdapter(mAdapter);
        mAdapter.startListening();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

}
