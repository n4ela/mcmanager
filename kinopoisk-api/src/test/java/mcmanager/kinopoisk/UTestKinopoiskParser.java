package mcmanager.kinopoisk;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.bind.JAXBException;

import mcmanager.kinopoisk.utils.JaxbUtils;

import org.custommonkey.xmlunit.XMLTestCase;
import org.junit.Test;
import org.xml.sax.SAXException;

public class UTestKinopoiskParser extends XMLTestCase {
    private final String testPath = System.getProperty("catalina.home") + 
            File.separator + "data" + File.separator;
    
    @Test
    public void testFilmParser() throws IOException, SAXException, JAXBException {
        Reader expected = null;
        Reader actual = null;
        try {
            expected = new FileReader(new File(testPath + 
                    "Аргентина. Интервью с мертвым наркодилером.nfo"));
            actual = new StringReader(JaxbUtils.jaxbMarshalToString(
                    WebExploer.parseMovie("http://www.kinopoisk.ru/level/1/film/468770/")));
            assertXMLEqual(expected, actual);
        } finally {
            if (expected != null)
                expected.close();
            if (actual != null)
                actual.close();
        }
    }
    
    @Test
    public void testSerialParser() throws IOException, SAXException, JAXBException {
        Reader expected = null;
        Reader actual = null;
        try {
            expected = new FileReader(new File(testPath + 
                    "Блудливая Клифорния.nfo"));
            actual = new StringReader(JaxbUtils.jaxbMarshalToString(
                    WebExploer.parseTvShow("http://www.kinopoisk.ru/level/1/film/394375/")));
            assertXMLEqual(expected, actual);
        } finally {
            if (expected != null)
                expected.close();
            if (actual != null)
                actual.close();
        }
    }
    
    @Test
    public void testEpisodeParser() throws IOException, SAXException, JAXBException {
        Reader expected = null;
        Reader actual = null;
        String fileName = "Блудливая Клифорния.s01e05.nfo"; 
        try {
            expected = new FileReader(new File(testPath + fileName));
            actual = new StringReader(JaxbUtils.jaxbMarshalToString(
                    WebExploer.parseEpisodedetails(
                            "http://www.kinopoisk.ru/level/1/film/394375/", fileName)));
            assertXMLEqual(expected, actual);
        } finally {
            if (expected != null)
                expected.close();
            if (actual != null)
                actual.close();
        }
    }


}
