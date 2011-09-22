package mcmanager.monitor.utils;

public enum FilmTypeEnum {
    AVI(".avi"),
    MKV(".mkv"),
    SRT(".srt");
    
    private String type;
    
    FilmTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    
    public static boolean matcher(String path) {
        for (FilmTypeEnum typeEnum : values()) {
            if (path.endsWith(typeEnum.type))
                return true;
        }
        return false;
    }
}
