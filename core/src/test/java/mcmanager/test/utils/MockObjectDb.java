package mcmanager.test.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import junitx.util.PrivateAccessor;
import mcmanager.data.ObjectDB;
import mcmanager.exception.CoreException;
import mcmanager.utils.LogUtils;
import mcmanager.utils.StringUtils;

/**
 * Класс для работы с Mock объектами базы данных
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 * Date: 28.08.2011
 */
public class MockObjectDb {

    /**
     * Загрузить объекты из файла
     * @param fileName - имя файла
     * @param clazz    - класс ожидаемых объектов
     * @return Map\<Id, Объект\>
     * @throws Throwable
     */
    public static <T extends ObjectDB> Map<Long, T> loadGroup(String fileName, Class<T> clazz) throws Throwable {
        Map<Long, T> objects = new HashMap<Long, T>();
        BufferedReader br = null;
        T object = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
            String str = null;
            while ((str = br.readLine()) != null) {
                int endKey = str.indexOf("=");
                if (endKey < 1 || StringUtils.isEmpty(str.substring(endKey + 1)) 
                        || endKey + 1 == str.length()) {
                    LogUtils.getDefaultLog().warn("Строка " + str + " будет " +
                            "пропущена при разборе файла настроек");
                    continue;
                }
                String key = str.substring(0, endKey).trim();
                String value = str.substring(endKey + 1).trim();
                if (key.equalsIgnoreCase("id")) {
                    if (object != null) {
                        objects.put(object.getId(), object);
                    }
                    object = clazz.newInstance();
                    object.setId(Long.valueOf(value));
                    continue;
                }
                Method method = ReflectUtils.getMethodByName(clazz, "set" + key);
                invokeMethod(method, value, object, method.getParameterTypes()[0]);
            }
            if (object != null) {
                objects.put(object.getId(), object);
            }
        } finally {

        }
        return objects;
    }
    
    private static void invokeMethod(Method method, String value, Object object, Class clazz) throws Throwable {
        //TODO Возможно тут можно воспользовать приведением через рефликсию
        if (clazz.getName().equals("java.lang.String"))
            PrivateAccessor.invoke(object, method.getName(), 
                    new Class[] {clazz}, new Object[] {value});
        else if (clazz.getName().equals("java.lang.Long")) { 
            Long longValue = Long.valueOf(value);
            PrivateAccessor.invoke(object, method.getName(), 
                    new Class[] {clazz}, new Object[] {longValue});
        } else if (clazz.getName().equals("java.lang.Integer")) { 
            Integer intValue = Integer.valueOf(value);
            PrivateAccessor.invoke(object, method.getName(), 
                    new Class[] {clazz}, new Object[] {intValue});
        } else {
            throw new CoreException("Не известный тип: " +  clazz.getName());
        }

    }
}
