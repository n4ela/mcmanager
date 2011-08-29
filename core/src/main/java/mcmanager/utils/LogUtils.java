package mcmanager.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Утилитный метод отвечающий за логи во всей программе
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 * Date: 28.08.2011
 */
public class LogUtils {

    /**
     * Получить дефолтный лог
     * @return лог
     */
    public static Log getDefaultLog() {
        //TODO Переделать на нормальный лог
        return LogFactory.getLog(LogUtils.class);
    }
}
