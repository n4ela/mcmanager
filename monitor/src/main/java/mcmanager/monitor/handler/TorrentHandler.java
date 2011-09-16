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

    private static final Log log = LogEnum.MONITOR_NEW.getLog();

    public void execute(Distribution distribution) throws CoreException {
        WebBrowser webBrowser = new WebBrowser(log);
        webBrowser.goToUrl(distribution.getLinkRutracker());
        String title = webBrowser.getTitle();
        System.out.println("HELLO");
        if (!distribution.getTitle().equals(title)) {
            log.info("Раздача: " + distribution.getLinkRutracker() + " обновилась");
            log.debug("Старый заголовок: " + distribution.getTitle() 
                    + " новый заголовок: " + title);
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
        } else {
            log.trace("Заголовок раздачи не изменился");
        }
    }
}
