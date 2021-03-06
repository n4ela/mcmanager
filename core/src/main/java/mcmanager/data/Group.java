package mcmanager.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Сущность "Группа"
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 * Date: 28.08.2011
 */
@Entity
@Table(name = "groups")
public class Group implements ObjectDB, Serializable {

    private static final long serialVersionUID = -898599405561047929L;

    /**
     * Идентификатор группы
     */
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    /**
     * Название группы
     */
    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    /**
     * Каталог для сохранения торрентов
     */
    @Column(name = "TORRENT_FOLDER", length=512, nullable = false)
    private String torrentFolder;
    
    /**
     * Каталог с медиацентра
     */
    @Column(name = "MEDIA_FOLDER", length=512)
    private String mediaFolder;
    
    /**
     * Каталог с фильмами
     */
    @Column(name = "DOWNLOAD_FOLDER", length=512)
    private String downloadFolder;
    
    /**
     * Текст сообщения отправляемый на емейл
     */
    @Column(name = "EMAIL_MESSAGE", length=512)
    private String emailMessage;

    /**
     * RegExp выражение для разбора сообщения уведомления
     */
    @Column(name = "EMAIL_REGEXP")
    private String emailRegexp;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTorrentFolder() {
        return torrentFolder;
    }

    public void setTorrentFolder(String torrentFolder) {
        this.torrentFolder = torrentFolder;
    }

    public String getMediaFolder() {
        return mediaFolder;
    }

    public void setMediaFolder(String mediaFolder) {
        this.mediaFolder = mediaFolder;
    }

    public String getDownloadFolder() {
        return downloadFolder;
    }

    public void setDownloadFolder(String downloadFolder) {
        this.downloadFolder = downloadFolder;
    }

    public String getEmailMessage() {
        return emailMessage;
    }

    public void setEmailMessage(String emailMessage) {
        this.emailMessage = emailMessage;
    }

    public String getEmailRegexp() {
        return emailRegexp;
    }

    public void setEmailRegexp(String emailRegexp) {
        this.emailRegexp = emailRegexp;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null && this != null)
            return false;
        Group group = (Group)o;
        if ((this.id == null && group.id != null) || !this.id.equals(group.id))
            return false;
        if ((this.name == null && group.name != null) || !this.name.equals(group.name))
            return false;
        if ((this.mediaFolder == null && group.mediaFolder != null) || !this.mediaFolder.equals(group.mediaFolder))
            return false;
        if ((this.torrentFolder == null && group.torrentFolder != null) || !this.torrentFolder.equals(group.torrentFolder))
            return false;
        if ((this.downloadFolder == null && group.downloadFolder != null) || !this.downloadFolder.equals(group.downloadFolder))
            return false;
        if ((this.emailMessage == null && group.emailMessage != null) || !this.emailMessage.equals(group.emailMessage))
            return false;
        if ((this.emailRegexp == null && group.emailRegexp != null) || !this.emailRegexp.equals(group.emailRegexp))
            return false;
        return true;
    }
    
    
    @Override
    public int hashCode() {
        return id.hashCode() * 3 +
               name.hashCode() * 7 + 
               mediaFolder.hashCode() * 3+ 
               torrentFolder.hashCode() * 3 + 
               downloadFolder.hashCode() * 7 +
               emailMessage.hashCode() * 3+ 
               emailRegexp.hashCode() * 7;
    }

    @Override
    public String toString() {
        return new StringBuilder("Id: ").append(id)
                .append("\nName: ").append(name)
                .append("\nTorrent folder: ").append(torrentFolder)
                .append("\nMedia folder: ").append(mediaFolder)
                .append("\nDownload folder: ").append(downloadFolder)
                .append("\nEmail message: ").append(emailMessage)
                .append("\nEmail regexp: ").append(emailRegexp)
                .toString();
    }

    /**
     * Выводит всю информацию о сущности в одну сплошную строку
     * @return информация о сущности
     */
    public String toLineString() {
        return toString().replace("\n", " | ");
    }
}