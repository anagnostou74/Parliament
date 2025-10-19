package gr.mobap.youtube;

public class YoutubeVideo {
    public final String videoId;
    public final String title;
    public final String published; // ISO 8601 ή φιλτραρισμένο
    public final String thumbUrl;

    public YoutubeVideo(String videoId, String title, String published, String thumbUrl) {
        this.videoId = videoId;
        this.title = title;
        this.published = published;
        this.thumbUrl = thumbUrl;
    }
}