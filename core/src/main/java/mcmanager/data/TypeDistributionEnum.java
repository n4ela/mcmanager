package mcmanager.data;

/**
 * Enum типа раздачи
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 * Date: 28.08.2011
 */
public enum TypeDistributionEnum {
    /**
     * DVD
     */
    DVD(0, "DVD"),
    /**
     * Сериалы
     */
    SERIALS(1, "Сериал"),
    /**
     * Фильмы
     */
    FILMS(2, "Фильм"),
    /**
     * Прочее
     */
    SHARED(3, "Прочее");
    
    /**
     * Статус раздачи
     */
    private int type;
    /**
     * Описание статуса
     */
    private String desc;
    
    /**
     * Констурктор енума
     * @param status - статус в цифровом виде
     * @param desc   - описание статуса
     */
    TypeDistributionEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }
    
    /**
     * Получить статус
     * @return статус
     */
    public int getType() {
        return type;
    }
    
    /**
     * Получить описание статуса
     * @return описание статуса
     */
    public String getDesc() {
        return desc;
    }
   

}
