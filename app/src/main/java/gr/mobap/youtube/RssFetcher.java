package gr.mobap.youtube;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RssFetcher {

    private final OkHttpClient client = new OkHttpClient();

    public List<YoutubeVideo> fetch(String channelId) throws Exception {
        if (channelId == null || channelId.trim().isEmpty()) return new ArrayList<>();
        String url = "https://www.youtube.com/feeds/videos.xml?channel_id=" + channelId;

        Request req = new Request.Builder().url(url).build();
        try (Response res = client.newCall(req).execute()) {
            if (!res.isSuccessful() || res.body() == null) return new ArrayList<>();
            String xml = res.body().string();
            return parse(xml);
        }
    }

    private List<YoutubeVideo> parse(String xml) throws Exception {
        List<YoutubeVideo> list = new ArrayList<>();
        Document doc = Jsoup.parse(xml, "", org.jsoup.parser.Parser.xmlParser());
        for (Element entry : doc.select("feed > entry")) {
            String videoId = entry.selectFirst("yt|videoId") != null
                    ? entry.selectFirst("yt|videoId").text() : null;
            String title = entry.selectFirst("title") != null
                    ? entry.selectFirst("title").text() : "";
            String published = entry.selectFirst("published") != null
                    ? entry.selectFirst("published").text() : "";
            String thumb = entry.selectFirst("media|group > media|thumbnail") != null
                    ? entry.selectFirst("media|group > media|thumbnail").attr("url") : null;

            if (videoId != null) {
                list.add(new YoutubeVideo(videoId, title, published, thumb));
            }
        }
        return list;
    }
}
