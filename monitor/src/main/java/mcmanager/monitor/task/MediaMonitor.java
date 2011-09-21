package mcmanager.monitor.task;

import static mcmanager.data.TypeDistributionEnum.DVD;
import static mcmanager.data.TypeDistributionEnum.FILMS;
import static mcmanager.data.TypeDistributionEnum.SERIALS;

import java.io.File;
import java.io.IOException;
import java.util.List;

import mcmanager.dao.DaoFactory;
import mcmanager.data.Distribution;
import mcmanager.data.StatusEnum;
import mcmanager.exception.CoreException;
import mcmanager.kinopoisk.WebExploer;
import mcmanager.kinopoisk.info.Movie;
import mcmanager.log.LogEnum;

import org.apache.commons.logging.Log;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class MediaMonitor extends QuartzJobBean  {

    private static final Log log = LogEnum.MEDIA_MONITOR.getLog();

    @Override
    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {
        log.info("Начало выполнения задачи обработка медиатеки");
        List<Distribution> distributions = DaoFactory.getInstance().getDistributionDao()
                .getDistributionByStatus(StatusEnum.PROCESSING);
        log.debug("Найдено " + distributions.size() + " для обоаботки");
        for (Distribution distribution : distributions) {
            int type = distribution.getType();
            try {
                if (type == FILMS.getType()) {
                    executeFilms(distribution);
                } else if (type == SERIALS.getType()) {
                    executeSerials(distribution);
                } else if (type == DVD.getType()) {
                    executeDvd(distribution);
                } else {
                    log.error("Не известный тип для обработки: "+ type);
                } 
            } catch (CoreException e) {
                log.error("Ошибка при обработки медиатеки", e);
            }
        }
        log.info("Задача по обработки медиатеки успешно завершена");

    }

    private void executeDvd(Distribution distribution) {
        // TODO Auto-generated method stub

    }

    private void executeSerials(Distribution distribution) {
        // TODO Auto-generated method stub

    }

    private void executeFilms(Distribution distribution) throws CoreException {
        log.debug("Обработка фильма");
        try {
            Movie movie = WebExploer.parseMovie(distribution.getLinkKinoposk());
            File mediFolder = new File(distribution.getGroup().getMediaFolder());
            if (!mediFolder.exists() || !mediFolder.isDirectory())
                throw new CoreException("Каталог " + mediFolder.getAbsolutePath() + " не найден");
            File dirMovie = new File(mediFolder, movie.getTitle()); 
            if (dirMovie.exists()) {
                log.debug("Каталог: " + dirMovie + " уже существует"); 
            } else {
                if (dirMovie.mkdir()) {
                    log.debug("Каталог " + dirMovie + " создан");
                } else {
                    throw new CoreException("Не удалось создать каталог " + mediFolder.getAbsolutePath() 
                            + File.separator + movie.getTitle());
                }
            }
//            distribution.getGroup().getDownloadFolder()
        } catch (IOException e) {
            throw new CoreException(e);
        }

    }

}
