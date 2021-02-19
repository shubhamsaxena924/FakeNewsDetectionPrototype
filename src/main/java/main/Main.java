package main;

import parser.HeadAndRef;
import writer.WriteCSV;

import javax.net.ssl.*;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Scanner;

import static parser.GoogleParser.getSearchContent;
import static parser.GoogleParser.parseLinks;

public class Main {

    static {
        TrustManager[] trustAllCertificates = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null; // Not relevant.
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        // Do nothing. Just allow them all.
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        // Do nothing. Just allow them all.
                    }
                }
        };

        HostnameVerifier trustAllHostnames = (hostname, session) -> {
            return true; // Just allow them all.
        };

        try {
            System.setProperty("jsse.enableSNIExtension", "false");
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCertificates, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(trustAllHostnames);
        } catch (GeneralSecurityException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome!\nEnter a term to search for: ");
        String searchTerm = sc.nextLine();
        System.out.println("Enter Number of search counts (Approx.) : ");
        int searchCount = sc.nextInt();
        searchCount = searchCount + 3;
        System.out.println("Searching for: " + searchTerm);
        String searchTermUrl = searchTerm.replace(' ', '+');
        String query = "https://www.google.com/search?q=" + searchTermUrl + "&num=" + searchCount;
        System.out.println(query);

        //Get the search result content
        String page = null;
        try {
            page = getSearchContent(query);
        } catch (Exception e) {
            System.out.println(e);
        }

        //Extract Links from the whole search result
        List<HeadAndRef> links = null;
        try {
            links = parseLinks(page);
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println();
        System.out.println(links.size() + " Results Found:");
        for (int i = 0; i < links.size(); i++) {
            System.out.println(links.get(i));
        }
        System.out.println(WriteCSV.writingToCSV(links, searchTerm));
    }
}
