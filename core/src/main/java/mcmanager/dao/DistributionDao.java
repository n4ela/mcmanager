package mcmanager.dao;

import java.util.List;
import java.util.regex.Pattern;

import mcmanager.data.Distribution;
import mcmanager.data.Group;
import mcmanager.data.StatusEnum;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Класс реализующий работы в БД с табличкой distribution
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 * Date: 28.08.2011
 */
public class DistributionDao extends HibernateDaoSupport {

    /**
     * Добавить раздачу в таблицу
     * TODO допилить exception
     * @param distribution - раздача 
     */
    public void addDistribution(Distribution distribution) {
        getHibernateTemplate().save(distribution);
    }

    /**
     * Обновить информацию о раздаче в таблице 
     * @param distribution - раздача
     */
    public void updateDistribution(Distribution distribution) {
        getHibernateTemplate().update(distribution);
    }
    
    /**
     * Удалить раздачу
     * @param id - идентификатор раздачи
     */
    public void removeDistribution(long id) {
        Distribution distribution = getDistribution(id);
        if (distribution != null)
            getHibernateTemplate().delete(distribution);
    }
    
    /**
     * Получить раздачу по ее ID
     * @param id - идентификатор раздачи
     * @return раздача
     */
    public Distribution getDistribution(long id) {
        return getHibernateTemplate().get(Distribution.class, id);
    }
    
    /**
     * Получить список раздач по статусу
     * @param status
     * @return
     */
    public List<Distribution> getDistributionByStatus(StatusEnum status) {
        return (List<Distribution>) getHibernateTemplate().findByCriteria(
                DetachedCriteria.forClass(Distribution.class).add(
                        Restrictions.eq("status", status)));
    }
    
    public List<Distribution> getDistributionByTorrent(String torrentName) {
        return (List<Distribution>) getHibernateTemplate().findByCriteria(
                DetachedCriteria.forClass(Distribution.class).add(
                        Restrictions.eq("torrent", torrentName)));
    }
    
    public List<Distribution> getAllDistribution() {
        return getHibernateTemplate().findByCriteria(DetachedCriteria.forClass(Distribution.class));
    }
    
}
