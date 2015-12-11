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
    public static final String POST_SERVICE = "entities.post";

    //Date formats
    public static final String GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"; //Can't touch this
    public static final String VIEW_DATE_FORMAT = "dd/MM/yyyy";
    public static final String VIEW_TIME_FORMAT = "HH:mm";

    //Substring length in list view
    public static final int PREVIEW_STRING_LENGTH = 110;

    //Uruguay by default
    public static final int DEFAULT_COUNTRY_SPINNER_VALUE = 232;

}
