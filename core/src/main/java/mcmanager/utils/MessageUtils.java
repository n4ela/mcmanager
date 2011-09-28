package mcmanager.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mcmanager.exception.CoreException;


public class MessageUtils {

    public static String createMessage(String title, String regexp, String message) {
        List<String> list = new ArrayList<String>();
        if (!StringUtils.isEmpty(regexp)) {
            Pattern pattern = Pattern.compile(regexp);
            Matcher matcher = pattern.matcher(title);
            try {
                matcher.find();
                for (int i = 1; i <= matcher.groupCount(); ++i) {
                    list.add(matcher.group(i).trim());
                }
            } catch (IllegalStateException e) {
                return "Раздача с заголовком: " + title + " была поставлена на раздачу/скачена, "
                        + "но произошла ошибка при формировании сообщения"; 
            }
        }
        return MessageFormat.format(message, list.toArray());
    }
    
    public static String parseEpisode(String filmFile, String regexp) throws CoreException {
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(filmFile);
        if (matcher.find() && matcher.group(1) != null) {
            return prepareNumber(matcher.group(1));  
        } 
        throw new CoreException("Ошибка при разборе номера серии по regexp: " 
                + regexp + " файл: " + filmFile);

    }
    
    /**
     * Если число содержит одну цифру то подставляет 0 в начало строки
     * @param str
     * @return
     */
    public static String prepareNumber(String str) {
        return str.length() == 1 ? "0" + str : str;

    }

}
