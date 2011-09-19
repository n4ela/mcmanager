package mcmanager.spring;

public enum SpringEnum {
    CORE("classpath:core-config.xml"),
    MONITOR("classpath:monitor-config.xml"),
    QUARTZ("classpath:quartz-config.xml");
    
    private final String config;
    
    SpringEnum(String config) {
        this.config = config;
    }

    public String getConfig() {
        return config;
    }
    
}
