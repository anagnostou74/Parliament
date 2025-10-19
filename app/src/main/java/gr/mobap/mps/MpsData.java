package gr.mobap.mps;
import androidx.annotation.Keep;
import com.google.firebase.database.IgnoreExtraProperties;

@Keep
@IgnoreExtraProperties
public class MpsData {
    public String rank;
    public String govRank;
    public String epitheto;
    public String onoma;
    public String onomaPatros;
    public String titlos;
    public String govPosition;
    public String image;
    public String komma;
    public String perifereia;
    public String birth;
    public String family;
    public String epaggelma;
    public String parliamentActivities;
    public String socialActivities;
    public String spoudes;
    public String languages;
    public String address;
    public String site;
    public String email;
    public String phone;
    public String facebook;
    public String twitter;
    public String instagram;
    public String linkedin;
    public String youtube;
    public String pr_ko_rank;
    public String antipr_rank;
    public String perifereia_komma;

    public MpsData() {

    }

    public MpsData(String rank,
                   String govRank,
                   String epitheto,
                   String onoma,
                   String onomaPatros,
                   String titlos,
                   String govPosition,
                   String image,
                   String komma,
                   String perifereia,
                   String birth,
                   String family,
                   String epaggelma,
                   String parliamentActivities,
                   String socialActivities,
                   String spoudes,
                   String languages,
                   String address,
                   String site,
                   String email,
                   String phone,
                   String facebook,
                   String twitter,
                   String instagram,
                   String linkedin,
                   String youtube,
                   String pr_ko_rank,
                   String antipr_rank,
                   String perifereiaKomma) {


        this.epitheto = epitheto;
        this.onoma = onoma;
        this.onomaPatros = onomaPatros;
        this.titlos = titlos;
        this.govPosition = govPosition;
        this.image = image;
        this.komma = komma;
        this.perifereia = perifereia;
        this.birth = birth;
        this.family = family;
        this.epaggelma = epaggelma;
        this.parliamentActivities = parliamentActivities;
        this.socialActivities = socialActivities;
        this.spoudes = spoudes;
        this.languages = languages;
        this.address = address;
        this.site = site;
        this.email = email;
        this.phone = phone;
        this.facebook = facebook;
        this.twitter = twitter;
        this.youtube = youtube;
        this.instagram = instagram;
        this.linkedin = linkedin;
        this.pr_ko_rank = pr_ko_rank;
        this.antipr_rank = antipr_rank;
        this.perifereia_komma = perifereiaKomma;
    }
}