package youtube;

/**
 * Created by Sepehr on 5/27/2017.
 */
public class YouTubeMp3 {

    private static final String baseURL = "http://www.youtubeinmp3.com/fetch/?video=";

    public String getDownloadLink (String videoURL) {

        return baseURL + videoURL;
    }
}
