package main;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.UnknownHostException;

/**
 * Created by Sepehr on 5/25/2017.
 */
public class Main {
    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(Main.class);

        String baseURL = "https://api.telegram.org/bot291366280:AAHFt-W8VwmGjLEunUsSinti2Cf0taynmsw";
        String getMeMethod = "/getMe";


        TelegramBot telegramBot = TelegramBotAdapter.build("291366280:AAHFt-W8VwmGjLEunUsSinti2Cf0taynmsw");
    }
}
