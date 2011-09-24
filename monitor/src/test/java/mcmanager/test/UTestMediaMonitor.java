package mcmanager.test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import junitx.util.PrivateAccessor;
import mcmanager.dao.DaoFactory;
import mcmanager.data.StatusEnum;
import mcmanager.monitor.task.MediaMonitor;
import mcmanager.utils.CloseUtils;

import org.custommonkey.xmlunit.XMLTestCase;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.xml.sax.SAXException;

@SpringApplicationContext({"core-config.xml", "monitor-config.xml"})
public class UTestMediaMonitor extends UnitilsJUnit4 {

    @Test
    @DataSet("UTestMediaMonitorFilm.xml")
    public void testExecuteFilms() throws Throwable {
        MediaMonitor monitor = new MediaMonitor();
        PrivateAccessor.invoke(monitor, "executeInternal", 
                new Class[] {JobExecutionContext.class}, new Object[]{null});

        assertEquals(StatusEnum.OFF.getStatus(), 
                (int)DaoFactory.getInstance().getDistributionDao().getDistribution(3).getStatus());
        File filmFile = new File("src/test/xbmc/mediaFolder/Крылья, ноги и хвосты");
        for (String file : filmFile.list()) {
            if (file.equals("Крылья, ноги и хвосты.nfo")) {
                XmlUnit.equalsXml("src/test/xbmc/nfo/Крылья, ноги и хвосты.nfo", 
                        "src/test/xbmc/mediaFolder/Крылья, ноги и хвосты/Крылья, ноги и хвосты.nfo");
            }

            //Удаляем только эти два файла
            if (file.equals("Крылья, ноги и хвосты.nfo") || file.equals("Крылья, ноги и хвосты.avi"))
                assertTrue(new File(filmFile, file).delete());
        }
        assertTrue(filmFile.delete());
    }

    @Test
    @DataSet("UTestMediaMonitorSerials.xml")
    public void testExecuteSerials() throws Throwable {
        MediaMonitor monitor = new MediaMonitor();
        PrivateAccessor.invoke(monitor, "executeInternal", 
                new Class[] {JobExecutionContext.class}, new Object[]{null});

        assertEquals(StatusEnum.TRACK_ON.getStatus(), 
                (int)DaoFactory.getInstance().getDistributionDao().getDistribution(4).getStatus());

        File filmFile = new File("src/test/xbmc/mediaFolder/Дело было на Кубани");
        Set<String> serialList = createSerial();
        for (String file : filmFile.list()) {
            if (serialList.contains(file)) {
                if (file.endsWith(".nfo")) {
                    XmlUnit.equalsXml("src/test/xbmc/nfo/" + file, filmFile.getPath() + "/" + file);
                }
                assertTrue(new File(filmFile, file).delete());
            }
        }
        assertTrue(filmFile.delete());
    }

    //TODO Этот класс можно заюзать в kinopoisk-api
    private static class XmlUnit extends XMLTestCase {

        public static void equalsXml(String expectedPath, String actualPath) throws SAXException, IOException {
            Reader expected = null;
            Reader actual = null; 
            try {
                expected = new FileReader(new File(expectedPath));
                actual = new FileReader(new File(actualPath));
                XmlUnit xmlUnit = new XmlUnit();
                xmlUnit.assertXMLEqual(expected, actual);
            } finally {
                CloseUtils.softClose(expected);
                CloseUtils.softClose(actual);
            }
        }
    }        

    private Set<String> createSerial() {
        Set<String> set = new HashSet<String>();
        set.add("tvshow.nfo");
        set.add("Дело было на Кубани.s01eE01.avi");
        set.add("Дело было на Кубани.s01eE01.nfo");
        set.add("Дело было на Кубани.s01eE02.avi");
        set.add("Дело было на Кубани.s01eE02.nfo");
        set.add("Дело было на Кубани.s01eE03.avi");
        set.add("Дело было на Кубани.s01eE03.nfo");
        set.add("Дело было на Кубани.s01eE04.avi");
        set.add("Дело было на Кубани.s01eE04.nfo");
        set.add("Дело было на Кубани.s01eE05.avi");
        set.add("Дело было на Кубани.s01eE05.nfo");
        set.add("Дело было на Кубани.s01eE06.avi");
        set.add("Дело было на Кубани.s01eE06.nfo");
        set.add("Дело было на Кубани.s01eE07.avi");
        set.add("Дело было на Кубани.s01eE07.nfo");
        set.add("Дело было на Кубани.s01eE08.avi");
        set.add("Дело было на Кубани.s01eE08.nfo");
        return set;
    }
}

