package mcmanager.android.bobj;

import mcmanager.kinopoisk.info.Episodedetails;
import mcmanager.kinopoisk.info.Tvshow;

import java.io.Serializable;

public class EpisodeAndroid extends Episodedetails implements Serializable, DataBaseObject {

    private Long updateTime;
    private String filenameandpath;

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String getActiveThumb() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setActiveThumb(String activeThumb) {
    }

    public String getFilenameandpath() {
        return filenameandpath;
    }

    public void setFilenameandpath(String filenameandpath) {
        this.filenameandpath = filenameandpath;
    }

}
