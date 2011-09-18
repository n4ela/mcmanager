package mcmanager.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public enum LogEnum {
    MONITOR("monitor"),
    MONITOR_NEW("monitor-new"),
    KINOPOISK("kinopoisk"),
    EMAIL("email"),
    FILEMONITOR("file-monitor");
    
    private String logName;
    
    private LogEnum(String logName) {
        this.logName = logName;
    }
    
    public Log getLog() {
        return LogFactory.getLog(logName);
    }
}
