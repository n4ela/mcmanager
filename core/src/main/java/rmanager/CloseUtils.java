package rmanager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Утилитный метод для закрытия потоков, коннекшенов к БД и.т.д
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 * Date: 22.08.2011
 */
public class CloseUtils {

    /**
     * Закрытие {@link InputStream} с проглатыванием исключения
     * @param is
     */
    public static void softClose(InputStream is) {
        try {
            is.close();
        } catch (IOException e) {
        }
    }
}
