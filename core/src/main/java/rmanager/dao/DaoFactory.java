package rmanager.dao;

public class DaoFactory {
    
    private static FilmDao filmDao;                                                                                                                            

    private static GroupsDao groupsDao;                                                                                                                     

    private static class RuTrackerFactoryHolder {                                                                                                              
        static DaoFactory instance = new DaoFactory();                                                                                             
    }                                                                                                                                                          

    public static synchronized DaoFactory getInstance() {                                                                                                
        return RuTrackerFactoryHolder.instance;                                                                                                                
    }                                                                                                                                                          

    public FilmDao getFilmDao() {                                                                                                                              
        if (filmDao == null)                                                                                                                                   
            filmDao = new FilmDao();                                                                                                                       
        return filmDao;                                                                                                                                        
    }                                                                                                                                                          

    public GroupsDao getGroupDao() {                                                                                                                            
        if (groupsDao == null)                                                                                                                             
            groupsDao = new GroupsDao();                                                                                                                
        return groupsDao;                                                                                                                                  
    }                                                                                                                                                          
}
