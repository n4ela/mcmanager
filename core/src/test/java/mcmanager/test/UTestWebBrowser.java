package mcmanager.test;

import mcmanager.exception.CoreException;
import mcmanager.log.LogEnum;
import mcmanager.web.WebBrowser;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.spring.annotation.SpringApplicationContext;

import static junit.framework.Assert.*;

@SpringApplicationContext({"core-config.xml"})
public class UTestWebBrowser extends UnitilsJUnit4 {

    @Test
    public void testGetThumb() throws CoreException {
        WebBrowser webBrowser = new WebBrowser(LogEnum.OTHER.getLog());
        webBrowser.goToUrl("http://rutracker.org/forum/viewtopic.php?t=3706661");
        assertEquals("http://i25.fastpic.ru/big/2011/0828/4e/177c7b2f34d95fa55f46bfc81e526c4e.png",
                webBrowser.getThumb());
    }
}
