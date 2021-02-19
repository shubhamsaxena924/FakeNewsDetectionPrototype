package parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static parser.GoogleParser.getString;

public class ImageParser {
    /**
     * Get the html content of the link
     *
     * @param relHref String which stores the link
     * @return the String object (html content) returned
     * @throws IOException
     */
    public static String getLinkContent(String relHref) throws IOException {
        relHref = relHref.trim();
        final String agent = "Mozilla/5.0";
        URL url = new URL(relHref);
        final URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", agent);
        final InputStream stream = connection.getInputStream();
        return getString(stream);
    }

    public static List parseImageSrc(final String html, List<String> queryList) {
        List<String> imageLinks = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements results = doc.select("img");
        for (Element result : results) {
            Elements parent = result.parent().getAllElements();
            String altText = parent.attr("alt");
            String srcText = parent.attr("src");
            for (String s : queryList) {
                s=s.toLowerCase();
                if ((altText.toLowerCase().contains(s) || srcText.toLowerCase().contains(s))&& srcText.contains(".jpg")) {
                    imageLinks.add(srcText);
                    break;
                }
            }
        }
        return imageLinks;
    }
}
