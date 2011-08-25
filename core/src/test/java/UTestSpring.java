import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByName;

import rmanager.dao.DaoFactory;
import rmanager.dao.FilmDao;
import rmanager.data.FilmData;


@SpringApplicationContext({"application-config.xml"})
@DataSet
public class UTestSpring extends UnitilsJUnit4 {
    
    @Test 
    public void UTestSpring() {
        FilmData film = new FilmData();
    }
    
}
