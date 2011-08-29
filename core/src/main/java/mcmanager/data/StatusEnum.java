package mcmanager.data;

/**
 * ENUM возможных статусов раздачи
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 * Date: 28.08.2011
 */
public enum StatusEnum {
    /**
     * Раздача выключена
     */
    OFF(0, "Выключена"),
    /**
     * Отслеживать раздачу
     */
    TRACK_ON(1, "Отслеживать"),
    /**
     * Новая
     */
    NEW(2, "Новая"),
    /**
     * Скачивание
     */
    DOWNLOAD(3, "Скачивание"),
    /**
     * Требует обработки
     */
    PROCESSING(4, "Требует обработки");
    
    /**
     * Статус раздачи
     */
    private int status;
    /**
     * Описание статуса
     */
    private String desc;
    
    /**
     * Констурктор енума
     * @param status - статус в цифровом виде
     * @param desc   - описание статуса
     */
    StatusEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
    
    /**
     * Получить статус
     * @return статус
     */
    public int getStatus() {
        return status;
    }
    
    /**
     * Получить описание статуса
     * @return описание статуса
     */
    public String getDesc() {
        return desc;
    }
   
}
