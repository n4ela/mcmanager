package mcmanager.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import mcmanager.data.Distribution;

/**
 * Класс реализующий работы в БД с табличкой distribution
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 * Date: 28.08.2011
 */
public class DistributionDao extends HibernateDaoSupport {

    /**
     * Добавить или обновить раздачу в таблице
     * @param distribution - раздача 
     */
    public void addOrUpdateDistribution(Distribution distribution) {
        getHibernateTemplate().save(distribution);
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
}
