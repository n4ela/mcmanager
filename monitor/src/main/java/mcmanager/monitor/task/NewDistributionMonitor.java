package mcmanager.monitor.task;

import java.util.List;

import mcmanager.dao.DaoFactory;
import mcmanager.data.Distribution;
import mcmanager.data.StatusEnum;
import mcmanager.exception.CoreException;
import mcmanager.log.LogEnum;
import mcmanager.monitor.task.handler.TorrentHandler;

import org.apache.commons.logging.Log;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class NewDistributionMonitor extends QuartzJobBean {

    private static final Log log = LogEnum.MONITOR_NEW.getLog();

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("Начало выполнения задачи отслеживания новых торрент файлов");
        List<Distribution> distributions = 
                DaoFactory.getInstance().getDistributionDao().getDistributionByStatus(StatusEnum.NEW);
        log.debug("Найдено " + distributions.size() + " раздач со статусом " + StatusEnum.NEW.getDesc());
        try {
            for (Distribution distribution : distributions) {
                new TorrentHandler().executeNewDistribution(distribution);
            }
            log.info("Задача отслеживания новых торрент файлов успешно завершена");
        } catch (CoreException e) {
            log.fatal("Ошибка при выполнение задачи отслеживания новых торрент файлов", e);
        }
    }
}
