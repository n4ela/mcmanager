package mcmanager.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import junitx.util.PrivateAccessor;
import mcmanager.dao.DaoFactory;
import mcmanager.data.StatusEnum;
import mcmanager.monitor.task.FileMonitor;
import mcmanager.monitor.utils.MonitorSettings;
import mcmanager.utils.ApplicationUtils;
import mcmanager.utils.CloseUtils;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;

import static junit.framework.Assert.*;

@SpringApplicationContext({"core-config.xml", "monitor-config.xml"})
@DataSet("UTestFileMonitor.xml")
public class UTestFileMonitor extends UnitilsJUnit4 {

    private String endDownloadsFolder = 
            ApplicationUtils.getApplicationHome() + File.separator + "end-downloads";
    
    private File testDir = new File(endDownloadsFolder, "test");
    
    @Test
    public void testExecuteInternal() throws Throwable {
        createTestData();
        MonitorSettings.getInstance().setDirEndDownloadTorrents(endDownloadsFolder 
                + File.separator + "test");
        
        FileMonitor monitor = new FileMonitor();
        PrivateAccessor.invoke(monitor, "executeInternal", 
                new Class[] {JobExecutionContext.class}, new Object[]{null});
        
        assertEquals(StatusEnum.PROCESSING.getStatus(),
                (int)DaoFactory.getInstance().getDistributionDao().getDistribution(4).getStatus().getStatus());
        
        assertEquals(StatusEnum.TRACK_ON.getStatus(),
                (int)DaoFactory.getInstance().getDistributionDao().getDistribution(3).getStatus().getStatus());
        
        assertEquals(true, testDir.delete());
    }

    private void createTestData() throws IOException {
        File sourceDir = new File(endDownloadsFolder);

        testDir.mkdir();

        FileInputStream src = null;
        FileOutputStream dest = null;
        FileChannel srcChannel = null;
        FileChannel destChannel = null;
        try {
            src = new FileInputStream(new File(sourceDir, "[rutracker.org].t524993.torrent"));
            dest = new FileOutputStream(new File(testDir, "[rutracker.org].t524993.torrent"));
            srcChannel = src.getChannel();
            destChannel = dest.getChannel();
            srcChannel.transferTo(0, srcChannel.size(), destChannel);
            
            src = new FileInputStream(new File(sourceDir, "[rutracker.org].t1130196.torrent"));
            dest = new FileOutputStream(new File(testDir, "[rutracker.org].t1130196.torrent"));
            srcChannel = src.getChannel();
            destChannel = dest.getChannel();
            srcChannel.transferTo(0, srcChannel.size(), destChannel);
        } finally {
            CloseUtils.softClose(src);
            CloseUtils.softClose(dest);
            CloseUtils.softClose(srcChannel);
            CloseUtils.softClose(destChannel);
        }

    }
}
