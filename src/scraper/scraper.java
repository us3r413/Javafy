package src.scraper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.util.concurrent.TimeUnit;


public class scraper {
    public String getSongWriter(String songName, int duration) {
        try {
            String query = songName.replace(" ", "+");
            String url = "https://genius.com/api/search/multi?per_page=5&q=" + query;

            Connection.Response response = Jsoup.connect(url)
                    .ignoreContentType(true) // Important to parse JSON
                    .userAgent("Mozilla/5.0")
                    .execute();

            String body = response.body();
            JSONObject json = new JSONObject(body);
            JSONArray sections = json.getJSONObject("response").getJSONArray("sections");
            int bestTime = Integer.MAX_VALUE;
            String bestArtist = "N/A";
            for (int i = 0; i < sections.length(); i++) {
                JSONObject section = sections.getJSONObject(i);
                if (section.getString("type").equals("song")) {
                    JSONArray hits = section.getJSONArray("hits");
                    if (!hits.isEmpty()) {
                        for (int j = 0; j < hits.length(); j++) {
                            JSONObject song = hits.getJSONObject(j).getJSONObject("result");
                            String urlString = song.getString("url");
                        }
                    }
                }
            }
            return bestArtist;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        return "N/A";
    }
    public String getSongWriter(String songName) {
        try {
            String query = songName.replace(" ", "+");
            String url = "https://genius.com/api/search/multi?per_page=5&q=" + query;

            Connection.Response response = Jsoup.connect(url)
                    .ignoreContentType(true) // Important to parse JSON
                    .userAgent("Mozilla/5.0")
                    .execute();

            String body = response.body();
            JSONObject json = new JSONObject(body);
            JSONArray sections = json.getJSONObject("response").getJSONArray("sections");

            for (int i = 0; i < sections.length(); i++) {
                JSONObject section = sections.getJSONObject(i);
                if (section.getString("type").equals("song")) {
                    JSONArray hits = section.getJSONArray("hits");
                    if (!hits.isEmpty()) {
                        JSONObject song = hits.getJSONObject(0).getJSONObject("result");
                        String artistName = song.getJSONObject("primary_artist").getString("name");
                        System.out.println("Artist: " + artistName);
                        return artistName;
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        return "N/A";
    }
    private int stringtoSeconds(String time) {
        String[] parts = time.split(":");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        return minutes * 60 + seconds;
    }


}
