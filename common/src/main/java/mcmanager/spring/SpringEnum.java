package mcmanager.spring;

public enum SpringEnum {
    CORE("core-config"),
    MONITOR("monitor-config.xml"),
    QUARTZ("quartz-config");
    
    private final String config;
    
    SpringEnum(String config) {
        this.config = config;
    }

    public String getConfig() {
        return config;
    }
    
}
