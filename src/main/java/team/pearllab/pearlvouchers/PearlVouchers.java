package team.pearllab.pearlvouchers;

import org.bukkit.plugin.java.JavaPlugin;
import team.pearllab.pearlvouchers.console.Console;
import team.pearllab.pearlvouchers.console.LogType;
import team.pearllab.pearlvouchers.lang.LangManager;
import team.pearllab.pearlvouchers.lang.Phrase;
import team.pearllab.pearlvouchers.utils.Configuration;

public final class PearlVouchers extends JavaPlugin {

    private Configuration pluginConfiguration;
    private LangManager langManager;

    @Override
    public void onEnable() {
        Console.sendMessage(LogType.ANNOUNCE, "Loading PearlGifts v" + getDescription().getVersion() + "...");

        this.pluginConfiguration = new Configuration(this);
        this.langManager = new LangManager(this, pluginConfiguration.language);
        loadPhrases();

        Console.sendMessage(LogType.ANNOUNCE, "Loaded PearlGifts v" + getDescription().getVersion() + "!");

    }

    private void loadPhrases() {
        for(Phrase phrase : Phrase.values()){
            langManager.getPhrase(phrase, null);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public Configuration getPluginConfiguration() {
        return pluginConfiguration;
    }

    public LangManager getLangManager() {
        return langManager;
    }
}
