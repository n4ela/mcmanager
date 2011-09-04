package mcmanager.kinopoisk.utils;

import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.nodes.Element;

/**
 * Вспомогательный класс для работы с библиотекой jsoup
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 * Date: 04.09.2011
 */
public class JSoupUtils {

    /**
     * Установить настройки бразера которым будет осуществлен заход на страницу
     * @param headers    - мап user-agent, кодировка и т.д.
     * @param connection - сам коннекшен
     * @return конекшен у становленными параметрами
     */
    public static Connection setHeader(Map<String, String> headers, Connection connection) {
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
