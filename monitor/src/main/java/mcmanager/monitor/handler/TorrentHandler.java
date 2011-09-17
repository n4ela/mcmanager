package mcmanager.monitor.handler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import mcmanager.data.Distribution;
import mcmanager.exception.CoreException;
import mcmanager.log.LogEnum;
import mcmanager.monitor.settings.MonitorSettings;
import mcmanager.utils.CloseUtils;
import mcmanager.utils.FileUtils;
import mcmanager.web.WebBrowser;
import mcmanager.web.WebBrowser.TorrentFile;

import org.apache.commons.logging.Log;


public class TorrentHandler {

    private static final Log logMonitorNew = LogEnum.MONITOR_NEW.getLog();
    private static final Log logMonitor = LogEnum.MONITOR.getLog();

    public void executeDistribution(Distribution distribution) throws CoreException {
        WebBrowser webBrowser = new WebBrowser(logMonitor);
        webBrowser.goToUrl(distribution.getLinkRutracker());
        if (!distribution.getTitle().equals(webBrowser.getTitle())) {
            logMonitor.info("Раздача: " + distribution.getLinkRutracker() + " обновилась");
            downloadsTorrent(webBrowser, distribution, logMonitor);
        } else {
            logMonitor.debug("Заголовок раздачи " + distribution.getLinkRutracker() + " не изменился");
        }
    }

    public void executeNewDistribution(Distribution distribution) throws CoreException {
        WebBrowser webBrowser = new WebBrowser(logMonitorNew);
        webBrowser.goToUrl(distribution.getLinkRutracker());
        downloadsTorrent(webBrowser, distribution,logMonitorNew);
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
}
