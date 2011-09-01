package mcmanager.kinopoisk.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import mcmanager.kinopoisk.exception.KinopoiskException;

public class JaxbUtils {
    public static String jaxbMarshalToString(Object jaxbObj) throws KinopoiskException {
        try {
            Marshaller marshaller =JAXBContext.newInstance(jaxbObj.getClass()).createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            java.io.StringWriter sw = new java.io.StringWriter();
            marshaller.marshal(jaxbObj, sw);
            return sw.toString();
        } catch (JAXBException jaxbex) {
            throw new KinopoiskException(jaxbex);
        }
    }
}
