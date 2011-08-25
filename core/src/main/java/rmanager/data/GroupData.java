package rmanager.data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Класс описывающий таблицу categories
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 *
 */
@Entity
@Table(name = "groups",
uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class GroupData implements Serializable {

    private static final long serialVersionUID = -898599405561047929L;

    @Id
    @GeneratedValue
    private Long id;

    @Basic(optional=false)
    private String name;

    @Basic(optional=false)
    @Column(length=512)
    private String folder;

    @Column(length=512)
    private String message;

    private String regexp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    @Override
    public String toString() {
        return new StringBuilder("Id: " + id + "\nName: " + name + "\nFolder: "
                + folder + "\nMessage: " + message + "\nRegExp: " + regexp).toString();
    }

    public String toLineString() {
        return toString().replace("\n", " | ");
    }
}