package mcmanager.dao;

/**
 * Фактория объектов БД
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 * Date: 28.08.2011
 */
public class DaoFactory {
    
    /**
     * Сущность "Раздача"
     */
    private static DistributionDao distributionDao;                                                                                                                            

    /**
     * Сущность "Фильм"
     */
    private static GroupDao groupDao;                                                                                                                     


    private static class DaoFactoryHolder {                                                                                                              
        private static DaoFactory instance = new DaoFactory();                                                                                             
    }                                                                                                                                                          

    /**
     * Сингелтон
     * @return {@link DaoFactory}
     */
    public static synchronized DaoFactory getInstance() {                                                                                                
        return DaoFactoryHolder.instance;                                                                                                                
    }                             
    
    /**
     * Получить ДАО объект раздачи
     * @return ДАО объект раздачи
     */
    public DistributionDao getDistributionDao() {                                                                                                                              
        return distributionDao;                                                                                                                                        
    }                                                                                                                                                          
    
    /**
     * Установить ДАО раздачи
     * @param distributionDao - объект раздачи
     */
    public static void setDistributionDao(DistributionDao distributionDao) {
        System.out.println("setDistributionDao");
        DaoFactory.distributionDao = distributionDao;
    }

    /**
     * Получить ДАО объект группы
     * @return ДАО объект группы
     */
    public GroupDao getGroupDao() {                                                                                                                            
        return groupDao;                                                                                                                                  
    }

    /**
     * Утсановить ДАО объект группы
     * @param groupDao - объект группы
     */
    public static void setGroupDao(GroupDao groupDao) {
        DaoFactory.groupDao = groupDao;
    }                                                                                                                                                          
}
