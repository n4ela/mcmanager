package mcmanager.web;

import java.util.Map;

import mcmanager.web.SettingsWebClient;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

//TODO Этот класс полностью копирует класс из API KINOPOISKA
// Его нужно куда нибудь вынести
/**
 * Вспомогательный класс для работы с библиотекой jsoup
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 * Date: 04.09.2011
 */
public class JSoupUtils {

    public static Connection goToUrl(String url) { 
        return JSoupUtils.setHeader(SettingsWebClient.toMap(), Jsoup.connect(url));
    }
    
    /**
     * Установить настройки бразера которым будет осуществлен заход на страницу
     * @param headers    - мап user-agent, кодировка и т.д.
     * @param connection - сам коннекшен
     * @return конекшен у становленными параметрами
     */
    private static Connection setHeader(Map<String, String> headers, Connection connection) {
        for (Map.Entry<String, String> header : headers.entrySet()) {
            connection.header(header.getKey(), header.getValue());
        }
        return connection;
    }
    
    /**
     * Проверяет element на наличие в нем текста
     * @param element - элемент со страницы
     * @return true - если элемент содержит текст, иначе false
     */
    public static boolean elemenTexttIsEmpty(Element element) {
        return element == null || element.text().isEmpty();
    }
}
