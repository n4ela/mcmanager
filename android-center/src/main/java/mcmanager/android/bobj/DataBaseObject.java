package mcmanager.android.bobj;

import mcmanager.kinopoisk.info.Thumb;

import java.io.Closeable;
import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: n4ela
 * Date: 26.02.12
 * Time: 4:36
 * To change this template use File | Settings | File Templates.
 */
public interface DataBaseObject extends Serializable{
    public String getTitle();
    public String getPlot();
    public List<Thumb> getThumb();
    public String getActiveThumb();
    public void setActiveThumb(String activeThumb);
}
