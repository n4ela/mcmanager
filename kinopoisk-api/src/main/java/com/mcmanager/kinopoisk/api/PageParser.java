package com.mcmanager.kinopoisk.api;

import mcmanager.kinopoisk.info.Actor;
import mcmanager.kinopoisk.info.Movie;
import mcmanager.kinopoisk.info.Thumb;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ivanov D.V. (ivanovdw@gmail.com)
 *         Date 30.11.14.
 */
public class PageParser {

    private static final int TIMEOUT = 15000;

    private static final char NBSP = (int)160;
    private static final String SPECIAL_DEF = "\u0097";
    private static final String SPECIAL_DOT = "\u0085";

    public Movie parseMovie(String url) throws IOException {
        Movie movie = new Movie();
        Connection connection = JSoupUtils.goToUrl(url).timeout(TIMEOUT);
        Document document = connection.get();
        movie.setTitle(document.select("#headerFilm").text());
        movie.setOriginaltitle(document.select("span[itemprop=\"alternativeHeadline\"]").text());

        Map<String, String> infoMap = getInfoMap(document, url);
        movie.setDirector(infoMap.get(FilmKey.DIRECTOR.getValue()));
        movie.setTagline(infoMap.get(FilmKey.TAGLINE.getValue()).replaceAll("-", ""));
        movie.setCredits(infoMap.get(FilmKey.CREDITDS.getValue()));
        movie.setGenre(infoMap.get(FilmKey.GENRE.getValue()));
        movie.setMpaa(infoMap.get(FilmKey.MPAA.getValue()));
        movie.setYear(infoMap.get(FilmKey.YEAR.getValue()));

        String plot = removeSpecialChar(document.getElementsByClass("brand_words").first().text());
        movie.setPlot(plot);
        movie.setOutline(plot);

        Element rating = document.select(".rating_link").first();
        movie.setRating(removeSpecialChar(rating.select(".rating_ball").text()));
        movie.setVotes(removeSpecialChar(rating.select(".ratingCount").text()));
        Elements bigImage = document.getElementsByClass("popupBigImage");
        if (!bigImage.isEmpty()) {
            Thumb thumb = new Thumb();
            System.out.println("bigImage.first(): " + bigImage.first());
            String img = bigImage.first().attr("onclick");
            img = img.replace("openImgPopup('", "").replace("'); return false", "");
            thumb.setValue("http://st.kp.yandex.net/" + img);
            movie.getThumb().add(thumb);
        }
        movie.getThumb().addAll(getPoster(url));
        movie.getThumb().addAll(getCovers(url));
        movie.getActor().addAll(getActorInfo(url));



        try {
            System.out.println(JaxbUtils.jaxbMarshalToString(movie));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return movie;
    }

    private List<Actor> getActorInfo(String urlFilm) throws IOException {
        //URL где хранится список актеров
        Connection connection = JSoupUtils.goToUrl(urlFilm, "cast").timeout(TIMEOUT);
        //Получаем страницу
        Document document = connection.get();
        //block_left это список актеров, режесеров и т.д в одной куче.
        Elements blockLeft = document.select(".block_left").first().children();
        //Список в который мы должны занести актеров
        List<Actor> actorList = new ArrayList<Actor>();
        //Флаг того что дальше идет список актеров
        boolean startActer = false;
        for (Element elementBlockLeft : blockLeft) {
            if (!startActer && "a".equals(elementBlockLeft.tagName()) && "actor".equals(elementBlockLeft.attr("name"))) {
                System.out.println("TEST: " + elementBlockLeft.attr("name"));
                startActer = true;
                continue;
            }
            if (startActer) {
                System.out.println(elementBlockLeft.tagName());
                if ("a".equals(elementBlockLeft.tagName())) {
                    break;
                } else if (actorList.size() < ACTOR_SIZE) {
                    Elements actorElements = elementBlockLeft.select(".actorInfo");
                    if (!actorElements.isEmpty()) {
                        System.out.println("EEEE");
                        Element actorElement = actorElements.first();
                        Actor actor = new Actor();
                        String name = actorElement.getElementsByClass("name").select("a[href]").text();
                        String roleSrc = actorElement.getElementsByClass("role").text().replace("... ", "");
                        String src = actorElement.getElementsByClass("photo").select("img").attr("src");
                        String title = actorElement.getElementsByClass("photo").select("img").attr("title");
                        String thumb = src.replace("/images/spacer.gif", title.replace("sm_", ""));
                        actor.setName(removeSpecialChar(name));
                        actor.setRole(removeSpecialChar(roleSrc).replace("...", ""));
                        actor.setThumb(thumb);
                        actorList.add(actor);
                    }
                }
            }
        }
        return actorList;
    }

    private static final int ACTOR_SIZE = 10;

    private Map<String, String> getInfoMap(Document document, String url) {
        Map<String, String> infoMap = new HashMap<String, String>();
        Elements info = document.getElementsByClass("info").select("tr");
        for (Element element : info) {
            Element elementKey = element.select("td").get(0);
            Element elementValue = element.select("td").get(1);
            String values[] = elementValue.text().split(",");
            StringBuilder value = new StringBuilder();
            for (int i = 0; i < values.length; ++i) {
                String valueFormat = removeSpecialChar(values[i]);
                if (!valueFormat.startsWith("...")) {
                    if (i != 0) {
                        value.append("/");
                    }
                    value.append(valueFormat);
                }
            }
            infoMap.put(elementKey.text().trim(), value.toString());
        }
        return infoMap;
    }

    private static List<Thumb> getPoster(String urlFilm) throws IOException {
        Connection connection = JSoupUtils.goToUrl(urlFilm, "posters").timeout(TIMEOUT);
        //Получаем страницу
        Document document = connection.get();
        List<Thumb> thumbList = new ArrayList<Thumb>();
        for (Element element : document.getElementsByClass("fotos").select("a[href] img")) {
            String images = element.attr("src").replace("sm_", "");
            //Небольшой хак что бы отрезать лишнии картинки(те для которых нету большого разрешения
            Thumb thumb = new Thumb();
            thumb.setValue(images);
            thumbList.add(thumb);
        }
        return thumbList;
    }

    private static List<Thumb> getCovers(String urlFilm) throws IOException {
        Connection connection = JSoupUtils.goToUrl(urlFilm, "posters").timeout(TIMEOUT);
        //Получаем страницу
        Document document = connection.get();
        int page = 1;
        int coverCount = getConverCount(document);
        List<Thumb> thumbList = new ArrayList<Thumb>();
        while (page <= coverCount) {
            for (Element element : document.getElementsByClass("fotos").select("a[href] img")) {
                String images = element.attr("src").replace("sm_", "");
                //Небольшой хак что бы отрезать лишнии картинки(те для которых нету большого разрешения
                Thumb thumb = new Thumb();
                thumb.setValue(images);
                thumbList.add(thumb);
            }
            if (page <= coverCount) {
                page++;
                connection = JSoupUtils.goToUrl(urlFilm, "posters/page/" + page).timeout(TIMEOUT);
                document = connection.get();
            }
        }
        return thumbList;

    }

    private static Integer getConverCount(Document document) {
        Elements list = document.getElementsByClass("list");
        if (!list.isEmpty()) {
            String url = list.first().select("li").last().select("a").first().attr("href");
            int end = url.lastIndexOf("/");
            int start = url.substring(0, end).lastIndexOf("/");
            return Integer.valueOf(url.substring(start + 1, end));
        }
        return 1;

    }

    private String removeSpecialChar(String str) {
        return !StringUtils.isEmpty(str) ? str.replace(SPECIAL_DEF, "-").replace(SPECIAL_DOT, ".")
                .replace(NBSP, ' ').replaceAll("«|»", "\"").trim() : "";
    }

}
