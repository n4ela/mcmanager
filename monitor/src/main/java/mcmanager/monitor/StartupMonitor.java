package mcmanager.monitor;

import mcmanager.spring.SpringEnum;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartupMonitor {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext(SpringEnum.CORE.getConfig());
        new ClassPathXmlApplicationContext(SpringEnum.MONITOR.getConfig());
        new ClassPathXmlApplicationContext(SpringEnum.QUARTZ.getConfig());
    }
    
}
