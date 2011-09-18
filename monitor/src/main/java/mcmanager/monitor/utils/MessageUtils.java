package mcmanager.monitor.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtils {

    public static String createMessage(String title, String regexp, String message) {
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(title);
        List<String> list = new ArrayList<String>();
        try {
            matcher.find();
            for (int i = 1; i <= matcher.groupCount(); ++i) {
                list.add(matcher.group(i).trim());
            }
        } catch (IllegalStateException e) {
            return "Раздача с заголовком: " + title + " была поставлена на раздачу/скачена, "
                    + "но произошла ошибка при формировании сообщения"; 
        }
        return MessageFormat.format(message, list.toArray());
    }

}
