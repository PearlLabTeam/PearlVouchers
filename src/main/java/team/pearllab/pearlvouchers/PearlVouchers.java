package team.pearllab.pearlvouchers;

import org.bukkit.plugin.java.JavaPlugin;
import team.pearllab.pearlvouchers.console.Console;
import team.pearllab.pearlvouchers.console.LogType;
import team.pearllab.pearlvouchers.lang.LangManager;
import team.pearllab.pearlvouchers.lang.Phrase;
import team.pearllab.pearlvouchers.utils.Configuration;
import team.pearllab.pearlvouchers.voucher.VoucherHolder;

public final class PearlVouchers extends JavaPlugin {

    private Configuration pluginConfiguration;
    private LangManager langManager;

    private VoucherHolder voucherManager;

    @Override
    public void onEnable() {
        Console.sendMessage(LogType.ANNOUNCE, "Loading PearlVoucers v" + getDescription().getVersion() + "...");

        this.pluginConfiguration = new Configuration(this);
        this.langManager = new LangManager(this, pluginConfiguration.language);
        loadPhrases();

        Console.sendMessage(LogType.ANNOUNCE, "Loaded PearlVouchers v" + getDescription().getVersion() + "!");

        // Try to load the voucher manager.
        this.voucherManager = new VoucherHolder(this);
        voucherManager.loadVouchers();

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
