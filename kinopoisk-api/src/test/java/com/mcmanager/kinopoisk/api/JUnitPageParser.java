package com.mcmanager.kinopoisk.api;

import mcmanager.kinopoisk.info.Movie;
import org.custommonkey.xmlunit.XMLTestCase;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.*;

/**
 * @author Ivanov D.V. (ivanovdw@gmail.com)
 *         Date 01.12.14.
 */
public class JUnitPageParser extends XMLTestCase {

    @Test
    public void testFilmParser() throws IOException, SAXException, JAXBException {
        Movie movie = new PageParser().parseMovie("http://www.kinopoisk.ru/film/468770");
        //К сожалению заполнения рейтинга протестировать нельзя т.к. он все время меняется
        movie.setRating("7.570");
        movie.setVotes("463");
        try (Reader expected = new InputStreamReader(getClass().getResourceAsStream("/Аргентина. Интервью с мертвым наркодилером.xml"));
             Reader actual = new StringReader(JaxbUtils.jaxbMarshalToString(movie))) {
            assertXMLEqual(expected, actual);
        }
    }

    @Test
    public void testFilmParser2() throws IOException, SAXException, JAXBException {
        Movie movie = new PageParser().parseMovie("http://www.kinopoisk.ru/film/263531/");
        //К сожалению заполнения рейтинга протестировать нельзя т.к. он все время меняется
        movie.setRating("7.570");
        movie.setVotes("463");
        try (Reader expected = new InputStreamReader(getClass().getResourceAsStream("/Мстители.xml"));
             Reader actual = new StringReader(JaxbUtils.jaxbMarshalToString(movie))) {
            assertXMLEqual(expected, actual);
        }
    }
}
