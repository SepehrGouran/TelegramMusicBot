package main;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by Sepehr on 5/25/2017.
 */
public class Main {
    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(Main.class);

        String URL = "http://www.google.com";

        HttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpResponse response = httpClient.execute(new HttpGet(URL));
            int statusCode = response.getStatusLine().getStatusCode();
            logger.info("Status Code: " + statusCode);

        } catch (UnknownHostException e) {
            logger.error("Check your internet connection");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
