package mcmanager.monitor.task;

import java.util.List;

import mcmanager.dao.DaoFactory;
import mcmanager.data.Distribution;
import mcmanager.data.StatusEnum;
import mcmanager.exception.CoreException;
import mcmanager.log.LogEnum;
import mcmanager.monitor.handler.TorrentHandler;

import org.apache.commons.logging.Log;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class DistributionMonitor extends QuartzJobBean {

    private static final Log log = LogEnum.MONITOR.getLog();
    
    @Override
    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {
        log.info("Начало выполнения задачи отслеживания обновления раздач");
        List<Distribution> distributions = 
                DaoFactory.getInstance().getDistributionDao().getDistributionByStatus(StatusEnum.TRACK_ON);
        System.out.println("distributions: " + distributions);
        log.debug("Найдено " + distributions.size() + " раздач со статусом " + StatusEnum.TRACK_ON.getDesc());
        try {
            for (Distribution distribution : distributions) {
                new TorrentHandler().executeNewDistribution(distribution);
                distribution.setStatus(StatusEnum.DOWNLOAD.getStatus());
                DaoFactory.getInstance().getDistributionDao().updateDistribution(distribution);
            }
            log.info("Задача отслеживания обновления раздач завершена");
        } catch (CoreException e) {
            log.fatal("Ошибка при выполнение задачи отслеживания обновления раздач", e);
        }
    }

}