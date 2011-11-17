package mcmanager.data;

import javax.persistence.Enumerated;

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
    
    private String image;
    
    /**
     * Констурктор енума
     * @param status - статус в цифровом виде
     * @param desc   - описание статуса
     */
    StatusEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
        this.image = name().toLowerCase() + ".png";
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

    public String getImage() {
        return image;
    }
    
    public static StatusEnum matcherByStatus(int status) {
        for (StatusEnum statusEnum : values()) {
            if (statusEnum.getStatus() == status) 
                return statusEnum;
        }
        return null;
    }
    
    public static StatusEnum matcherByDesc(String desc) {
        for (StatusEnum statusEnum : values()) {
            if (statusEnum.getDesc().equalsIgnoreCase(desc)) {
                return statusEnum;
            }
        }
        return null;
    }
}
