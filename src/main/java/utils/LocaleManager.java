package utils;

import java.util.Locale;

public class LocaleManager {
    public static final Locale RU_LOCALE = new Locale("ru");
    public static final Locale BE_LOCALE = new Locale("be_by");
    public static final Locale HR_LOCALE = new Locale("hr_ru");
    public static final Locale EN_LOCALE = new Locale("en_ca");

    private static Lang currentLang;

    public static Lang getCurrentLang() {return currentLang;}
    public static void setCurrentLang(Lang currentLang){LocaleManager.currentLang = currentLang;}
}
