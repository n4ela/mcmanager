package mcmanager.android.utils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import android.os.Environment;
import android.util.Log;
import de.mindpipe.android.logging.log4j.LogConfigurator;

public class LogDb {
    public static final Logger log = Logger.getLogger(LogDb.class);
    static {
        final LogConfigurator logConfigurator = new LogConfigurator();
        Log.e("BALALB", Environment.getExternalStorageDirectory() + "/myapp.log");
        logConfigurator.setFileName(Environment.getExternalStorageDirectory() + "/myapp.log");
        logConfigurator.setRootLevel(Level.TRACE);
        logConfigurator.configure();
    }
}
