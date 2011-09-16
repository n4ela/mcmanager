package mcmanager.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
            if (is != null)
                is.close();
        } catch (IOException e) {
        }
    }
    
    /**
     * Закрытие {@link OutputStream} с проглатыванием исключения
     * @param os - поток
     */
    public static void softClose(OutputStream os) {
        try {
            if (os != null)
                os.close();
        } catch (IOException e) {
        }
    }
    
    /**
     * Закрытие {@link BufferedReader} с проглатыванием исключения
     * @param br - буффер
     */
    public static void softClose(BufferedReader br) {
        try {
            if (br != null)
                br.close();
        } catch (IOException e) {
        }
    }
}
