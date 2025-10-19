package gr.mobap.mps;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import gr.mobap.R;

public class MpsViewHolder extends RecyclerView.ViewHolder {
    public TextView rank;
    public TextView epitheto;
    public TextView onoma;
    public TextView onomaPatros;
    public TextView titlos;
    public TextView govPosition;
    public ImageView mpsImage;
    public TextView komma;
    public TextView perifereia;
    public TextView birth;
    public TextView family;
    public TextView epaggelma;
    public TextView parliamentActivities;
    public TextView socialActivities;
    public TextView spoudes;
    public TextView languages;
    public TextView address;
    public TextView email;
    public TextView instagram;
    public TextView linkedin;
    public TextView twitter;
    public TextView youtube;


    public MpsViewHolder(@NonNull View itemView) {
        super(itemView);

        //rank = itemView.findViewById(R.id.rank);
        epitheto = itemView.findViewById(R.id.epitheto);
        onoma = itemView.findViewById(R.id.onoma);
        onomaPatros = itemView.findViewById(R.id.onomaPatros);
        titlos = itemView.findViewById(R.id.titlos);
        govPosition = itemView.findViewById(R.id.govPosition);
        mpsImage = itemView.findViewById(R.id.mps_image);
        komma = itemView.findViewById(R.id.komma);
        perifereia = itemView.findViewById(R.id.perifereia);
        birth = itemView.findViewById(R.id.birth);
        family = itemView.findViewById(R.id.family);
        epaggelma = itemView.findViewById(R.id.epaggelma);
        parliamentActivities = itemView.findViewById(R.id.parliamentActivities);
        socialActivities = itemView.findViewById(R.id.socialActivities);
        spoudes = itemView.findViewById(R.id.spoudes);
        languages = itemView.findViewById(R.id.languages);
        address = itemView.findViewById(R.id.address);
        email = itemView.findViewById(R.id.email_img);
        instagram = itemView.findViewById(R.id.insta_img);
        linkedin = itemView.findViewById(R.id.linkedin_img);

    }
}
