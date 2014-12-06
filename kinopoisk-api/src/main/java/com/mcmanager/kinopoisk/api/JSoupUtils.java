package com.mcmanager.kinopoisk.api;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 * @author Ivanov D.V. (ivanovdw@gmail.com)
 *         Date 30.11.14.
 */
public class JSoupUtils {

    public static Connection goToUrl(String url, String append) {
        return goToUrl(url + (url.endsWith("/") ? "" : "/") + append);
    }


    public static Connection goToUrl(String url) {
        Connection connection = Jsoup.connect(url);
        for (SettingsWebClient header : SettingsWebClient.values()) {
            connection.header(header.getKey(), header.getValue());
        }
        return connection;
    }
}
