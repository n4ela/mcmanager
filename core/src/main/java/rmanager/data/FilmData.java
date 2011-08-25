package rmanager.data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

/**
 * Сущность ФИЛЬМ(раздача) в БД
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 */
@Entity
@Table(name="films",
       uniqueConstraints = @UniqueConstraint(columnNames = { "link" }))
public class FilmData implements Serializable {

    private static final long serialVersionUID = -5653550306094239999L;

    @Id
    @GeneratedValue
    private long id;

    @Basic(optional=false)
    private String link;

    @Basic(optional=false)
    @Column(length=512)
    private String title;

    @Basic(optional=false)
    private String message;

    @Basic(optional=false)
    private String regexp;

    @Basic(optional=false)
    private String groups;

    @Basic(optional=false)
    private Integer status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGroup() {
        return groups;
    }

    public void setGroup(String group) {
        this.groups = group;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new StringBuilder("Id: ").append(id).append(" ;\nLink: ").append(link)
                .append(";\nTitle: ").append(title).append(";\nRegExp: ").append(regexp)
                .append(";\nMessage: ").append(message)
                .append(";\nGroup: ").append(groups).append(";\nStatus: ").append(status).append(";")
                .toString();
    }

    public String toLineString() {
        return toString().replace("\n", " | ");
    }
}