package mcmanager.test;
import static org.junit.Assert.assertEquals;

import java.io.File;

import junitx.util.PrivateAccessor;
import mcmanager.monitor.task.NewDistributionMonitor;
import mcmanager.utils.MD5Checksum;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;


@SpringApplicationContext({"core-config.xml", "monitor-config.xml"})
@DataSet("UTestNewDistributionMonitor.xml")
public class UTestNewDistributionMonitor extends UnitilsJUnit4 {
    
    @Test
    public void testExecuteInternal() throws Throwable {
        String expected = "bb71b0e6d873fbcfc202bf8c5c8666ef";
        NewDistributionMonitor monitor = new NewDistributionMonitor();
        PrivateAccessor.invoke(monitor, "executeInternal", 
                new Class[] {JobExecutionContext.class}, new Object[]{null});
        assertEquals(expected, MD5Checksum.getMD5Checksum("/tmp/[rutracker.org].t524993.torrent"));
        new File("/tmp/[rutracker.org].t524993.torrent").delete();
    }
}
