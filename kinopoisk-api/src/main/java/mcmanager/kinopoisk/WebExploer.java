package mcmanager.kinopoisk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mcmanager.kinopoisk.info.Actor;
import mcmanager.kinopoisk.info.Episodedetails;
import mcmanager.kinopoisk.info.Movie;
import mcmanager.kinopoisk.info.Thumb;
import mcmanager.kinopoisk.info.Tvshow;
import mcmanager.log.LogEnum;
import mcmanager.web.JSoupUtils;

import org.apache.commons.logging.Log;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Класс осуществляющий парсинг сайта кинопоиск
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 * Date: 04.09.2011
 */
public class WebExploer {

    private static final Log log = LogEnum.KINOPOISK.getLog();

    private static final int TIMEOUT = 15000;

    private static final String ACTOR_INFO_LINK = "19";
    /** Глубина поиска по актерам */
    private static final int ACTOR_SIZE = 10; 
    
    private static final String POSTER_LINK = "17";

    private static final String TITLE = "title";
    private static final String PLOT = "brand_words";
    private static final String ORIGINAL_NAME = "div.movie tbody tbody tbody tbody tbody td";
    private static final String SMALL_POSTER = "div.movie tbody tbody tr";
    
    private static final char NBSP = (int)160;
    private static final String SPECIAL_DEF = "\u0097";

    //TODO Потенциально в infoMap могут возникнуть nullpointer, 
    //в качестве решения можно предложить сетить значения по умолчанию
    /**
     * Парсит страницу кинопоиска 
     * @param url - url страницы
     * @return Объект {@link Movie}
     * @throws IOException
     */
    public static Movie parseMovie(String url) throws IOException {
        Movie movie = new Movie();
        Connection connection = JSoupUtils.goToUrl(url).timeout(TIMEOUT);
        Document document = connection.get();
        movie.setTitle(document.select(TITLE).first().text());
        movie.setOriginaltitle(document.select(ORIGINAL_NAME).first().text());
        Map<String, String> infoMap = getInfoMap(document, url);
        movie.setDirector(infoMap.get(FilmKey.DIRECTOR.getValue()));
        movie.setTagline(infoMap.get(FilmKey.TAGLINE.getValue()).replaceAll("«|»|-", ""));
        movie.setCredits(infoMap.get(FilmKey.CREDITDS.getValue()));
        movie.setGenre(infoMap.get(FilmKey.GENRE.getValue()));
        movie.setMpaa(infoMap.get(FilmKey.MPAA.getValue()));
        movie.setYear(infoMap.get(FilmKey.YEAR.getValue()));
        String plot = document.getElementsByClass(PLOT).first().text();
        movie.setPlot(plot.replace(SPECIAL_DEF, "-"));
        movie.setOutline(plot.replace(SPECIAL_DEF, "-"));
        String rating = document.getElementsByClass("continue").first().text().replace(NBSP, ' ');
        String[] arrayRating = rating.split("  ");
        if (arrayRating.length == 2) {
            movie.setRating(arrayRating[0]);
            movie.setVotes(arrayRating[1]);
        }
        movie.getActor().addAll(getActorInfo(url));
        movie.getThumb().addAll(getThumb(url, document));
        return movie;
    }

    /**
     * Парсит страницу кинопоиска
     * @param url - url страницы
     * @return {@link Tvshow}
     * @throws IOException
     */
    public static Tvshow parseTvShow(String url) throws IOException {
        Tvshow tvshow = new Tvshow();
        Connection connection = JSoupUtils.goToUrl(url).timeout(TIMEOUT);
        Document document = connection.get();
        tvshow.setTitle(document.select(TITLE).first().text());
        String plot = document.getElementsByClass(PLOT).first().text();
        tvshow.setPlot(plot.replace(SPECIAL_DEF, "-"));
        tvshow.setOutline(plot.replace(SPECIAL_DEF, "-"));
        tvshow.getActor().addAll(getActorInfo(url));
        tvshow.getThumb().addAll(getThumb(url, document));
        Map<String, String> infoMap = getInfoMap(document, url);
        tvshow.setCredits(infoMap.get(FilmKey.CREDITDS.getValue()));
        tvshow.setGenre(infoMap.get(FilmKey.GENRE.getValue()));
        tvshow.setMpaa(infoMap.get(FilmKey.MPAA.getValue()));
        tvshow.setTagline(infoMap.get(FilmKey.TAGLINE.getValue()).replaceAll("«|»|-", ""));
        tvshow.setYear(infoMap.get(FilmKey.YEAR.getValue()));
        String rating = document.getElementsByClass("continue").first().text().replace(NBSP, ' ');
        String[] arrayRating = rating.split("  ");
        if (arrayRating.length == 2) {
            tvshow.setRating(arrayRating[0]);
            tvshow.setVotes(arrayRating[1]);
        }
        return tvshow;
    }
    
    /**
     * Парсит страницу кинопоиска
     * @param url - url страницы
     * @param fileName - файл с именем сериала
     * @return {@link Tvshow}
     * @throws IOException
     */
    public static Episodedetails parseEpisodedetails(String url, String fileName) throws IOException {
        Episodedetails episodedetails = new Episodedetails();
        Connection connection = JSoupUtils.goToUrl(url).timeout(TIMEOUT);
        Document document = connection.get();
        episodedetails.setTitle(document.select(TITLE).first().text());
        String plot = document.getElementsByClass(PLOT).first().text();
        episodedetails.setPlot(plot.replace(SPECIAL_DEF, "-"));
        episodedetails.getActor().addAll(getActorInfo(url));
        episodedetails.getThumb().addAll(getThumb(url, document));
        Map<String, String> infoMap = getInfoMap(document, url);
        episodedetails.setCredits(infoMap.get(FilmKey.CREDITDS.getValue()));
        episodedetails.setMpaa(infoMap.get(FilmKey.MPAA.getValue()));
        
        Pattern seasonRegexp = Pattern.compile("\\.[sS]([0-9]*)[eE]([0-9]*)");
        Matcher matcher = seasonRegexp.matcher(fileName);
        if (matcher.find() && matcher.groupCount() == 2) {
            episodedetails.setSeason(matcher.group(1));
            episodedetails.setEpisode(matcher.group(2));
        } else {
            log.error("Из имени файла: " + fileName + 
                    " не удалось получить номер сезона и номер серии");
        }
        return episodedetails;
    }
    
    private static final Map<String, String> getInfoMap(Document document, String url) {
        Map<String, String> infoMap = new HashMap<String, String>();
        Elements info = document.getElementsByClass("info").select("tr");
        for (Element element : info) {
            Element elementKey = element.select("td").get(0);
            Element elementValue = element.select("td").get(1);
            if (!JSoupUtils.elemenTexttIsEmpty(elementKey) && 
                    !JSoupUtils.elemenTexttIsEmpty(elementValue)) {
                String key = elementKey.text();
                String value = elementValue.text().replace(",", " /").replace("...", "").trim();
                if (keyOrValueIsEmpty(key, value, url))
                    continue;
                String oldValue = null;
                if ((oldValue = infoMap.get(elementKey.text())) != null) {
                    log.trace("При парсинге страницы: " + url + 
                            " на странице найдены два одинаковых элемента. " +
                            "Значения итогового объекта будут переписаны." +
                            "Ключ: " + key + " старое значение: " + oldValue +
                            " Новое значене" + value);
                } else {
                    infoMap.put(key, value);
                }
            } else {
                log.trace("При парсинге страницы: " + url + " пропущен элемент " + 
                        "key: " + elementKey + " value: " + elementValue);
            }

        }
        return infoMap;
    }

    private static List<Actor> getActorInfo(String urlFilm) throws IOException {
        //URL где хранится список актеров
        String url = urlFilm.replace("level/1/film", "level/" + ACTOR_INFO_LINK + "/film");
        Connection connection = JSoupUtils.goToUrl(url).timeout(TIMEOUT);
        //Получаем страницу
        Document document = connection.get();
        //block_left это список актеров, режесеров и т.д в одной куче.
        Elements blockLeft = document.getElementsByClass("block_left").first().children();
        //Список в который мы должны занести актеров
        List<Actor> actorList = new ArrayList<Actor>();
        //Флаг того что дальше идет список актеров
        boolean startActer = false;
        for (Element elementBlockLeft : blockLeft) {
            Elements td = elementBlockLeft.select("td");
            if (td.isEmpty() && startActer) {
                Element actorElement = elementBlockLeft.getElementsByClass("actorInfo").first();
                Actor actor = new Actor();
                //Заполняем информацию по каждому актеру
                String name = actorElement.getElementsByClass("name").select("a[href]").text();
                String role = actorElement.getElementsByClass("role").text().replace("... ", "");
                String src = actorElement.getElementsByClass("photo").select("img").attr("src");
                String title = actorElement.getElementsByClass("photo").select("img").attr("title");
                String thumb = src.replace("/images/spacer.gif", title.replace("sm_", ""));
                actor.setName(name.replace(NBSP, ' ').trim());
                actor.setRole(role.replace(NBSP, ' ').trim());
                actor.setThumb(thumb);
                actorList.add(actor);
                continue;
            }
            startActer = markActorStart(td);
            
        }
        return actorList.size() > ACTOR_SIZE ? actorList.subList(0, ACTOR_SIZE) : actorList;
    }
    
    private static List<Thumb> getThumb(String urlFilm, Document oldDocument) throws IOException {
        String url = urlFilm.replace("level/1/film", "level/" + POSTER_LINK + "/film");
        Connection connection = JSoupUtils.goToUrl(url).timeout(TIMEOUT);
        //Получаем страницу
        Document document = connection.get();
        List<Thumb> thumbList = new ArrayList<Thumb>();
        for (Element element : document.getElementsByClass("fotos").select("a[href] img")) {
            String images = element.attr("src").replace("sm_", "");
            //Небольшой хак что бы отрезать лишнии картинки(те для которых нету большого разрешения
            if (images.matches("http://st.kinopoisk.ru/images/poster/[0-9]*.jpg")) {
                Thumb thumb = new Thumb();
                thumb.setValue(images);
                thumbList.add(thumb);    
            }
        }
        
        //Если ни одной картинки получить не удалось(не было в хорошем качестве)
        //То пробуем получить милипиздричную
        //Deprecated если картинки в нормальном разрешении нету пробуем получить ее с rutracker
//        if (thumbList.isEmpty()) {
//            Thumb thumb = new Thumb();
//            thumb.setValue(oldDocument.select(SMALL_POSTER).get(6).select("img").attr("src"));
//            thumbList.add(thumb);
//        }
        return thumbList;
    }
    
    private static boolean markActorStart(Elements elements) {
        boolean actorStart = false;
        for (Element td : elements)
            if (td.text().replace(NBSP, ' ').trim().equals("Актеры"))
                actorStart = true;
        return actorStart;
    }

    private static boolean keyOrValueIsEmpty(String key, String value, String url) {
        if (value.isEmpty()){
            log.trace("При парсинге страницы: " + url + 
                    "значение ключа: " + key + " пустое");
            return true;
        } else if (key.isEmpty()) {
            log.trace("При парсинге страницы: " + url + 
                    "ключ не задан");
            return true;
        }
        return false;
    }
    
}
