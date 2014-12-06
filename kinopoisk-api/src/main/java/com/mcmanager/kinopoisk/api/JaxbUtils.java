package com.mcmanager.kinopoisk.api;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * Класс конвертер java object -> xml и xml -> java object
 * @author Ivanov D.V. (ivanovdw@gmail.com)
 * Date: 04.09.2011
 */
public class JaxbUtils {

    /**
     * Конвертирует java object в xml
     * @param jaxbObj - java объект
     * @return Строка представляющая собой xml
     * @throws javax.xml.bind.JAXBException
     */
    public static String jaxbMarshalToString(Object jaxbObj) throws JAXBException {
        Marshaller marshaller = JAXBContext.newInstance(jaxbObj.getClass()).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        java.io.StringWriter sw = new java.io.StringWriter();
        marshaller.marshal(jaxbObj, sw);
        return sw.toString();
    }
    
//    public static void saveToFile(Object jaxbObj, File file) throws JAXBException, IOException {
//        String xml = jaxbMarshalToString(jaxbObj);
//        FileWriter fw = null;
//        try {
//            fw = new FileWriter(file);
//            fw.write(xml);
//        } finally {
//            CloseUtils.softClose(fw);
//        }
//    }
}
