package utils;

import java.util.Locale;

public class LocaleManager {
    public static final Locale RU_LOCALE = new Locale("ru");
    public static final Locale BE_LOCALE = new Locale("be_by");
    public static final Locale HR_LOCALE = new Locale("hr_ru");
    public static final Locale EN_LOCALE = new Locale("en_ca");
    public static final Locale ES_LOCALE = new Locale("es");
    public static final Locale NO_LOCALE = new Locale("no_NO");
    public static final Locale WG_LOCALE = new Locale("wg");
    public static final Locale CS_LOCALE = new Locale("cs");

    private static Lang currentLang;

    public static Lang getCurrentLang() {return currentLang;}
    public static void setCurrentLang(Lang currentLang){LocaleManager.currentLang = currentLang;}
}
