package mcmanager.data;

/**
 * Интерйфейс показывающий что его реализации являются объектами БД
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 * Date: 28.08.2011
 */
public interface ObjectDB {
    
    /**
     * Установить идентификатор
     * @param id - идентификатор
     */
    public void setId(Long id);
    
    /**
     * Получить идентификатор
     * @return идентификатор
     */
    public Long getId();

}
