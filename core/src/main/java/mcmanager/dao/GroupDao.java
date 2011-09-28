package mcmanager.dao;

import java.util.List;

import mcmanager.data.Distribution;
import mcmanager.data.Group;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Класс реализующий работу с БД таблицы groups
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 * Date: 28.08.2011
 */
public class GroupDao extends HibernateDaoSupport {

    /**
     * Добавить или обновить запись в таблице
     * @param group - группа
     */
    public void addGroup(Group group) {
        getHibernateTemplate().save(group);
    }

    public void updateGroup(Group group) {
        getHibernateTemplate().update(group);
    }

    /**
     * Удалить группу
     * @param id - идентификатор группы
     */
    public void deleteGroup(long id) {
        Group group = getGroup(id);
        if (group != null)
            getHibernateTemplate().delete(group);
    }

    /**
     * Получить группу из таблицы
     * @param id - идентификатор группы
     * @return группа
     */
    public Group getGroup(long id) {
        return getHibernateTemplate().get(Group.class, id);
    }


    public List<Group> getGroupByName(String name) {
        return getHibernateTemplate().findByCriteria(
                DetachedCriteria.forClass(Group.class).add(Restrictions.eq("name", name)));   
    }

    public List<Group> getAllGroup() {
        return getHibernateTemplate().findByCriteria(DetachedCriteria.forClass(Group.class));
    }

}
