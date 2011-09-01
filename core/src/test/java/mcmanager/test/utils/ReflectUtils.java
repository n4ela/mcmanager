package mcmanager.test.utils;

import static junit.framework.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import junitx.util.PrivateAccessor;
import mcmanager.data.Distribution;
import mcmanager.data.Group;
import mcmanager.exception.CoreException;
/**
 * Вспомогательные класс рефликсии
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 * Date: 28.08.2011
 */
public class ReflectUtils {

    /**
     * Получить метод из класса по его имени
     * @param clazz      - класс
     * @param methodName - имя метода
     * @return           - метод
     * @throws CoreException
     */
    public static Method getMethodByName(Class clazz, String methodName) throws CoreException {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        throw new CoreException("В классе: " + clazz + " не найден метод: " + methodName);
    }

    public static void equalsByGetMethod(Object expected, Object actual) throws Throwable {
        if (expected instanceof Group &&
                actual instanceof Group) {
            Group expectedGroup = (Group)expected;
            Group actualGroup = (Group)actual;
            for (Method method : getAllGetter(Group.class)) {
                Object expectedObject = PrivateAccessor.invoke(expectedGroup, method.getName(), 
                        method.getParameterTypes(), new Object[]{});
                Object actualObject = PrivateAccessor.invoke(actualGroup, method.getName(), 
                        method.getParameterTypes(), new Object[]{});
                equals(expectedObject, actualObject, method.getName());
            }
        } else if (expected instanceof Distribution &&
                actual instanceof Distribution) {
            Distribution expectedDistribution = (Distribution)expected;
            Distribution actualDistribution = (Distribution)actual;
            for (Method method : getAllGetter(Distribution.class)) {
                Object expectedObject = PrivateAccessor.invoke(expectedDistribution, method.getName(), 
                        method.getParameterTypes(), new Object[]{});
                Object actualObject = PrivateAccessor.invoke(actualDistribution, method.getName(), 
                        method.getParameterTypes(), new Object[]{});
                System.out.println("expectedObject: " + expectedObject + " actualObject: " + actualObject);
                equals(expectedObject, actualObject, method.getName());
            }
        } else {
            //TODO тут может быть NullPointerException
            throw new CoreException("Не известный класс: " + expected.getClass() + " " + actual.getClass());
        }
    }

    private static void equals(Object expected, Object actual, String name) throws CoreException {
        if (expected instanceof String && actual instanceof String) {
            assertEquals(name, (String)expected, (String)actual);
        } else if (expected instanceof Long && actual instanceof Long) {
            assertEquals(name, (Long)expected, (Long)actual);
        } else if (expected instanceof Integer && actual instanceof Integer) {
            assertEquals(name, (Integer)expected, (Integer)actual);
        } else if (expected != null || actual != null) {
            StringBuilder builder = new StringBuilder("Ошибка: method=").append(name);
            builder.append(" expected=").append(expected)
                .append(" actual=").append(actual);
            if (expected != null)
                builder.append(" expected class: ").append(expected.getClass());
            if (actual != null)
                builder.append(" actual class: ").append(actual.getClass());
            throw new CoreException(builder.toString());
        }
    }

    private static Set<Method> getAllGetter(Class clazz) {
        Set<Method> methods = new HashSet<Method>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("get")) {
                methods.add(method);
            }
        }
        return methods;
    }
}
