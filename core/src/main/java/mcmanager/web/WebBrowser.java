package mcmanager.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mcmanager.exception.CoreException;

import org.apache.commons.logging.Log;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;

public class WebBrowser {

    private final Log log;

    /**
     * Статические кукисы, существуют в единственном экземпляре в рамках все программы
     */
    private static Map<String, String> cookies = new HashMap<String, String>();

    private final static String LOGIN_SERVER = "http://login.rutracker.org/forum/login.php";
    private final static String MAIN_PAGE = "http://rutracker.org/forum/index.php";
    private static final int TIMEOUT = 15000;

    private final String LOGIN = "rutracker-manager";
    private final String PASSWORD = "z1x2c3v4";
    private final String SECRET_KEY = "Вход";

    private Document page;

    public WebBrowser(Log log) {
        this.log = log;
    }
    
    private void loginInRutracker() throws IOException {
        //Заход на главную страницу
        log.debug("Заход на главную страницу трекера: " + MAIN_PAGE);
        JSoupUtils.goToUrl(MAIN_PAGE).get();
        //Логин и получение куки
        log.debug("Производится идентификация пользователя");
        log.debug("Логин: " + LOGIN + " пароль: " + PASSWORD + 
                " дополнительный параметр: " + SECRET_KEY);
        Connection connection = JSoupUtils.goToUrl(LOGIN_SERVER).timeout(TIMEOUT);
        Connection.Response responce = connection.method(Method.POST)
                .data("login_username", LOGIN, "login_password", PASSWORD,
                        "login", SECRET_KEY).execute();
        cookies = responce.cookies();
        log.debug("cookie получены: " + cookies);
    }

    private CookieStatus checkCookie(Map<String, String> actualCookies) throws IOException {
        synchronized (cookies) {
            if (cookies.get("bb_data") == null ||
                    (actualCookies.get("bb_data") != null && 
                    actualCookies.get("bb_data").equals("deleted"))) {
                log.debug("Cookies устарели пробуем получить новые куки");    
                loginInRutracker();
                if (cookies.get("bb_data") == null) {
                    return CookieStatus.BAD;
                }
                return CookieStatus.REJOINING;
            }
            return CookieStatus.OK;
        }
    }

    private int countRejount = 0;
    private int TIME_SLEEP = 5000;
    private int COUNT_RECONNECT = 3;

    public void goToUrl(String url) throws CoreException {
        Connection.Response response;
        CookieStatus cookieStatus;
        synchronized (cookies) {
            log.debug("Заход на страницу " + url + cookies);
            try {
                response = JSoupUtils.goToUrl(url).timeout(TIMEOUT)
                        .cookies(cookies).method(Method.GET).execute();
                cookieStatus = checkCookie(response.cookies());
            } catch (IOException e) {
                throw new CoreException(e);
            }
        }
        if (cookieStatus == CookieStatus.OK) {
            log.debug("Заход на страницу " + url + " прошел успешно");
            countRejount = 0;
            try {
                page = response.parse();
            } catch (IOException e) {
                throw new CoreException(e);
            }
            //TODO надо сделать проверку на тот случай если тайтл пустой
            //            return response.parse().getElementsByClass("maintitle").select("a[href]").first().text();
        } else if (cookieStatus == CookieStatus.BAD) {
            throw new CoreException("Ошибка при получении cookies");
        } else if (cookieStatus == CookieStatus.REJOINING) {
            log.debug("Cookies изменились, требуется повторить запрос к странице: " + url);
            if (countRejount++ < COUNT_RECONNECT) {
                log.debug("Задержка: " + TIME_SLEEP + "мс.");
                try {
                    Thread.sleep(TIME_SLEEP);
                } catch (InterruptedException e) {
                    throw new CoreException(e);
                }
                goToUrl(url);
            } else {
                throw new CoreException("Число попыток повторного захода на страницу " + url + 
                        " привысило " + COUNT_RECONNECT + " прерываем работу"); 
            }
        } else {
            throw new CoreException("Ошибка при перемещениям по страницам cookieStatus: " + cookieStatus);
        }

    }

    public String getTitle() throws CoreException {
        if (page != null)
            return page.getElementsByClass("maintitle").select("a[href]").first().text();
        else 
            throw new CoreException("Страница не была получена (page==null)");
    }

    public String getTorrentUrl() throws CoreException {
        if (page != null) {
            String url = page.getElementsByClass("dl-link").attr("href"); 
            if (!url.isEmpty())
                return url;
            else 
                throw new CoreException("Не удалось найти ссылку на торрент файл");
        } else 
            throw new CoreException("Страница не была получена (page==null)");
    }

    //TODO в метод надо добавить проверку кукисов
    public TorrentFile downloadTorrentFile(String url) throws CoreException {
        Connection.Response response;
        synchronized (cookies) {
            try {
                response = JSoupUtils.goToUrl(url).timeout(TIMEOUT)
                        .cookies(cookies).method(Method.POST).execute();
            } catch (IOException e) {
                throw new CoreException(e);
            }
            String contentType = response.contentType();
            int beginFileName = contentType.indexOf("name=");
            if (contentType.isEmpty() || beginFileName == -1)
                throw new CoreException("Не удалось получить torrent файл по ссылки " + url);
            String fileName = contentType.substring(beginFileName + 6, contentType.length() - 1);
            return new TorrentFile(fileName, response.bodyAsBytes());
        }
    }

    public class TorrentFile {
        private final String name;
        private final byte[] content;

        public TorrentFile(String name, byte[] content) {
            this.name = name;
            this.content = content;
        }
        public String getName() {
            return name;
        }
        public byte[] getContent() {
            return content;
        }
    }

    private enum CookieStatus {
        OK(1),
        REJOINING(2),
        BAD(3);

        private int status;

        private CookieStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

    }
}
