package mcmanager.monitor.utils;

public class MonitorSettings {
    
    private static class MonitorSettingsHolder {                                                                                                              
        private static MonitorSettings instance = new MonitorSettings();                                                                                             
    }                                                                                                                                                          

    /**
     * Сингелтон
     * @return {@link MonitorSettings}
     */
    public static synchronized MonitorSettings getInstance() {                                                                                                
        return MonitorSettingsHolder.instance;                                                                                                                
    }                             

    /**
     * Задержка в мс. после удаления торрента и сохранением нового
     */
    private int sleepAfterTorrentDelete;
    
    /**
     * Отсылка уведомления о закачки торрента
     * Возможные значения:
     * 0 -отключить отсылку сообщений 
     * 1 -отсылка сообщений при скачивание torrent'a
     * 2- отсылка сообщений после завершении закачки
     */
    private int sendMail;
    
    /**
     * Папка в которую помещаются текстовые файлы с имена торрентов которые закончили закачку
     */
    private String dirEndDownloadTorrents;
    
    public int getSleepAfterTorrentDelete() {
        return sleepAfterTorrentDelete;
    }

    public void setSleepAfterTorrentDelete(int sleepAfterTorrentDelete) {
        this.sleepAfterTorrentDelete = sleepAfterTorrentDelete;
    }

    public int getSendMail() {
        return sendMail;
    }

    public void setSendMail(int sendMail) {
        this.sendMail = sendMail;
    }

    public String getDirEndDownloadTorrents() {
        return dirEndDownloadTorrents;
    }

    public void setDirEndDownloadTorrents(String dirEndDownloadTorrents) {
        this.dirEndDownloadTorrents = dirEndDownloadTorrents;
    }
}
