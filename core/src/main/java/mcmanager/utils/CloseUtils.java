package mcmanager.utils;

import java.io.BufferedReader;
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
     * @param is - поток
     */
    public static void softClose(InputStream is) {
        try {
            is.close();
        } catch (IOException e) {
        }
    }
    
    /**
     * Закрытие {@link BufferedReader} с проглатыванием исключения
     * @param br - буффер
     */
    public static void softClose(BufferedReader br) {
        try {
            br.close();
        } catch (IOException e) {
        }
    }
}
