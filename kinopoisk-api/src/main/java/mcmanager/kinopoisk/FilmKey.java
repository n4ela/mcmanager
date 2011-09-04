package mcmanager.kinopoisk;

/**
 * Енум значений которые нам надо получить со страницы конопоиска
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 * Date: 04.09.2011
 */
public enum FilmKey {
    DIRECTOR("режиссер"),
    RUNTIME("время"),
    COUNTRY("страна"),
    TAGLINE("слоган"),
    CREDITDS("сценарий"),
    GENRE("жанр"),
    MPAA("рейтинг MPAA"),
    YEAR("год");
    
    FilmKey(String value) {
        this.value = value;
    }
    
    private String value;
    
    public String getValue() {
        return value;
    }
}
