package main;

import Oauth2.Auth;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by Sepehr on 5/25/2017.
 */
public class Search {


        /*Logger logger = LoggerFactory.getLogger(Search.class);

        String baseURL = "https://api.telegram.org/bot291366280:AAHFt-W8VwmGjLEunUsSinti2Cf0taynmsw";
        String getMeMethod = "/getMe";


        TelegramBot telegramBot = TelegramBotAdapter.build("291366280:AAHFt-W8VwmGjLEunUsSinti2Cf0taynmsw");*/


        private static final String PROPERTIES_FILENAME = "youtube.properties";

        private static final long NUMBER_OF_VIDEOS_RETURNED = 3;

        /**
         * Define a global instance of a Youtube object, which will be used
         * to make YouTube Data API requests.
         */
        private static YouTube youtube;

        /**
         * Initialize a YouTube object to search for videos on YouTube. Then
         * display the name and thumbnail image of each video in the result set.
         *
         * @param args command line args.
         */
        public static void main(String[] args) {

            // Read the developer key from the properties file.
            Properties properties = new Properties();
            try {
                InputStream in = Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
                properties.load(in);

            } catch (IOException e) {
                System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
                        + " : " + e.getMessage());
                System.exit(1);
            }

            try {
                // This object is used to make YouTube Data API requests. The last
                // argument is required, but since we don't need anything
                // initialized when the HttpRequest is initialized, we override
                // the interface and provide a no-op function.
                youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
                    public void initialize(HttpRequest request) throws IOException {
                    }
                }).setApplicationName("youtube-cmdline-search-sample").build();

                // Prompt the user to enter a query term.
                String queryTerm = getInputQuery();

                // Define the API request for retrieving search results.
                YouTube.Search.List search = youtube.search().list("id,snippet");

                // Set your developer key from the {{ Google Cloud Console }} for
                // non-authenticated requests. See:
                // {{ https://cloud.google.com/console }}
                String apiKey = properties.getProperty("youtube.apikey");
                search.setKey(apiKey);
                search.setQ(queryTerm);

                // Restrict the search results to only include videos. See:
                // https://developers.google.com/youtube/v3/docs/search/list#type
                search.setType("video");

                // To increase efficiency, only retrieve the fields that the
                // application uses.
                search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
                search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

                // Call the API and print results.
                SearchListResponse searchResponse = search.execute();
                List<SearchResult> searchResultList = searchResponse.getItems();
                if (searchResultList != null) {
                    prettyPrint(searchResultList.iterator(), queryTerm);
                }
            } catch (GoogleJsonResponseException e) {
                System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                        + e.getDetails().getMessage());
            } catch (IOException e) {
                System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

    /*
     * Prompt the user to enter a query term and return the user-specified term.
     */
        private static String getInputQuery() throws IOException {

            String track = "";
            String artist = "";

            System.out.print("Please enter the Track name: ");
            BufferedReader bReaderTrack = new BufferedReader(new InputStreamReader(System.in));
            track = bReaderTrack.readLine();

            //System.out.print("Please enter Artist name: ");
            //BufferedReader bReaderArtist = new BufferedReader(new InputStreamReader(System.in));
            //artist = bReaderArtist.readLine();

            if (track.length() < 1) {
                // Use the string "YouTube Developers Live" as a default.
                track = " ";
            }

            if (artist.length() < 1) {
                // Use the string "YouTube Developers Live" as a default.
                artist = " ";
            }

            return track;
        }

    /*
     * Prints out all results in the Iterator. For each result, print the
     * title, video ID, and thumbnail.
     *
     * @param iteratorSearchResults Iterator of SearchResults to print
     *
     * @param query Search query (String)
     */
        private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

            System.out.println("\n=============================================================");
            System.out.println(
                    "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
            System.out.println("=============================================================\n");

            if (!iteratorSearchResults.hasNext()) {
                System.out.println(" There aren't any results for your query.");
            }

            while (iteratorSearchResults.hasNext()) {

                SearchResult singleVideo = iteratorSearchResults.next();
                ResourceId rId = singleVideo.getId();

                // Confirm that the result represents a video. Otherwise, the
                // item will not contain a video ID.
                if (rId.getKind().equals("youtube#video")) {
                    Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();

                    System.out.println(" Video Id" + rId.getVideoId());
                    System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
                    System.out.println(" Thumbnail: " + thumbnail.getUrl());
                    YouTubeMp3 youTubeMp3 = new YouTubeMp3();
                    String downloadLink = youTubeMp3.getDownloadLink("https://www.youtube.com/watch?v=" + rId.getVideoId());
                    System.out.println(downloadLink);
                    System.out.println("\n-------------------------------------------------------------\n");
                }


            }
        }

}
