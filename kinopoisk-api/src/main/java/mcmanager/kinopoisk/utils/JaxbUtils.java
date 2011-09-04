package mcmanager.kinopoisk.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * Класс конвертер java object -> xml и xml -> java object
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 * Date: 04.09.2011
 */
public class JaxbUtils {
    /**
     * Конвертирует java object в xml
     * @param jaxbObj - java объект
     * @return Строка представляющая собой xml
     * @throws JAXBException
     */
    public static String jaxbMarshalToString(Object jaxbObj) throws JAXBException {
        Marshaller marshaller = JAXBContext.newInstance(jaxbObj.getClass()).createMarshaller();
        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        java.io.StringWriter sw = new java.io.StringWriter();
        marshaller.marshal(jaxbObj, sw);
        return sw.toString();
    }
}
