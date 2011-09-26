package mcmanager.utils;

public class RuTrackerSettings {

    private static class RuTrackerSettingsHolder {                                                                                                              
        private static RuTrackerSettings instance = new RuTrackerSettings();                                                                                             
    }                                                                                                                                                          

    public static synchronized RuTrackerSettings getInstance() {                                                                                                
        return RuTrackerSettingsHolder.instance;                                                                                                                
    }                             
    
    private String login;
    
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
