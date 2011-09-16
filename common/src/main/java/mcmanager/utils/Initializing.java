package mcmanager.utils;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Log4jConfigurer;

public class Initializing implements InitializingBean {

    private List<String> logFiles;
    
    public List<String> getLogFiles() {
        return logFiles;
    }

    public void setLogFiles(List<String> logFiles) {
        this.logFiles = logFiles;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (String logFile : logFiles) {
            System.out.println(ApplicationUtils.getApplicationHome() + File.separator + 
                    "etc" + File.separator + logFile);
            Log4jConfigurer.initLogging(ApplicationUtils.getApplicationHome() + File.separator + 
                    "etc" + File.separator + logFile);    
        }
    }

}
