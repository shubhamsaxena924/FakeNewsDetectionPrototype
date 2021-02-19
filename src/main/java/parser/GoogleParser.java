package parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static parser.ImageParser.getLinkContent;
import static parser.ImageParser.parseImageSrc;

public class GoogleParser {
    /**
     * Method to convert the {@link InputStream} to {@link String}
     *
     * @param is the {@link InputStream} object
     * @return the {@link String} object returned
     */
    public static String getString(InputStream is) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            /** finally block to close the {@link BufferedReader} */
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
        return sb.toString();
    }

    /**
     * The method will return the search page result in a {@link String} object
     *
     * @param googleSearchQuery the google search query
     * @return the content as {@link String} object
     * @throws Exception
     */
    public static String getSearchContent(String googleSearchQuery) throws Exception {
        googleSearchQuery = googleSearchQuery.trim();
        final String agent = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
        URL url = new URL(googleSearchQuery);
        final URLConnection connection = url.openConnection();
        /**
         * User-Agent is mandatory otherwise Google will return HTTP response
         * code: 403
         */
        connection.setRequestProperty("User-Agent", agent);
        final InputStream stream = connection.getInputStream();
        return getString(stream);
    }

    /**
     * Parse all links
     *
     * @param html the page
     * @return the list with all URLSs
     * @throws Exception
     */
    public static List<HeadAndRef> parseLinks(final String html) {
        //List of class that contains heading and reference of result
        List<HeadAndRef> result = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements results = doc.select("a > h3");
        int num = 1;  //for page rank of result
        for (Element link : results) {
            Elements parent = link.parent().getAllElements();
            String relHead = parent.select("h3 > div").text();  //select heading and its text
            String relHref = parent.attr("href");     //select href attribute's value
            List imageLinks = new ArrayList();    //For links of images in each search result link
            if (relHref.startsWith("/url?q=")) {
                relHref = relHref.replace("/url?q=", "");
            }
            String[] splittedString = relHref.split("&sa=");
            if (splittedString.length > 1) {
                relHref = splittedString[0];
            }
            List<String> queryList = new ArrayList<>(Arrays.asList(relHead.split(" ").clone()));
            try {
                imageLinks = parseImageSrc(getLinkContent(relHref), queryList);  //Find src image in each link
            } catch (IOException e) {
                System.out.println(e);
                ;
            }
            HeadAndRef relHeadRef = new HeadAndRef(num, relHead, relHref, imageLinks);
            num++;
            result.add(relHeadRef);
        }
        return result;
    }

}