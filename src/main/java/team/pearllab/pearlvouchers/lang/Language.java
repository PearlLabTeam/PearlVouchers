package team.pearllab.pearlvouchers.lang;

import team.pearllab.pearlvouchers.console.Console;
import team.pearllab.pearlvouchers.console.LogType;

public enum Language {
    ENGLISH("English", "english.yml"),
    SPANISH("Español", "spanish.yml"),
    GERMAN("Deutsch", "deutsch.yml");

    private static final String directoryName = "plugins/PearlVouchers/language";
    private String key;
    private String fileName;


    Language(String key, String fileName){
        this.key = key;
        this.fileName = fileName;
    }

    public String getKey() {
        return key;
    }

    public String getFileName() {
        return fileName;
    }

    public static String getFilePath(Language language) {
        return directoryName + "/" + language.getFileName();
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public static Language withValue(String value) {
        for (Language language : Language.values()) {
            if (language.getKey().equalsIgnoreCase(value)) {
                return language;
            }
        }
        Console.sendMessage(LogType.ERROR, "The language " + value + " was not found, defaulting to English.");
        return ENGLISH;
    }
}
