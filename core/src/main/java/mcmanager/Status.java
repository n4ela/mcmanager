package mcmanager;

public enum Status {
    OFF(0, "Выкл"),
    ON(1, "Вкл"),
    NEW(2, "Новый"),
    REPROCESSING(3, "Требует обработки");
    
    private int status;
    private String desc;
    
    Status(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
    
    public int getStatus() {
        return status;
    }
    
    public String getDesc() {
        return desc;
    }
    
}
