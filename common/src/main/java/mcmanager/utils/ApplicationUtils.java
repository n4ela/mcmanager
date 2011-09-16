package mcmanager.utils;

import java.io.File;

public class ApplicationUtils {

    public static String getApplicationHome() {
        return System.getProperty("catalina.home");
    }
}
