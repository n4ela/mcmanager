package mcmanager.kinopoisk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

import mcmanager.kinopoisk.exception.KinopoiskException;
import mcmanager.kinopoisk.info.Actor;
import mcmanager.kinopoisk.info.Audio;
import mcmanager.kinopoisk.info.Episodedetails;
import mcmanager.kinopoisk.info.Fanart;
import mcmanager.kinopoisk.info.Fileinfo;
import mcmanager.kinopoisk.info.Movie;
import mcmanager.kinopoisk.info.Streamdetails;
import mcmanager.kinopoisk.info.Subtitle;
import mcmanager.kinopoisk.info.Thumb;
import mcmanager.kinopoisk.info.ThumbType;
import mcmanager.kinopoisk.info.Tvshow;
import mcmanager.kinopoisk.info.Url;
import mcmanager.kinopoisk.info.Video;
import mcmanager.kinopoisk.info.Tvshow.Episodeguide;
import mcmanager.kinopoisk.utils.JaxbUtils;

import org.custommonkey.xmlunit.XMLTestCase;
import org.junit.Test;
import org.xml.sax.SAXException;

public class UTestJaxb extends XMLTestCase {

    private final String testPath = System.getProperty("catalina.home") + 
            File.separator + "data" + File.separator;

    /**
     * Тестирует объект {@link Movie}
     * @throws Exception
     */
    @Test
    public void testMovie() throws Exception {
        Movie movie = new Movie();
        movie.setTitle("Who knows");
        movie.setOriginaltitle("Who knows for real");
        movie.setSorttitle("Who knows 1");
        movie.setSet("Who knows trilogy");
        movie.setRating("6.100000");
        movie.setYear("2008");
        movie.setTop250("0");
        movie.setVotes("50");
        movie.setOutline("A look at the role of the Buckeye State in the 2004 Presidential Election.");
        movie.setPlot("A look at the role of the Buckeye State in the 2004 Presidential Election.");
        movie.setRuntime("90 min");
        movie.setThumb("http://ia.ec.imdb.com/media/imdb/01/I/25/65/31/10f.jpg");
        movie.setMpaa("Not available");
        movie.setPlaycount("0");
        movie.setWatched("false");
        movie.setId("tt0432337");
        movie.setFilenameandpath("c:\\Dummy_Movie_Files\\Movies\\...So Goes The Nation.avi");
        Fileinfo fileinfo = new Fileinfo();
        fileinfo.setStreamdetails(new Streamdetails());
        Video video = new Video();
        video.setCodec("h264");
        video.setAspect("2.35");
        video.setWidth("1920");
        video.setHeight("816");

        Audio audioEng = new Audio();
        audioEng.setCodec("ac3");
        audioEng.setLanguage("eng");
        audioEng.setChannels("6");

        Audio audioSpa = new Audio();
        audioSpa.setCodec("ac3");
        audioSpa.setLanguage("spa");
        audioSpa.setChannels("2");

        Subtitle subtitle = new Subtitle();
        subtitle.setLanguage("spa");

        fileinfo.getStreamdetails().setVideo(video);
        fileinfo.getStreamdetails().getAudio().addAll(Arrays.asList(audioEng, audioSpa));
        fileinfo.getStreamdetails().getSubtitle().addAll(Arrays.asList(subtitle));

        movie.setFileinfo(fileinfo);
        movie.setDirector("Adam Del Deo");

        Actor paul = new Actor();
        paul.setName("Paul Begala");
        paul.setRole("Himself");

        Actor george = new Actor();
        george.setName("George W. Bush");
        george.setRole("Himself");

        Actor mary = new Actor();
        mary.setName("Mary Beth Cahill");
        mary.setRole("Herself");

        Actor ed = new Actor();
        ed.setName("Ed Gillespie");
        ed.setRole("Himself");

        Actor john = new Actor();
        john.setName("John Kerry");
        john.setRole("Himself");

        movie.getActor().addAll(Arrays.asList(paul, george, mary, ed, john));

        Reader expected = null;
        Reader actual = null;
        try {
            expected = new FileReader(new File(testPath + "movie.dat"));
            actual = new StringReader(JaxbUtils.jaxbMarshalToString(movie));
            assertXMLEqual(expected, actual);
        } finally {
            expected.close();
            actual.close();
        }
    }
    
    /**
     * Тестирует объект {@link Tvshow}
     * @throws Exception
     */
    @Test
    public void testTvshow() throws Exception {
        Tvshow tvshow = new Tvshow();
        tvshow.setTitle("MythBusters");
        tvshow.setRating("9.300001");
        tvshow.setYear("0");
        tvshow.setTop250("0");
        tvshow.setSeason("-1");
        tvshow.setEpisode("143");
        tvshow.setDisplayseason("-1");
        tvshow.setDisplayepisode("-1");
        tvshow.setPlot("So exactly how hard is it to find a needle in a haystack, anyway? And can water dripping on your forehead really drive you nuts? Those are the kinds of questions, myths and urban legends that are put to the test in this humorous series to find out which ones hold water (and keep it from dripping on your forehead and driving you insane).");
        
        Thumb thumb1 = new Thumb();
        thumb1.setValue("http://thetvdb.com/banners/graphical/73388-g3.jpg");

        Thumb thumb2 = new Thumb();
        thumb2.setValue("http://thetvdb.com/banners/graphical/73388-g2.jpg");

        Thumb thumb3 = new Thumb();
        thumb3.setValue("http://thetvdb.com/banners/seasons/73388-1.jpg");
        thumb3.setType(ThumbType.SEASON);
        thumb3.setSeason("1");

        Thumb thumb4 = new Thumb();
        thumb4.setValue("http://thetvdb.com/banners/seasons/73388-0.jpg");
        thumb4.setType(ThumbType.SEASON);
        thumb4.setSeason("0");

        Thumb thumb5 = new Thumb();
        thumb5.setValue("http://thetvdb.com/banners/seasons/73388-2.jpg");
        thumb5.setType(ThumbType.SEASON);
        thumb5.setSeason("2");

        Thumb thumb6 = new Thumb();
        thumb6.setValue("http://thetvdb.com/banners/posters/73388-1.jpg");
        thumb6.setType(ThumbType.SEASON);
        thumb6.setSeason("-1");
        tvshow.getThumb().addAll(Arrays.asList(thumb1, thumb2, thumb3, thumb4, thumb5, thumb6));
        
        Thumb thumbFanart1 = new Thumb();
        thumbFanart1.setDim("1920x1080");
        thumbFanart1.setColors("|183,185,161|51,51,51|206,205,185|");
        thumbFanart1.setPreview("_cache/fanart/original/73388-6.jpg");
        thumbFanart1.setValue("fanart/original/73388-6.jpg");
        
        Thumb thumbFanart2 = new Thumb();
        thumbFanart2.setDim("1280x720");
        thumbFanart2.setColors("|255,255,240|101,141,117|129,132,123|");
        thumbFanart2.setPreview("_cache/fanart/original/73388-1.jpg");
        thumbFanart2.setValue("fanart/original/73388-1.jpg");
        Fanart fanart = new Fanart();
        fanart.setUrl("http://thetvdb.com/banners/");
        fanart.getThumb().addAll(Arrays.asList(thumbFanart1, thumbFanart2));
        tvshow.setFanart(fanart);
        
        tvshow.setMpaa("TV-PG");
        tvshow.setPlaycount("0");
        
        Url url = new Url();
        url.setCache("73388.xml");
        url.setValue("http://www.thetvdb.com/api/1D62F2F90030C444/series/73388/all/en.zip");
        Episodeguide episodeguide = new Episodeguide();
        episodeguide.setUrl(url);
        tvshow.setEpisodeguide(episodeguide);
        
        tvshow.setId("73388");
        tvshow.setGenre("Reality");
        tvshow.setPremiered("2003-01-23");
        tvshow.setStudio("Discovery");
        Actor robert = new Actor();
        robert.setName("Robert Lee");
        robert.setRole("USA Narrator");
        Actor adam = new Actor();
        adam.setName("Adam Savage");
        tvshow.getActor().addAll(Arrays.asList(robert, adam));
        
        Reader expected = null;
        Reader actual = null;
        try {
            expected = new FileReader(new File(testPath + "tvshow.dat"));
            actual = new StringReader(JaxbUtils.jaxbMarshalToString(tvshow));
            assertXMLEqual(expected, actual);
        } finally {
            expected.close();
            actual.close();
        }
    }
    
    /**
     * Тестирует объект {@link Episodedetails}
     * @throws Exception
     */
    @Test 
    public void testEpisodedetails() throws Exception {
        Episodedetails episodedetails = new Episodedetails();
        episodedetails.setTitle("My TV Episode");
        episodedetails.setRating("10.00");
        episodedetails.setSeason("2");
        episodedetails.setEpisode("1");
        episodedetails.setPlot("he best episode in the world");
        episodedetails.setThumb("http://thetvdb.com/banners/episodes/164981/2528821.jpg");
        episodedetails.setPlaycount("0");
        episodedetails.setCredits("Writer");
        episodedetails.setDirector("Mr. Vision");
        episodedetails.setAired("2000-12-31");
        episodedetails.setPremiered("2010-09-24");
        episodedetails.setStudio("Production studio or channel");
        episodedetails.setMpaa("MPAA certification");
        episodedetails.setEpbookmark("200");
        episodedetails.setDisplayseason("3");
        episodedetails.setDisplayepisode("4096");
        Actor little = new Actor();
        little.setName("Little Suzie");
        little.setRole("Pole Jumper/Dancer");
        episodedetails.getActor().add(little);
        Fileinfo fileinfo = new Fileinfo();
        Audio audio = new Audio();
        audio.setChannels("6");
        audio.setCodec("ac3");
        Video video = new Video();
        video.setAspect("1.778");
        video.setCodec("h264");
        video.setDurationinseconds("587");
        video.setHeight("720");
        video.setLanguage("eng");
        video.setLonglanguage("English");
        video.setScantype("Progressive");
        video.setWidth("1280");
        Streamdetails streamdetails = new Streamdetails();
        streamdetails.setVideo(video);
        streamdetails.getAudio().add(audio);
        fileinfo.setStreamdetails(streamdetails);
        episodedetails.setFileinfo(fileinfo);
        Reader expected = null;
        Reader actual = null;
        try {
            expected = new FileReader(new File(testPath + "episodedetails.dat"));
            actual = new StringReader(JaxbUtils.jaxbMarshalToString(episodedetails));
            assertXMLEqual(expected, actual);
        } finally {
            expected.close();
            actual.close();
        }
    }
}
