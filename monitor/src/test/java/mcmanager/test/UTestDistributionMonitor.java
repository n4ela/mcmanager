package mcmanager.test;

import static org.junit.Assert.assertEquals;

import java.io.File;

import junitx.util.PrivateAccessor;
import mcmanager.monitor.task.DistributionMonitor;
import mcmanager.utils.MD5Checksum;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;

@SpringApplicationContext({"core-config.xml", "monitor-config.xml"})
@DataSet("UTestDistributionMonitor.xml")
public class UTestDistributionMonitor extends UnitilsJUnit4 {

    @Test
    public void testExecuteInternal() throws Throwable {
        String expected = "1d11f537b6f5c8bf80e9fd00dc65da2e";
        DistributionMonitor monitor = new DistributionMonitor();
        PrivateAccessor.invoke(monitor, "executeInternal", 
                new Class[] {JobExecutionContext.class}, new Object[]{null});
        assertEquals(expected, MD5Checksum.getMD5Checksum("/tmp/[rutracker.org].t1931100.torrent"));
        new File("/tmp/[rutracker.org].t1931100.torrent").delete();
    }
}
