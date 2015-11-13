package tecno.competitionplatform.config;

/**
 * Created by Andres on 19/10/2015.
 */
public class Config {

    //Url Services
    public static final String BASE_URL_SERVICES = "http://tecnocompetition.ddns.net:8080/pfinal/services/";
    public static final String AUTHENTICATION_SERVICE = "auth";
    public static final String MAINCOMPETITION_SERVICE = "entities.maincompetition";
    public static final String COMPETITION_SERVICE = "entities.competition";
    public static final String SUBSCRIBER_SERVICE = "suscriber";
    public static final String COUNTRY_SERVICE = "entities.country";

    //Date formats
    public static final String GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"; //Can't touch this
    public static final String VIEW_DATE_FORMAT = "dd/MM/yyyy";
    public static final String VIEW_DATETIME_FORMAT = "dd/MM/yyyy HH:mm";


}
