package mcmanager.android.bobj;

import java.io.Serializable;

import mcmanager.kinopoisk.info.Actor;
import mcmanager.kinopoisk.info.Fileinfo;
import mcmanager.kinopoisk.info.Movie;
import mcmanager.kinopoisk.info.Thumb;
import android.os.Parcel;
import android.os.Parcelable;

public class MovieAndroid extends Movie implements Serializable {

    private String activeThumb;
    
    private Long updateTime;

    public String getActiveThumb() {
        return activeThumb;
    }

    public void setActiveThumb(String activeThumb) {
        this.activeThumb = activeThumb;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
    
}
