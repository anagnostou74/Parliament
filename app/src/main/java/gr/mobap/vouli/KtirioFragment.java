package gr.mobap.vouli;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import gr.mobap.R;

public class KtirioFragment extends Fragment {


    public KtirioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String htmlText = getResources().getString(R.string.ktirioKeim);
        View view = inflater.inflate(R.layout.fragment_keimena, container, false);

        TextView tv = view.findViewById(R.id.textThesmos);
        tv.setMovementMethod(LinkMovementMethod.getInstance());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv.setText(Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tv.setText(Html.fromHtml(htmlText));
        }
        // Inflate the layout for this fragment
        return view;
    }

}