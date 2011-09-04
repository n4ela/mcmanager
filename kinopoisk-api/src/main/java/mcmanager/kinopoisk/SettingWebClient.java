package mcmanager.kinopoisk;

import java.util.HashMap;
import java.util.Map;

public enum SettingWebClient {
    USER_AGENT("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; ru; rv:1.9.2.9) Gecko/20100908 Firefox/3.6.9"),
    ACCEPT("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"),
    ACCEPT_LANGUAGE("Accept-Language", "ru,en-us;q=0.7,en;q=0.3"),
    ACCEPT_ENCODING("Accept-Encoding", "gzip,deflate"),
    ACCEPT_CHARSET("Accept-Charset", "UTF-8,*"),
    KEEP_ALIVE("Keep-Alive", "115"),
    CONNECTION("Connection", "keep-alive"),
    CACHE_CONTROL("Cache-Control", "max-age=0");
    
    private String key;
    private String value;
    
    SettingWebClient(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
    
    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (SettingWebClient settings : values()) {
            map.put(settings.key, settings.value);
        }
        return map;
    }
}
