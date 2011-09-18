package mcmanager.monitor.handler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mcmanager.dao.DaoFactory;
import mcmanager.data.Distribution;
import mcmanager.data.StatusEnum;
import mcmanager.exception.CoreException;
import mcmanager.log.LogEnum;
import mcmanager.monitor.settings.MonitorSettings;
import mcmanager.monitor.utils.MessageUtils;
import mcmanager.utils.CloseUtils;
import mcmanager.utils.FileUtils;
import mcmanager.web.WebBrowser;
import mcmanager.web.WebBrowser.TorrentFile;

import org.apache.commons.logging.Log;


public class TorrentHandler {

    private static final Log logMonitorNew = LogEnum.MONITOR_NEW.getLog();
    private static final Log logMonitor = LogEnum.MONITOR.getLog();
    //TODO вынести в енум
    private static final int SEND_MAIL = 1;
    
    public void executeDistribution(Distribution distribution) throws CoreException {
        WebBrowser webBrowser = new WebBrowser(logMonitor);
        webBrowser.goToUrl(distribution.getLinkRutracker());
        String newTitle = webBrowser.getTitle();
        if (!distribution.getTitle().equals(newTitle)) {
            logMonitor.info("Раздача: " + distribution.getLinkRutracker() + " обновилась");
            downloadsTorrent(webBrowser, distribution, logMonitor);
            
            distribution.setTitle(newTitle);
            distribution.setStatus(StatusEnum.DOWNLOAD.getStatus());
            DaoFactory.getInstance().getDistributionDao().updateDistribution(distribution);
            
            sendMessage(distribution);
        } else {
            logMonitor.debug("Заголовок раздачи " + distribution.getLinkRutracker() + " не изменился");
        }
    }

    public void executeNewDistribution(Distribution distribution) throws CoreException {
        WebBrowser webBrowser = new WebBrowser(logMonitorNew);
        webBrowser.goToUrl(distribution.getLinkRutracker());
        downloadsTorrent(webBrowser, distribution,logMonitorNew);
        
        distribution.setStatus(StatusEnum.DOWNLOAD.getStatus());
        DaoFactory.getInstance().getDistributionDao().updateDistribution(distribution);
        
        sendMessage(distribution);
    }

    private void downloadsTorrent(WebBrowser webBrowser, Distribution distribution, Log log) throws CoreException {
        String torrentUrl = webBrowser.getTorrentUrl();
        log.debug("Получена ссылка на торрент файл: " + torrentUrl);
        TorrentFile torrentFile = 
                webBrowser.downloadTorrentFile(torrentUrl);
        String pathToTorrent = distribution.getGroup().getTorrentFolder() + 
                File.separator + torrentFile.getName();
        FileUtils.removeFile(pathToTorrent);
        OutputStream os = null;
        try {
            Thread.sleep(MonitorSettings.getInstance().getSleepAfterTorrentDelete());
            os = new FileOutputStream(pathToTorrent);
            os.write(torrentFile.getContent());
            log.info("Файл " + pathToTorrent + File.separator 
                    + torrentFile.getName() + " сохранен");
        } catch (FileNotFoundException e) {
            throw new CoreException("Не удалось сохранить файл: " + pathToTorrent);
        } catch (Exception e) {
            throw new CoreException(e);
        } finally {
            CloseUtils.softClose(os);
        }
    }
    
    private void sendMessage(Distribution distribution) {
        if (MonitorSettings.getInstance().getSendMail() == SEND_MAIL) {
            LogEnum.EMAIL.getLog().fatal(MessageUtils.createMessage(distribution.getTitle(), 
                                                                    distribution.getMailRegexp(), 
                                                                    distribution.getMailMessage()));
        }
    }
    
}
