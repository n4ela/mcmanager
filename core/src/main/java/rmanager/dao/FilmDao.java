package rmanager.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import rmanager.data.FilmData;

/**
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 */
public class FilmDao extends HibernateDaoSupport {

    public void addFilm(FilmData film) {
        getHibernateTemplate().save(film);
    }

    public void updateFilm(FilmData film) {
        getHibernateTemplate().update(film);
    }

    public void removeFilm(long id) {
        getHibernateTemplate().delete(new Object());
    }
}
