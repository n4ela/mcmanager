package mcmanager.test.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

import mcmanager.exception.CoreException;
import mcmanager.utils.CloseUtils;
import mcmanager.utils.LogUtils;

/**
 * Аналог класса проперти работающего через {@link LinkedHashMap}
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 * Date: 28.08.2011
 */
public class NewProperties {
    
    /**
     * Загрузить настройки из файла
     * @param fileName - имя файла
     * @return map настроек
     * @throws CoreException
     */
    public static Map<String, String> load(String fileName) throws CoreException {
        Map<String, String> prop = new LinkedHashMap<String, String>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
            String str = null;
            while ((str = br.readLine()) != null) {
                int endKey = str.indexOf("=");
                if (endKey < 1) {
                    LogUtils.getDefaultLog().warn("Строка " + str + " будет " +
                    		"пропущена при разборе файла настроек");
                    continue;
                }
                String key = str.substring(0, endKey);
                String value = str.substring(endKey + 1);
                prop.put(key.trim(), value.trim());
            }
        } catch (FileNotFoundException e) {
            throw new CoreException("Файл: " + fileName + " не найден", e);
        } catch (IOException e) {
            throw new CoreException(e);
        } finally {
            CloseUtils.softClose(br);
        }
        return prop;
    }
}
