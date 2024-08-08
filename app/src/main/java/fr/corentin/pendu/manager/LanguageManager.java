package fr.corentin.pendu.manager;


import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

/**
 * Class {@link LanguageManager}
 * @author Corentin Dupont
 * @version For project Info0306
 */

public class LanguageManager {
    public static void changeLocale(Resources res, String locale){
        Configuration config;
        config = new Configuration(res.getConfiguration());

        if (locale.equals("fr")) {
            config.locale = Locale.FRANCE;
        } else {
            config.locale = Locale.ENGLISH;
        }
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
}
