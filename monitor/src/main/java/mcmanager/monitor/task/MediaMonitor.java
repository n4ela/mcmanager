package mcmanager.monitor.task;

import mcmanager.dao.DaoFactory;
import mcmanager.data.Distribution;
import mcmanager.data.StatusEnum;
import mcmanager.exception.CoreException;
import mcmanager.kinopoisk.WebExploer;
import mcmanager.kinopoisk.info.Episodedetails;
import mcmanager.kinopoisk.info.Movie;
import mcmanager.kinopoisk.info.Thumb;
import mcmanager.kinopoisk.info.Tvshow;
import mcmanager.kinopoisk.utils.JaxbUtils;
import mcmanager.log.LogEnum;
import mcmanager.monitor.utils.FilmTypeEnum;
import mcmanager.monitor.utils.SymbolicLinkUtils;
import mcmanager.utils.MessageUtils;
import mcmanager.utils.TorrentInfo;
import mcmanager.web.WebBrowser;
import org.apache.commons.logging.Log;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static mcmanager.data.TypeDistributionEnum.*;

public class MediaMonitor extends QuartzJobBean  {

    private static final Log log = LogEnum.MEDIA_MONITOR.getLog();

    @Override
    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {
        synchronized (log) {
            log.info("Начало выполнения задачи обработка медиатеки");
            List<Distribution> distributions = DaoFactory.getInstance().getDistributionDao()
                    .getDistributionByStatus(StatusEnum.PROCESSING);
            log.debug("Найдено " + distributions.size() + " для обработки");
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
    }

    private void executeDvd(Distribution distribution) {
        // TODO Auto-generated method stub

    }

    private void executeSerials(Distribution distribution) throws CoreException {
        log.debug("Обработка сериала");
        try {
            //Получаем информацию о сериале с сайта кинопоиск
            Tvshow tvshow = WebExploer.parseTvShow(distribution.getLinkKinoposk());
            if (tvshow.getThumb() == null || tvshow.getThumb().size() == 0) {
                WebBrowser webBrowser = new WebBrowser(log);
                webBrowser.goToUrl(distribution.getLinkRutracker());
                String thumbStr = webBrowser.getThumb();
                if (!thumbStr.isEmpty()) {
                    Thumb thumb = new Thumb();
                    thumb.setValue(thumbStr);
                    tvshow.getThumb().add(thumb);
                }
            }

            //Сам torrent файл который был скачен с рутрекера
            File torrentFile = new File(distribution.getGroup().getTorrentFolder() + 
                    File.separator + distribution.getTorrent());
            //Если его нету кидаем ошибку
            if (!torrentFile.exists())
                throw new CoreException("Не найден торрент файл: " + torrentFile);

            //Директория xbmc в которой будут хранится символические ссылки на фильм
            //Директория имеет вид "каталог_указанный_в_группе_к_фильму/полное_название_фильм"
            File dirMovie = 
                    createFilmFolder(distribution.getGroup().getMediaFolder(), tvshow.getTitle());

            //Получаем список файлов из *.torrent
            TorrentInfo torrentInfo = new TorrentInfo(torrentFile);
            for (String filmFile : torrentInfo.getInfo()) {
                if (FilmTypeEnum.matcher(filmFile)) {
                    log.debug("Обработка файла: " + filmFile);

                    File source = new File(distribution.getGroup().getDownloadFolder(), filmFile);
                    if (!source.exists()) {
                        log.error("Не найден файл: " + source);
                        continue;
                    }

                    //Получаем расширение файла
                    int dotPos = filmFile.lastIndexOf(".");
                    if (dotPos == -1) {
                        log.warn("Не удалось определить расширение файла " + filmFile);
                        continue;
                    }
                    String fileExtension = filmFile.substring(dotPos);
                    log.debug("У файла: " + filmFile + " получено расширение: " + fileExtension);


                    //Получаем номер эпизода по названию файла в торренте
                    String episode = null;
                    try {
                        episode = MessageUtils.parseEpisode(filmFile, distribution.getRegexpSerialNumber());
                        log.info("Из файла " + filmFile + " получен номер серии " + episode);
                    } catch (CoreException e) {
                        //TODO Удалить
                        e.printStackTrace();
                    }

                    //Генерируем номер сериала согласно xbmc формату (.s01e01)
                    String serialNumber = ".s" + MessageUtils.prepareNumber(String.valueOf(distribution.getSeasonNumber())) + "e" + episode;

                    //Создаем символическую ссылку на серию/субтитры и.т.д
                    File link = new File(dirMovie, tvshow.getTitle() + serialNumber  + fileExtension);
                    if (!link.exists())
                        SymbolicLinkUtils.createSymbolicLink(link, source.getAbsoluteFile());
                    log.info("Создана символическая ссылка: " + link);

                    //Если файл является видео файлом то создаем еще и nfo файл для серии
                    if (filmFile.endsWith(FilmTypeEnum.AVI.getType()) ||
                            filmFile.endsWith(FilmTypeEnum.MKV.getType()) ||
                                filmFile.endsWith(FilmTypeEnum.MPG.getType())) {
                        Episodedetails episodedetails = WebExploer.parseEpisodedetails(distribution.getLinkKinoposk(), link.getName());
                        File nfoFile = new File(dirMovie, tvshow.getTitle() + serialNumber  + ".nfo");
                        JaxbUtils.saveToFile(episodedetails, nfoFile);
                        log.info("Сохранение информации о серии в файл: " + nfoFile + " прошло успешно");
                    }
                } else {
                    log.debug("Файл пропущен при обработке: " + filmFile);
                }
            }

            //Создаем корневой файл с информацией о сериале
            File fileInfo = new File(dirMovie, "tvshow.nfo");
            if (!fileInfo.exists()) {
                JaxbUtils.saveToFile(tvshow, fileInfo);
                log.info("Сохранение информации о сериале в файл: " + fileInfo + " прошло успешно");
            }
            //[2]

            log.info("Смена статуса на: " + StatusEnum.TRACK_ON.getStatus());
            distribution.setStatus(StatusEnum.TRACK_ON);
            DaoFactory.getInstance().getDistributionDao().updateDistribution(distribution);
        } catch (IOException e) {
            throw new CoreException(e);
        } catch (JAXBException e) {
            throw new CoreException(e);
        }
    }

    private void executeFilms(Distribution distribution) throws CoreException {
        log.debug("Обработка фильма");
        try {
            Movie movie = WebExploer.parseMovie(distribution.getLinkKinoposk());
            if (movie.getThumb() == null || movie.getThumb().size() == 0) {
                WebBrowser webBrowser = new WebBrowser(log);
                webBrowser.goToUrl(distribution.getLinkRutracker());
                String thumbStr = webBrowser.getThumb();
                if (!thumbStr.isEmpty()) {
                    Thumb thumb = new Thumb();
                    thumb.setValue(thumbStr);
                    movie.getThumb().add(thumb);
                }
            }
            //[1] Создаем каталог == название фильма
            File dirMovie = 
                    createFilmFolder(distribution.getGroup().getMediaFolder(), movie.getTitle());
            //[1]

            //[2] Создание символических ссылок
            //Сам torrent файл
            File torrentFile = new File(distribution.getGroup().getTorrentFolder() + 
                    File.separator + distribution.getTorrent());
            if (!torrentFile.exists())
                throw new CoreException("Не найден торрент файл: " + torrentFile);
            //Получаем список файлов из *.torrent
            TorrentInfo torrentInfo = new TorrentInfo(torrentFile);
            for (String filmFile : torrentInfo.getInfo()) {
                if (FilmTypeEnum.matcher(filmFile)) {
                    log.debug("Обработка файла: " + filmFile);

                    File source = new File(distribution.getGroup().getDownloadFolder(), filmFile);
                    if (!source.exists()) {
                        log.error("Не найден файл: " + source);
                        continue;
                    }

                    int dotPos = filmFile.lastIndexOf(".");
                    if (dotPos == -1) {
                        log.warn("Не удалось определить расширение файла " + filmFile);
                        continue;
                    }

                    String fileExtension = filmFile.substring(dotPos);
                    log.debug("У файла: " + filmFile + " получено расширение: " + fileExtension);

                    //Создаем символическую ссылку


                    File link = new File(dirMovie, movie.getTitle() + fileExtension);
                    if (!link.exists())
                        SymbolicLinkUtils.createSymbolicLink(link, source.getAbsoluteFile());
                    log.info("Создана символическая ссылка: " + link);
                } else {
                    log.debug("Файл пропущен при обработке: " + filmFile);
                }
            }
            //[2]

            //[3] Сохранение информации о фильме в nfo файл
            File fileInfo = new File(dirMovie, movie.getTitle() + ".nfo");
            if (!fileInfo.exists()) {
                JaxbUtils.saveToFile(movie, fileInfo);
                log.info("Сохранение информации о фильме в файл: " + fileInfo + " прошло успешно");
            }
            //[3]

            log.info("Смена статуса на: " + StatusEnum.OFF.getStatus());
            distribution.setStatus(StatusEnum.OFF);
            DaoFactory.getInstance().getDistributionDao().updateDistribution(distribution);
        } catch (IOException e) {
            throw new CoreException(e);
        } catch (JAXBException e) {
            throw new CoreException(e);
        }

    }


    private File createFilmFolder(String mediaFolder, String filmName) throws CoreException {
        //Каталог в котором будет лежать информация о фильмах данной категории(группы)
        File mediFolder = new File(mediaFolder);
        //Пропуем создать каталог если его еще не существует
        if (!mediFolder.exists() || !mediFolder.isDirectory())
            throw new CoreException("Каталог \"" + mediFolder.getAbsolutePath() + "\" не найден");

        //Каталог для конкретного фильма
        File dirMovie = new File(mediFolder, filmName); 
        if (dirMovie.exists()) {
            log.debug("Каталог: \"" + dirMovie + "\" уже существует"); 
        } else {
            if (dirMovie.mkdir()) {
                log.debug("Каталог \"" + dirMovie + "\" создан");
            } else {
                throw new CoreException("Не удалось создать каталог \"" + mediFolder.getAbsolutePath() 
                        + File.separator + filmName + "\"");
            }
        }

        return dirMovie;
    }

}
