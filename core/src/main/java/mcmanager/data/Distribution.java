package mcmanager.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import mcmanager.dao.DaoFactory;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Сущность "Раздача"
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)<br>
 *         Date: 28.08.2011
 */
@Entity
@Table(name = "distribution")
public class Distribution implements ObjectDB, Serializable {
    private static final long serialVersionUID = -5653550306094239999L;

    /**
     * Идентификатор записи
     */
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    /**
     * Ссылка на rutracker.org
     */
    @Column(name = "LINK_RUTRACKER", unique = true, nullable = false)
    private String linkRutracker;

    /**
     * Заголовок раздачи
     */
    @Column(name = "TITLE", length = 512, nullable = false)
    private String title;

    /**
     * Сообщение отправляемое на email
     */
    @Column(name = "MAIL_MESSAGE")
    private String mailMessage;

    /**
     * RegExp для обработки сообщения отпрааляемого на email
     */
    @Column(name = "MAIL_REGEXP")
    private String mailRegexp;

    /**
     * Ссылка на идентификатор группы
     */
    @JoinColumn(name = "GROUPS", nullable = false)
    @ManyToOne
    private Group group;

    /**
     * Статут раздачи, возможные значения {@link StatusEnum}
     */
    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private StatusEnum status;

    /**
     * Имя торрент файла
     */
    @Column(name = "TORRENT")
    private String torrent;

    /**
     * Ссылка на кинопоиск
     */
    @Column(name = "LINK_KINOPOISK")
    private String linkKinoposk;

    /**
     * RegExp по которому будет выбирать номер серии из названия файла(актуально
     * для сериалов)
     */
    @Column(name = "REGEXP_SERIAL_NUMBER")
    private String regexpSerialNumber;

    /**
     * Номер сезона(актуально для сериало)
     */
    @Column(name = "SEASON_NUMBER")
    private Integer seasonNumber;

    /**
     * Тип раздачи возможные значения {@link TypeDistributionEnum}
     */
    @Column(name = "TYPE")
    private Integer type;

    /**
     * Получить значение {@link #id}.
     * @return значение {@link #id}
     */
    public Long getId() {
        return id;
    }

    /**
     * Установить параметр {@link #id}.
     * @param id новое значение {@link #id}
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получить значение {@link #linkRutracker}.
     * @return значение {@link #linkRutracker}
     */
    public String getLinkRutracker() {
        return linkRutracker;
    }

    /**
     * Установить параметр {@link #linkRutracker}.
     * @param linkRutracker новое значение {@link #linkRutracker}
     */
    public void setLinkRutracker(String linkRutracker) {
        this.linkRutracker = linkRutracker;
    }

    /**
     * Получить значение {@link #title}.
     * @return значение {@link #title}
     */
    public String getTitle() {
        return title;
    }

    /**
     * Установить параметр {@link #title}.
     * @param title новое значение {@link #title}
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Получить значение {@link #mailMessage}.
     * @return значение {@link #mailMessage}
     */
    public String getMailMessage() {
        return mailMessage;
    }

    /**
     * Установить параметр {@link #mailMessage}.
     * @param mailMessage новое значение {@link #mailMessage}
     */
    public void setMailMessage(String mailMessage) {
        this.mailMessage = mailMessage;
    }

    /**
     * Получить значение {@link #mailRegexp}.
     * @return значение {@link #mailRegexp}
     */
    public String getMailRegexp() {
        return mailRegexp;
    }

    /**
     * Установить параметр {@link #mailRegexp}.
     * @param mailRegexp новое значение {@link #mailRegexp}
     */
    public void setMailRegexp(String mailRegexp) {
        this.mailRegexp = mailRegexp;
    }

    /**
     * Получить значение {@link #group}.
     * @return значение {@link #group}
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Установить параметр {@link #group}.
     * @param group новое значение {@link #group}
     */
    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * Получить значение {@link #status}.
     * @return значение {@link #status}
     */
    public StatusEnum getStatus() {
        return status;
    }

    /**
     * Установить параметр {@link #status}.
     * @param status новое значение {@link #status}
     */
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    /**
     * Получить значение {@link #torrent}.
     * @return значение {@link #torrent}
     */
    public String getTorrent() {
        return torrent;
    }

    /**
     * Установить параметр {@link #torrent}.
     * @param torrent новое значение {@link #torrent}
     */
    public void setTorrent(String torrent) {
        this.torrent = torrent;
    }

    /**
     * Получить значение {@link #linkKinoposk}.
     * @return значение {@link #linkKinoposk}
     */
    public String getLinkKinoposk() {
        return linkKinoposk;
    }

    /**
     * Установить параметр {@link #linkKinoposk}.
     * @param linkKinoposk новое значение {@link #linkKinoposk}
     */
    public void setLinkKinoposk(String linkKinoposk) {
        this.linkKinoposk = linkKinoposk;
    }

    /**
     * Получить значение {@link #regexpSerialNumber}.
     * @return значение {@link #regexpSerialNumber}
     */
    public String getRegexpSerialNumber() {
        return regexpSerialNumber;
    }

    /**
     * Установить параметр {@link #regexpSerialNumber}.
     * @param regexpSerialNumber новое значение {@link #regexpSerialNumber}
     */
    public void setRegexpSerialNumber(String regexpSerialNumber) {
        this.regexpSerialNumber = regexpSerialNumber;
    }

    /**
     * Получить значение {@link #seasonNumber}.
     * @return значение {@link #seasonNumber}
     */
    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    /**
     * Установить параметр {@link #seasonNumber}.
     * @param seasonNumber новое значение {@link #seasonNumber}
     */
    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    /**
     * Получить значение {@link #type}.
     * @return значение {@link #type}
     */
    public Integer getType() {
        return type;
    }

    /**
     * Установить параметр {@link #type}.
     * @param type новое значение {@link #type}
     */
    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new StringBuilder("Id: ").append(id)
                .append("\nLink rutracker: ").append(linkRutracker)
                .append("\nTitle: ").append(title).append("\nMail message: ")
                .append(mailMessage).append("\nMail regexp: ")
                .append(mailRegexp).append("\nGroup: ").append(group)
                .append("\nStatus: ").append(status).append("\nTorrent: ")
                .append(torrent).append("\nLink kinopoisk: ")
                .append(linkKinoposk).append("\nregexp serial number: ")
                .append(regexpSerialNumber).append("\nSeason number: ")
                .append(seasonNumber).append("\nType: ").append(type)
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