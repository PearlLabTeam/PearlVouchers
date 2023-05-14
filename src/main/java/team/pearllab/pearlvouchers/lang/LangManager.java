package team.pearllab.pearlvouchers.lang;


import org.apache.commons.lang3.exception.ExceptionUtils;
import org.bukkit.entity.Player;
import org.simpleyaml.configuration.file.YamlFile;
import team.pearllab.pearlvouchers.PearlVouchers;
import team.pearllab.pearlvouchers.console.Console;
import team.pearllab.pearlvouchers.console.LogType;

import java.io.File;

public class LangManager {

    private final PearlVouchers plugin;

    private Language language;
    private YamlFile languageFile;

    public LangManager(PearlVouchers plugin, Language language) {
        this.plugin = plugin;
        this.language = language;

        createLanguageFiles();
        loadLanguageFile();
    }

    public String getPhrase(Phrase phrase, Player player) {

        if (!languageFile.contains(phrase.getKey())) {
            try {
                languageFile.set(phrase.getKey(), "&cUpdate section &e" + phrase.getKey() + " &cin your language file, then reload the plugin.");
                languageFile.setComment(phrase.getKey(), "This section was needed but not found and has been updated.");
                languageFile.save();
            } catch (Exception e) {
                Console.sendMessage(LogType.WARNING, "There was an issue while trying to update a non-existing " +
                        "message from your language file...\n" + ExceptionUtils.getStackTrace(e));
            }
        }

        String message = languageFile.getString(phrase.getKey());

        return message;
    }

    private void loadLanguageFile() {
        this.languageFile = new YamlFile(Language.getFilePath(this.language));
        try {
            this.languageFile.load();
        } catch (Exception e) {
            Console.sendMessage(LogType.ERROR, "Error encountered while attempting to load " + this.language.getFileName() + " file." + "\n" + ExceptionUtils.getStackTrace(e));
        }
    }

    private void createLanguageFiles() {
        File directory = new File(Language.ENGLISH.getDirectoryName());

        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (Language l : Language.values()) {
            File file = new File(Language.getFilePath(l));

            if (!file.exists()) {
                try {
                    plugin.saveResource("language/" + l.getFileName(), true);
                } catch (Exception e) {
                    Console.sendMessage(LogType.ERROR, "Error encountered while attempting to create " + l.getFileName() + " file." + "\n" + ExceptionUtils.getStackTrace(e));
                }
            }

        }
    }

    public void reloadLanguage() {
        this.language = plugin.getPluginConfiguration().language;
        createLanguageFiles();
        loadLanguageFile();
    }

    public YamlFile getLanguageFile() {
        return languageFile;
    }
}
