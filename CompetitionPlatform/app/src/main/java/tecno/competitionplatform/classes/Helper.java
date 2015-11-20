package tecno.competitionplatform.classes;

import tecno.competitionplatform.config.Config;

/**
 * Created by Andres on 19/11/2015.
 */
public class Helper {

    private static Helper instance = null;
    protected Helper() {
        // Exists only to defeat instantiation.
    }
    public static Helper getInstance() {
        if(instance == null) {
            instance = new Helper();
        }
        return instance;
    }

    public String getPreviewString(String string) {
        if(string.length() > Config.PREVIEW_STRING_LENGTH ){
            return string.substring(0, Config.PREVIEW_STRING_LENGTH) + "...";
        }
        return string;

    }
}
