package mcmanager.monitor.task;

import java.io.File;
import java.util.List;

import mcmanager.dao.DaoFactory;
import mcmanager.data.Distribution;
import mcmanager.data.StatusEnum;
import mcmanager.data.TypeDistributionEnum;
import mcmanager.log.LogEnum;
import mcmanager.monitor.utils.MessageUtils;
import mcmanager.monitor.utils.MonitorSettings;

import org.apache.commons.logging.Log;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class FileMonitor extends QuartzJobBean {

    private final static Log log = LogEnum.FILEMONITOR.getLog();

    //TODO это должен быть енум
    private static final int SEND_MAIL = 2;

    @Override
    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {
        log.info("Начало выполнения задачи отслеживания завершения закачки");
        File endDownlods = new File(MonitorSettings.getInstance().getDirEndDownloadTorrents());
        if (endDownlods.exists() && endDownlods.isDirectory()) {
            for (String file : endDownlods.list()) {
                List<Distribution> distributions = 
                        DaoFactory.getInstance().getDistributionDao().getDistributionByTorrent(file);
                for (Distribution distribution : distributions) {
                    if (distribution.getType() == TypeDistributionEnum.SHARED.getType())
                        distribution.setStatus(StatusEnum.TRACK_ON.getStatus());
                    else {
                        distribution.setStatus(StatusEnum.PROCESSING.getStatus());
                        }
                    DaoFactory.getInstance().getDistributionDao().updateDistribution(distribution);
                    log.info("Закачка " + distribution.getLinkRutracker() + " завершена");

                    sendMessage(distribution);
                }
            }
        } else {
            log.error("Директория " + endDownlods.getAbsolutePath() + " не найдена");
        }
        log.info("Завершение выполнения задачи отслеживания завершения закачки");
    }

    private void sendMessage(Distribution distribution) {
        if (MonitorSettings.getInstance().getSendMail() == SEND_MAIL) {
            LogEnum.EMAIL.getLog().fatal(MessageUtils.createMessage(distribution.getTitle(), 
                    distribution.getMailRegexp(), 
                    distribution.getMailMessage()));
        }
    }

}
