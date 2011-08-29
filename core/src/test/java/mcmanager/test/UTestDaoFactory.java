package mcmanager.test;
import java.io.File;
import java.util.Map;

import mcmanager.dao.DaoFactory;
import mcmanager.data.Distribution;
import mcmanager.data.Group;
import mcmanager.test.utils.MockObjectDb;
import mcmanager.test.utils.ReflectUtils;
import mcmanager.utils.FileUtils;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;


@SpringApplicationContext({"application-config.xml"})
@DataSet
public class UTestDaoFactory extends UnitilsJUnit4 {

    private final String testPath = FileUtils.getApplicationHome() + 
                                    File.separator + "data" + 
                                    File.separator + "dao-factory" +
                                    File.separator;
    /**
     * Тест на заполнение таблицы distribution
     * @throws Throwable 
     */
    @Test
    public void testAddInDb() throws Throwable {
        Map<Long, Distribution> distributions = MockObjectDb.loadGroup(testPath + "mock-distribution-1-exception.dat", Distribution.class);
        Map<Long, Group> groups = MockObjectDb.loadGroup(testPath + "mock-group-1-exception.dat", Group.class);
        Group groupSerial = groups.get((long)1);
        Group groupFilms = groups.get((long)2);
        Distribution distributionSerial = distributions.get((long)1);
        Distribution distributionFilms = distributions.get((long)2);
        DaoFactory.getInstance().getGroupDao().addGroup(groupFilms);
        DaoFactory.getInstance().getGroupDao().addGroup(groupSerial);
        DaoFactory.getInstance().getDistributionDao().addOrUpdateDistribution(distributionSerial);
        DaoFactory.getInstance().getDistributionDao().addOrUpdateDistribution(distributionFilms);
    }
    
    @Test
    public void testGetFromDB() throws Throwable {
        Group actualGroupSerial = DaoFactory.getInstance().getGroupDao().getGroup(3);
        Group actualGroupFilms = DaoFactory.getInstance().getGroupDao().getGroup(4);
        Distribution actualDistributionSerial =  
                DaoFactory.getInstance().getDistributionDao().getDistribution(3);
        Distribution actualDistributionFilm = 
                DaoFactory.getInstance().getDistributionDao().getDistribution(4);
        Map<Long, Distribution> distributions = MockObjectDb.loadGroup(testPath + "mock-distribution-2-exception.dat", Distribution.class);
        Map<Long, Group> groups = MockObjectDb.loadGroup(testPath + "mock-group-2-exception.dat", Group.class);
        Group expectedGroupSerial = groups.get((long)3); 
        Group expectedGroupFilms = groups.get((long)4);
        Distribution expectedDistributionSerial = distributions.get((long)3); 
        Distribution expectedDistributionFilm = distributions.get((long)4);
        
        ReflectUtils.equalsByGetMethod(expectedGroupFilms, actualGroupFilms);
        ReflectUtils.equalsByGetMethod(expectedGroupSerial, actualGroupSerial);
        ReflectUtils.equalsByGetMethod(expectedDistributionSerial, actualDistributionSerial);
        ReflectUtils.equalsByGetMethod(expectedDistributionFilm, actualDistributionFilm);
    }
    
}
